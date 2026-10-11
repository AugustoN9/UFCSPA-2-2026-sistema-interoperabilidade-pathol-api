package br.edu.ufcspa.pathol_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ufcspa.pathol_api.dto.PacienteRequestDTO;
import br.edu.ufcspa.pathol_api.dto.PacienteResponseDTO;
import br.edu.ufcspa.pathol_api.model.Paciente;
import br.edu.ufcspa.pathol_api.repository.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public PacienteResponseDTO cadastrar(PacienteRequestDTO dto) {
        if (pacienteRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new RuntimeException("Já existe um paciente cadastrado com este CPF.");
        }
        
        Paciente paciente = new Paciente(dto.nome(), dto.cpf(), dto.dataNascimento(), dto.sexoBiologico());
        Paciente salvo = pacienteRepository.save(paciente);
        return toDTO(salvo);
    }

    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll().stream().map(this::toDTO).toList();
    }

    public PacienteResponseDTO buscarPorCpf(String cpf) {
        Paciente paciente = pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado para o CPF: " + cpf));
        return toDTO(paciente);
    }

    private PacienteResponseDTO toDTO(Paciente paciente) {
        return new PacienteResponseDTO(
            paciente.getId(),
            paciente.getNome(),
            paciente.getCpf(),
            paciente.getDataNascimento(),
            paciente.getSexoBiologico()
        );
    }
}