package br.edu.ufcspa.pathol_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufcspa.pathol_api.model.Prontuario;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    Optional<Prontuario> findByNumeroProntuario(String numeroProntuario);
    List<Prontuario> findByPacienteCpf(String cpf);
}