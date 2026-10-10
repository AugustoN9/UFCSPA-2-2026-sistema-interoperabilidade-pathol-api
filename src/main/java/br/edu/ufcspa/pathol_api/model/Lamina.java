package br.edu.ufcspa.pathol_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_lamina")
public class Lamina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoLamina;
    private String coloracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloco_id")
    @JsonBackReference
    private Bloco bloco;

    public Lamina() {
    }

    public Lamina(String codigoLamina, String coloracao) {
        this.codigoLamina = codigoLamina;
        this.coloracao = coloracao;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoLamina() {
        return codigoLamina;
    }

    public void setCodigoLamina(String codigoLamina) {
        this.codigoLamina = codigoLamina;
    }

    public String getColoracao() {
        return coloracao;
    }

    public void setColoracao(String coloracao) {
        this.coloracao = coloracao;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }
}