package br.edu.ufcspa.pathol_api.dto;

import java.util.List;

public record BlocoDTO(
    String codigoBloco,     // Ex: 1/3, 2/3
    List<LaminaDTO> laminas // Relação 1 para N (mínimo 1 lâmina por bloco)
) {}