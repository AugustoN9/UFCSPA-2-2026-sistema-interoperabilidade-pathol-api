package br.edu.ufcspa.pathol_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufcspa.pathol_api.dto.ProntuarioRequestDTO;
import br.edu.ufcspa.pathol_api.dto.ProntuarioResponseDTO;
import br.edu.ufcspa.pathol_api.service.ProntuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Prontuários", description = "Gerenciamento de prontuários clínicos e vínculo com pacientes")
@RestController
@RequestMapping("/api/v1/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @Operation(summary = "Criar novo prontuário vinculado a um paciente")
    @PostMapping
    public ResponseEntity<ProntuarioResponseDTO> criar(@RequestBody ProntuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prontuarioService.criar(dto));
    }

    @Operation(summary = "Listar todos os prontuários")
    @GetMapping
    public ResponseEntity<List<ProntuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(prontuarioService.listarTodos());
    }

    @Operation(summary = "Buscar prontuário pelo número do prontuário")
    @GetMapping("/numero/{numeroProntuario}")
    public ResponseEntity<ProntuarioResponseDTO> buscarPorNumero(@PathVariable String numeroProntuario) {
        return ResponseEntity.ok(prontuarioService.buscarPorNumero(numeroProntuario));
    }

    @Operation(summary = "Buscar prontuários vinculados ao CPF do paciente")
    @GetMapping("/paciente/{cpf}")
    public ResponseEntity<List<ProntuarioResponseDTO>> buscarPorCpfPaciente(@PathVariable String cpf) {
        return ResponseEntity.ok(prontuarioService.buscarPorCpfPaciente(cpf));
    }
}