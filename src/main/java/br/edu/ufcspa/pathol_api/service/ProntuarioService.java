package br.edu.ufcspa.pathol_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ufcspa.pathol_api.dto.PacienteResponseDTO;
import br.edu.ufcspa.pathol_api.dto.ProntuarioRequestDTO;
import br.edu.ufcspa.pathol_api.dto.ProntuarioResponseDTO;
import br.edu.ufcspa.pathol_api.model.Paciente;
import br.edu.ufcspa.pathol_api.model.Prontuario;
import br.edu.ufcspa.pathol_api.repository.PacienteRepository;
import br.edu.ufcspa.pathol_api.repository.ProntuarioRepository;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final PacienteRepository pacienteRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository, PacienteRepository pacienteRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public ProntuarioResponseDTO criar(ProntuarioRequestDTO dto) {
        if (prontuarioRepository.findByNumeroProntuario(dto.numeroProntuario()).isPresent()) {
            throw new RuntimeException("Já existe um prontuário cadastrado com este número: " + dto.numeroProntuario());
        }

        Paciente paciente = pacienteRepository.findByCpf(dto.cpfPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado para o CPF: " + dto.cpfPaciente()));

        Prontuario prontuario = new Prontuario(dto.numeroProntuario(), dto.historico(), paciente);
        Prontuario salvo = prontuarioRepository.save(prontuario);
        return toDTO(salvo);
    }

    public List<ProntuarioResponseDTO> listarTodos() {
        return prontuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    public ProntuarioResponseDTO buscarPorNumero(String numeroProntuario) {
        Prontuario prontuario = prontuarioRepository.findByNumeroProntuario(numeroProntuario)
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado: " + numeroProntuario));
        return toDTO(prontuario);
    }

    public List<ProntuarioResponseDTO> buscarPorCpfPaciente(String cpf) {
        return prontuarioRepository.findByPacienteCpf(cpf).stream().map(this::toDTO).toList();
    }

    private ProntuarioResponseDTO toDTO(Prontuario prontuario) {
        Paciente p = prontuario.getPaciente();
        PacienteResponseDTO pacienteDTO = new PacienteResponseDTO(
            p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento(), p.getSexoBiologico()
        );
        return new ProntuarioResponseDTO(
            prontuario.getId(),
            prontuario.getNumeroProntuario(),
            prontuario.getHistorico(),
            pacienteDTO
        );
    }
}