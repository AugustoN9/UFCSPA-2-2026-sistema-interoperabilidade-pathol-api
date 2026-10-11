package br.edu.ufcspa.pathol_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.edu.ufcspa.pathol_api.model.TipoExame;

public class ExameResponseDTO {

    private Long id;
    private String numeroExame;
    private TipoExame tipoExame;
    private String pacienteProntuario;
    private String cidSugerido;

    // Os 7 Marcos Temporais
    private LocalDateTime dataColetaProcedimento;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataRecebimento;
    private LocalDateTime dataMacroscopia;
    private LocalDateTime dataProcessamentoHistologico;
    private LocalDateTime dataRecebimentoLamina;
    private LocalDateTime dataLiberacaoLaudo;

    private String macroscopia;
    private String microscopia;
    private String diagnosticoSugerido;
    private List<FrascoDTO> frascos;

    public ExameResponseDTO() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroExame() { return numeroExame; }
    public void setNumeroExame(String numeroExame) { this.numeroExame = numeroExame; }

    public TipoExame getTipoExame() { return tipoExame; }
    public void setTipoExame(TipoExame tipoExame) { this.tipoExame = tipoExame; }

    public String getPacienteProntuario() { return pacienteProntuario; }
    public void setPacienteProntuario(String pacienteProntuario) { this.pacienteProntuario = pacienteProntuario; }

    public String getCidSugerido() { return cidSugerido; }
    public void setCidSugerido(String cidSugerido) { this.cidSugerido = cidSugerido; }

    public LocalDateTime getDataColetaProcedimento() { return dataColetaProcedimento; }
    public void setDataColetaProcedimento(LocalDateTime dataColetaProcedimento) { this.dataColetaProcedimento = dataColetaProcedimento; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public LocalDateTime getDataRecebimento() { return dataRecebimento; }
    public void setDataRecebimento(LocalDateTime dataRecebimento) { this.dataRecebimento = dataRecebimento; }

    public LocalDateTime getDataMacroscopia() { return dataMacroscopia; }
    public void setDataMacroscopia(LocalDateTime dataMacroscopia) { this.dataMacroscopia = dataMacroscopia; }

    public LocalDateTime getDataProcessamentoHistologico() { return dataProcessamentoHistologico; }
    public void setDataProcessamentoHistologico(LocalDateTime dataProcessamentoHistologico) { this.dataProcessamentoHistologico = dataProcessamentoHistologico; }

    public LocalDateTime getDataRecebimentoLamina() { return dataRecebimentoLamina; }
    public void setDataRecebimentoLamina(LocalDateTime dataRecebimentoLamina) { this.dataRecebimentoLamina = dataRecebimentoLamina; }

    public LocalDateTime getDataLiberacaoLaudo() { return dataLiberacaoLaudo; }
    public void setDataLiberacaoLaudo(LocalDateTime dataLiberacaoLaudo) { this.dataLiberacaoLaudo = dataLiberacaoLaudo; }

    public String getMacroscopia() { return macroscopia; }
    public void setMacroscopia(String macroscopia) { this.macroscopia = macroscopia; }

    public String getMicroscopia() { return microscopia; }
    public void setMicroscopia(String microscopia) { this.microscopia = microscopia; }

    public String getDiagnosticoSugerido() { return diagnosticoSugerido; }
    public void setDiagnosticoSugerido(String diagnosticoSugerido) { this.diagnosticoSugerido = diagnosticoSugerido; }

    public List<FrascoDTO> getFrascos() { return frascos; }
    public void setFrascos(List<FrascoDTO> frascos) { this.frascos = frascos; }
}
