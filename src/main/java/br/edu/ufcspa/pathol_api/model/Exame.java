package br.edu.ufcspa.pathol_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @Column(nullable = false, unique = true)
    private String numeroExame;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoExame tipoExame;

    private String pacienteProntuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id")
    @JsonIgnoreProperties("exames")
    private Prontuario prontuario;

    private String cidSugerido;

    // Os 7 Marcos Temporais do Ciclo de Vida da Biópsia
    private LocalDateTime dataColetaProcedimento;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataRecebimento;
    private LocalDateTime dataMacroscopia;
    private LocalDateTime dataProcessamentoHistologico;
    private LocalDateTime dataRecebimentoLamina;
    private LocalDateTime dataLiberacaoLaudo;

    @Column(columnDefinition = "TEXT")
    private String macroscopia;

    @Column(columnDefinition = "TEXT")
    private String microscopia;

    @Column(columnDefinition = "TEXT")
    private String diagnosticoSugerido;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exame_id")
    private List<FrascoAmostra> frascos = new ArrayList<>();

    public Exame() {}

    // Método auxiliar chamado pelo DataLoader e ExameService
    public void adicionarFrasco(FrascoAmostra frasco) {
        if (this.frascos == null) {
            this.frascos = new ArrayList<>();
        }
        this.frascos.add(frasco);
    }

    // Getters e Setters
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

    public List<FrascoAmostra> getFrascos() { return frascos; }
    public void setFrascos(List<FrascoAmostra> frascos) { this.frascos = frascos; }
}