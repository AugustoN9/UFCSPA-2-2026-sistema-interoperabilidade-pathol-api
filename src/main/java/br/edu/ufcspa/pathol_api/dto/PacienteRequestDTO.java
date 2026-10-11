package br.edu.ufcspa.pathol_api.dto;

import java.time.LocalDate;

import br.edu.ufcspa.pathol_api.model.SexoBiologico;

public record PacienteRequestDTO(
    String nome,
    String cpf,
    LocalDate dataNascimento,
    SexoBiologico sexoBiologico
) {}