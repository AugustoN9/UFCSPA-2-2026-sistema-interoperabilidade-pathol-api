package br.edu.ufcspa.pathol_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_exame")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroExame;

    @Enumerated(EnumType.STRING)
    private TipoExame tipoExame;

    private LocalDateTime dataRecebimento = LocalDateTime.now();

    private String pacienteProntuario;

    @Column(length = 20)
    private String cidSugerido;

    @Column(columnDefinition = "TEXT")
    private String macroscopia;

    @Column(columnDefinition = "TEXT")
    private String microscopia;

    @Column(columnDefinition = "TEXT")
    private String diagnosticoSugerido;

    @OneToMany(mappedBy = "exame", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FrascoAmostra> frascos = new ArrayList<>();

    public Exame() {
    }

    public Exame(String numeroExame, TipoExame tipoExame, String pacienteProntuario,
                 String cidSugerido, String macroscopia, String microscopia, 
                 String diagnosticoSugerido) {
        this.numeroExame = numeroExame;
        this.tipoExame = tipoExame;
        this.pacienteProntuario = pacienteProntuario;
        this.cidSugerido = cidSugerido;
        this.macroscopia = macroscopia;
        this.microscopia = microscopia;
        this.diagnosticoSugerido = diagnosticoSugerido;
    }

    public void adicionarFrasco(FrascoAmostra frasco) {
        frascos.add(frasco);
        frasco.setExame(this);
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroExame() {
        return numeroExame;
    }

    public void setNumeroExame(String numeroExame) {
        this.numeroExame = numeroExame;
    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(TipoExame tipoExame) {
        this.tipoExame = tipoExame;
    }

    public LocalDateTime getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDateTime dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getPacienteProntuario() {
        return pacienteProntuario;
    }

    public void setPacienteProntuario(String pacienteProntuario) {
        this.pacienteProntuario = pacienteProntuario;
    }

    public String getCidSugerido() {
        return cidSugerido;
    }

    public void setCidSugerido(String cidSugerido) {
        this.cidSugerido = cidSugerido;
    }

    public String getMacroscopia() {
        return macroscopia;
    }

    public void setMacroscopia(String macroscopia) {
        this.macroscopia = macroscopia;
    }

    public String getMicroscopia() {
        return microscopia;
    }

    public void setMicroscopia(String microscopia) {
        this.microscopia = microscopia;
    }

    public String getDiagnosticoSugerido() {
        return diagnosticoSugerido;
    }

    public void setDiagnosticoSugerido(String diagnosticoSugerido) {
        this.diagnosticoSugerido = diagnosticoSugerido;
    }

    public List<FrascoAmostra> getFrascos() {
        return frascos;
    }

    public void setFrascos(List<FrascoAmostra> frascos) {
        this.frascos = frascos;
    }
}