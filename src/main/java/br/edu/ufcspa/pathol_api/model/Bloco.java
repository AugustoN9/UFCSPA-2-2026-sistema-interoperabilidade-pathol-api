package br.edu.ufcspa.pathol_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_bloco")
public class Bloco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoBloco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frasco_id")
    @JsonBackReference
    private FrascoAmostra frasco;

    @OneToMany(mappedBy = "bloco", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Lamina> laminas = new ArrayList<>();

    public Bloco() {
    }

    public Bloco(String codigoBloco) {
        this.codigoBloco = codigoBloco;
    }

    public void adicionarLamina(Lamina lamina) {
        laminas.add(lamina);
        lamina.setBloco(this);
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoBloco() {
        return codigoBloco;
    }

    public void setCodigoBloco(String codigoBloco) {
        this.codigoBloco = codigoBloco;
    }

    public FrascoAmostra getFrasco() {
        return frasco;
    }

    public void setFrasco(FrascoAmostra frasco) {
        this.frasco = frasco;
    }

    public List<Lamina> getLaminas() {
        return laminas;
    }

    public void setLaminas(List<Lamina> laminas) {
        this.laminas = laminas;
    }
}