package br.edu.ufcspa.pathol_api.dto;

public record ProntuarioRequestDTO(
    String numeroProntuario,
    String historico,
    String cpfPaciente // Vincula o Prontuário ao Paciente existente via CPF
) {}