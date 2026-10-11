package br.edu.ufcspa.pathol_api.dto;

import java.util.List;

import br.edu.ufcspa.pathol_api.model.TipoExame;

public record ExameRequestDTO(
    String numeroExame,                 // Ex: AP-2026-08912
    TipoExame tipoExame,                // ANATOMOPATOLOGICO, CITOPATOLOGICO...
    String pacienteProntuario,          // Identificador pseudonimizado (LGPD)
    String cidSugerido,                 // Codificação CID-10/CID-11 sugerida (Ex: K29.5)
    String macroscopia,                 // Descrição macrossópica (peça/frasco/fragmentos)
    String microscopia,                 // Descrição microscópica detalhada
    String diagnosticoSugerido,         // Conclusão diagnóstica
    List<FrascoDTO> frascos             // Lista de frascos/amostras do exame
) {}