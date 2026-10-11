package br.edu.ufcspa.pathol_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcspa.pathol_api.dto.CidCountResponseDTO;
import br.edu.ufcspa.pathol_api.dto.ExameRequestDTO;
import br.edu.ufcspa.pathol_api.dto.ExameResponseDTO;
import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.service.ExameService;
import br.edu.ufcspa.pathol_api.service.FhirInteropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/exames")
@CrossOrigin(origins = "*")
@Tag(name = "Exames Patológicos", description = "Endpoints para gestão de exames e exportação HL7 FHIR R4")
public class ExameController {

    private final ExameService exameService;
    private final FhirInteropService fhirInteropService;

    public ExameController(ExameService exameService, FhirInteropService fhirInteropService) {
        this.exameService = exameService;
        this.fhirInteropService = fhirInteropService;
    }

    // 1. CRIAR LAUDO (POST)
    @Operation(summary = "Cadastrar um novo exame")
    @PostMapping
    public ResponseEntity<ExameResponseDTO> criarExame(@RequestBody ExameRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exameService.salvar(dto));
    }

    // 2. LISTAR TODOS OS LAUDOS (GET)
    @Operation(summary = "Listar todos os exames")
    @GetMapping
    public ResponseEntity<List<ExameResponseDTO>> listarExames() {
        return ResponseEntity.ok(exameService.listarTodos());
    }

    // 3. BUSCAR LAUDO POR ID (GET)
    @Operation(summary = "Buscar exame por ID da base de dados")
    @GetMapping("/{id}")
    public ResponseEntity<ExameResponseDTO> buscarPorId(
            @Parameter(description = "Identificador único da base de dados (ID)", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(exameService.buscarPorId(id));
    }

    // 4. BUSCAR POR NÚMERO DO EXAME (GET)
    @Operation(summary = "Buscar exame pelo número/código único do laudo")
    @GetMapping("/numero/{numeroExame}")
    public ResponseEntity<ExameResponseDTO> buscarPorNumeroExame(
            @Parameter(description = "Número/Código do exame no formato 'EX-YYYY-XXXX'", required = true, example = "EX-2026-1001")
            @PathVariable("numeroExame") String numeroExame) {
        return ResponseEntity.ok(exameService.buscarPorNumeroExame(numeroExame));
    }

    // 5. BUSCAR POR PRONTUÁRIO DO PACIENTE (GET)
    @Operation(summary = "Buscar exames vinculados a um prontuário de paciente")
    @GetMapping("/paciente/{pacienteProntuario}")
    public ResponseEntity<List<ExameResponseDTO>> buscarPorPacienteProntuario(
            @Parameter(description = "Código de prontuário do paciente", required = true, example = "PED-1001")
            @PathVariable("pacienteProntuario") String pacienteProntuario) {
        return ResponseEntity.ok(exameService.buscarPorPacienteProntuario(pacienteProntuario));
    }

    // 6. ATUALIZAR LAUDO (PUT)
    @Operation(summary = "Atualizar um exame existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ExameResponseDTO> atualizarExame(
            @Parameter(description = "ID do exame a ser atualizado", required = true)
            @PathVariable Long id, 
            @RequestBody ExameRequestDTO dto) {
        return ResponseEntity.ok(exameService.atualizar(id, dto));
    }

    // 7. DELETAR LAUDO (DELETE)
    @Operation(summary = "Excluir um exame por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExame(
            @Parameter(description = "ID do exame a ser removido", required = true)
            @PathVariable Long id) {
        exameService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // BUSCAR LISTA DE EXAMES POR CID SUGERIDO (GET)
    @Operation(summary = "Buscar exames filtrados por CID Sugerido")
    @GetMapping("/cid/{cidSugerido}")
    public ResponseEntity<List<ExameResponseDTO>> buscarPorCidSugerido(
            @Parameter(description = "Código CID-10 / CID-11 (ex: K29.5)", required = true, example = "K29.5")
            @PathVariable("cidSugerido") String cidSugerido) {
        return ResponseEntity.ok(exameService.buscarPorCidSugerido(cidSugerido));
    }

    // OBTER CONTAGEM / MÉTRICA POR CID SUGERIDO (GET)
    @Operation(summary = "Obter contagem de exames por CID Sugerido (Métrica Epidemiológica)")
    @GetMapping("/cid/{cidSugerido}/contagem")
    public ResponseEntity<CidCountResponseDTO> contarPorCidSugerido(
            @Parameter(description = "Código CID-10 / CID-11 (ex: K29.5)", required = true, example = "K29.5")
            @PathVariable("cidSugerido") String cidSugerido) {
        long total = exameService.contarPorCidSugerido(cidSugerido);
        return ResponseEntity.ok(new CidCountResponseDTO(cidSugerido, total));
    }

    // 8. EXPORTAR FHIR R4 (GET)
    @Operation(summary = "Exportar exame no padrão HL7 FHIR R4")
    @GetMapping(value = "/{id}/fhir", produces = "application/fhir+json")
    public ResponseEntity<String> obterLaudoFhir(
            @Parameter(description = "ID do exame para conversão HL7 FHIR R4", required = true)
            @PathVariable Long id) {
        Exame exame = exameService.buscarEntidadePorId(id);
        String fhirJson = fhirInteropService.exportarParaFhirJson(exame);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(fhirJson);
    }
}