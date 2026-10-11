package br.edu.ufcspa.pathol_api.dto;

public record CidCountResponseDTO(
    String cidSugerido,
    long totalExames
) {}