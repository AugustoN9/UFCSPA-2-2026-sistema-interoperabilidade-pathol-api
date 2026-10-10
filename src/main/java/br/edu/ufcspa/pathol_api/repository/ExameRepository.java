package br.edu.ufcspa.pathol_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.model.TipoExame;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {

    // Busca rápida pelo código de identificação do laboratório (ex: AP-2026-08912)
    Optional<Exame> findByNumeroExame(String numeroExame);

    // Filtrar exames por tipo (Anatomopatológico, Citopatológico, etc.)
    List<Exame> findByTipoExame(TipoExame tipoExame);

    // Método para os indicadores epidemiológicos: busca laudos com H. pylori positivo/negativo
    List<Exame> findByPesquisaHelicobacterPylori(Boolean pesquisaHelicobacterPylori);

    // Contagem direta para os gráficos de prevalência do dashboard epidemiológico
    long countByPesquisaHelicobacterPyloriTrue();
}
