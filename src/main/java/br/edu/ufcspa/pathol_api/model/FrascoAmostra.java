package br.edu.ufcspa.pathol_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_frasco_amostra")
public class FrascoAmostra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer posicao;
    private String topografia;
    private Integer quantidadeFragmentos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exame_id")
    @JsonBackReference
    private Exame exame;

    @OneToMany(mappedBy = "frasco", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Bloco> blocos = new ArrayList<>();

    public FrascoAmostra() {
    }

    public FrascoAmostra(Integer posicao, String topografia, Integer quantidadeFragmentos) {
        this.posicao = posicao;
        this.topografia = topografia;
        this.quantidadeFragmentos = quantidadeFragmentos;
    }

    public void adicionarBloco(Bloco bloco) {
        blocos.add(bloco);
        bloco.setFrasco(this);
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public String getTopografia() {
        return topografia;
    }

    public void setTopografia(String topografia) {
        this.topografia = topografia;
    }

    public Integer getQuantidadeFragmentos() {
        return quantidadeFragmentos;
    }

    public void setQuantidadeFragmentos(Integer quantidadeFragmentos) {
        this.quantidadeFragmentos = quantidadeFragmentos;
    }

    public Exame getExame() {
        return exame;
    }

    public void setExame(Exame exame) {
        this.exame = exame;
    }

    public List<Bloco> getBlocos() {
        return blocos;
    }

    public void setBlocos(List<Bloco> blocos) {
        this.blocos = blocos;
    }
}