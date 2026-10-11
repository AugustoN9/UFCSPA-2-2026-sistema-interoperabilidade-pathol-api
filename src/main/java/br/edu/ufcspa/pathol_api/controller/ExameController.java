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

import br.edu.ufcspa.pathol_api.dto.ExameRequestDTO;
import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.service.ExameService;
import br.edu.ufcspa.pathol_api.service.FhirInteropService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
        summary = "Cadastrar um novo exame",
        description = "Cria um exame patológico no banco de dados e retorna o objeto persistido."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Exame criado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exame.class))
    )
    @PostMapping
    public ResponseEntity<Exame> criarExame(@RequestBody ExameRequestDTO dto) {
        Exame novoExame = exameService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoExame);
    }

    // 2. LISTAR TODOS OS LAUDOS (GET)
    @Operation(summary = "Listar todos os exames")
    @GetMapping
    public ResponseEntity<List<Exame>> listarExames() {
        return ResponseEntity.ok(exameService.listarTodos());
    }

    // 3. BUSCAR LAUDO POR ID (GET)
    @Operation(summary = "Buscar exame por ID da base de dados")
    @ApiResponse(
        responseCode = "200",
        description = "Exame localizado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exame.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<Exame> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(exameService.buscarPorId(id));
    }

    // 4. BUSCAR POR NÚMERO DO EXAME (GET)
    @Operation(summary = "Buscar exame pelo número/código único do laudo", description = "Exemplo: EX-2026-1001")
    @ApiResponse(
        responseCode = "200",
        description = "Exame localizado por número",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exame.class))
    )
    @GetMapping("/numero/{numeroExame}")
    public ResponseEntity<Exame> buscarPorNumeroExame(@PathVariable String numeroExame) {
        return ResponseEntity.ok(exameService.buscarPorNumeroExame(numeroExame));
    }

    // 5. BUSCAR POR PRONTUÁRIO DO PACIENTE (GET)
    @Operation(summary = "Buscar exames vinculados a um prontuário de paciente", description = "Exemplo: PED-1001")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de exames do prontuário localizada",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Exame.class))
    )
    @GetMapping("/paciente/{pacienteProntuario}")
    public ResponseEntity<List<Exame>> buscarPorPacienteProntuario(@PathVariable String pacienteProntuario) {
        return ResponseEntity.ok(exameService.buscarPorPacienteProntuario(pacienteProntuario));
    }

    // 6. ATUALIZAR LAUDO (PUT)
    @Operation(summary = "Atualizar um exame existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Exame> atualizarExame(@PathVariable Long id, @RequestBody ExameRequestDTO dto) {
        Exame exameAtualizado = exameService.atualizar(id, dto);
        return ResponseEntity.ok(exameAtualizado);
    }

    // 7. DELETAR LAUDO (DELETE)
    @Operation(summary = "Excluir um exame por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExame(@PathVariable Long id) {
        exameService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // 8. EXPORTAR FHIR R4 (GET)
    @Operation(
        summary = "Exportar exame no padrão HL7 FHIR R4",
        description = "Gera um Bundle FHIR R4 (JSON) contendo DiagnosticReport (LOINC 11526-1 e CID), Specimen e Observation (SNOMED CT)."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Bundle FHIR R4 exportado com sucesso",
        content = @Content(
            mediaType = "application/fhir+json",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(
                value = "{\n  \"resourceType\": \"Bundle\",\n  \"type\": \"collection\",\n  \"entry\": [...]\n}"
            )
        )
    )
    @GetMapping(value = "/{id}/fhir", produces = "application/fhir+json")
    public ResponseEntity<String> obterLaudoFhir(@PathVariable Long id) {
        Exame exame = exameService.buscarPorId(id);
        String fhirJson = fhirInteropService.exportarParaFhirJson(exame);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(fhirJson);
    }
}