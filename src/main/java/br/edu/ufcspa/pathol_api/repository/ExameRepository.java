package br.edu.ufcspa.pathol_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.model.TipoExame;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {

    // Busca rápida pelo código de identificação do exame (ex: EX-2026-1001)
    Optional<Exame> findByNumeroExame(String numeroExame);

    // Busca o histórico de exames vinculados a um prontuário (ex: PED-1001)
    List<Exame> findByPacienteProntuario(String pacienteProntuario);

    // Filtrar exames por tipo (ANATOMOPATOLOGICO, CITOPATOLOGICO, etc.)
    List<Exame> findByTipoExame(TipoExame tipoExame);

    // Busca exames por CID sugerido no laudo (ex: K29.5)
    List<Exame> findByCidSugerido(String cidSugerido);

    // Contagem direta para métricas e estatísticas do dashboard
    long countByCidSugerido(String cidSugerido);
}