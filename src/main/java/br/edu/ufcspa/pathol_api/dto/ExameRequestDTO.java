package br.edu.ufcspa.pathol_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.edu.ufcspa.pathol_api.model.TipoExame;

public record ExameRequestDTO(
    String numeroExame,
    TipoExame tipoExame,
    String pacienteProntuario,
    String cidSugerido,
    LocalDateTime dataColetaProcedimento,
    LocalDateTime dataSolicitacao,
    LocalDateTime dataRecebimento,
    LocalDateTime dataMacroscopia,
    LocalDateTime dataProcessamentoHistologico,
    LocalDateTime dataRecebimentoLamina,
    LocalDateTime dataLiberacaoLaudo,
    String macroscopia,
    String microscopia,
    String diagnosticoSugerido,
    List<FrascoDTO> frascos
) {}