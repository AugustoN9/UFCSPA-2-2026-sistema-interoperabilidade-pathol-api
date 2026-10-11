package br.edu.ufcspa.pathol_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_exames")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroExame; // Ex: EX-2026-1001

    @Enumerated(EnumType.STRING)
    private TipoExame tipoExame;

    private String pacienteProntuario; // Chave/Código textual de busca rápida (ex: PED-1001)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario; // Vínculo com a entidade Prontuário

    private String cidSugerido;

    // LINHA DO TEMPO (7 MARCOS TEMPORAIS)
    private LocalDateTime dataColetaProcedimento;    // 1. Coleta / Procedimento Cirúrgico
    private LocalDateTime dataSolicitacao;             // 2. Solicitação médica
    private LocalDateTime dataRecebimento;           // 3. Recebimento no laboratório
    private LocalDateTime dataMacroscopia;            // 4. Clivagem / Macroscopia
    private LocalDateTime dataProcessamentoHistologico; // 5. Processamento histológico
    private LocalDateTime dataRecebimentoLamina;      // 6. Recebimento da Lâmina corada
    private LocalDateTime dataLiberacaoLaudo;         // 7. Microscopia / Liberação do laudo

    @Column(columnDefinition = "TEXT")
    private String macroscopia;

    @Column(columnDefinition = "TEXT")
    private String microscopia;

    @Column(columnDefinition = "TEXT")
    private String diagnosticoSugerido;

    @OneToMany(mappedBy = "exame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Frasco> frascos = new ArrayList<>();

    public Exame() {}

    // Getters e Setters para todos os campos (incluindo as 7 datas e o prontuario)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroExame() { return numeroExame; }
    public void setNumeroExame(String numeroExame) { this.numeroExame = numeroExame; }

    public TipoExame getTipoExame() { return tipoExame; }
    public void setTipoExame(TipoExame tipoExame) { this.tipoExame = tipoExame; }

    public String getPacienteProntuario() { return pacienteProntuario; }
    public void setPacienteProntuario(String pacienteProntuario) { this.pacienteProntuario = pacienteProntuario; }

    public Prontuario getProntuario() { return prontuario; }
    public void setProntuario(Prontuario prontuario) { this.prontuario = prontuario; }

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

    public List<Frasco> getFrascos() { return frascos; }
    public void setFrascos(List<Frasco> frascos) { this.frascos = frascos; }
}