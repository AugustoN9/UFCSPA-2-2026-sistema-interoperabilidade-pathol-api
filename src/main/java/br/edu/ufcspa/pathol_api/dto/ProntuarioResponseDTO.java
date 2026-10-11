package br.edu.ufcspa.pathol_api.dto;

public record ProntuarioResponseDTO(
    Long id,
    String numeroProntuario,
    String historico,
    PacienteResponseDTO paciente
) {}