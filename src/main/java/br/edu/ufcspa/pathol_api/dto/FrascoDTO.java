package br.edu.ufcspa.pathol_api.dto;

import java.util.List;

public record FrascoDTO(
    Integer posicao,               // Ex: 1, 2, 3
    String topografia,             // Ex: Região gástrica alta, Cárdia, Antro
    Integer quantidadeFragmentos,  // Ex: 3, 4, 2
    List<BlocoDTO> blocos          // Blocos gerados a partir da amostra
) {}