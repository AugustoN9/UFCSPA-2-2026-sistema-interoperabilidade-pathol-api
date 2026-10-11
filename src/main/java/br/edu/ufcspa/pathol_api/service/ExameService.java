package br.edu.ufcspa.pathol_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufcspa.pathol_api.dto.BlocoDTO;
import br.edu.ufcspa.pathol_api.dto.ExameRequestDTO;
import br.edu.ufcspa.pathol_api.dto.ExameResponseDTO;
import br.edu.ufcspa.pathol_api.dto.FrascoDTO;
import br.edu.ufcspa.pathol_api.dto.LaminaDTO;
import br.edu.ufcspa.pathol_api.model.Bloco;
import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.model.FrascoAmostra;
import br.edu.ufcspa.pathol_api.model.Lamina;
import br.edu.ufcspa.pathol_api.model.Prontuario;
import br.edu.ufcspa.pathol_api.repository.ExameRepository;
import br.edu.ufcspa.pathol_api.repository.ProntuarioRepository;

@Service
public class ExameService {

    private final ExameRepository exameRepository;
    private final ProntuarioRepository prontuarioRepository;

    public ExameService(ExameRepository exameRepository, ProntuarioRepository prontuarioRepository) {
        this.exameRepository = exameRepository;
        this.prontuarioRepository = prontuarioRepository;
    }

    // MÉTODO AUXILIAR: Mapeia Entidade Exame para ExameResponseDTO (Elimina recursão JSON)
    public ExameResponseDTO converterParaDTO(Exame exame) {
        ExameResponseDTO dto = new ExameResponseDTO();
        dto.setId(exame.getId());
        dto.setNumeroExame(exame.getNumeroExame());
        dto.setTipoExame(exame.getTipoExame());
        dto.setPacienteProntuario(exame.getPacienteProntuario());
        dto.setCidSugerido(exame.getCidSugerido());

        // Mapeamento dos 7 Marcos Temporais
        dto.setDataColetaProcedimento(exame.getDataColetaProcedimento());
        dto.setDataSolicitacao(exame.getDataSolicitacao());
        dto.setDataRecebimento(exame.getDataRecebimento());
        dto.setDataMacroscopia(exame.getDataMacroscopia());
        dto.setDataProcessamentoHistologico(exame.getDataProcessamentoHistologico());
        dto.setDataRecebimentoLamina(exame.getDataRecebimentoLamina());
        dto.setDataLiberacaoLaudo(exame.getDataLiberacaoLaudo());

        // Laudo
        dto.setMacroscopia(exame.getMacroscopia());
        dto.setMicroscopia(exame.getMicroscopia());
        dto.setDiagnosticoSugerido(exame.getDiagnosticoSugerido());

        // Mapeamento Completo de Amostragem (Frascos -> Blocos -> Lâminas)
        if (exame.getFrascos() != null) {
            List<FrascoDTO> frascosDTO = exame.getFrascos().stream().map(frasco -> {
                List<BlocoDTO> blocosDTO = null;
                if (frasco.getBlocos() != null) {
                    blocosDTO = frasco.getBlocos().stream().map(bloco -> {
                        List<LaminaDTO> laminasDTO = null;
                        if (bloco.getLaminas() != null) {
                            laminasDTO = bloco.getLaminas().stream()
                                    .map(lamina -> new LaminaDTO(lamina.getCodigoLamina(), lamina.getColoracao()))
                                    .collect(Collectors.toList());
                        }
                        return new BlocoDTO(bloco.getCodigoBloco(), laminasDTO);
                    }).collect(Collectors.toList());
                }
                return new FrascoDTO(
                    frasco.getPosicao(),
                    frasco.getTopografia(),
                    frasco.getQuantidadeFragmentos(),
                    blocosDTO
                );
            }).collect(Collectors.toList());

            dto.setFrascos(frascosDTO);
        }

        return dto;
    }

    // 1. SALVAR (Criar Exame a partir do DTO)
    @Transactional
    public ExameResponseDTO salvar(ExameRequestDTO dto) {
        validarRegrasClinicas(dto);
        Exame exame = new Exame();
        mapDtoParaEntidade(dto, exame);
        Exame salvo = exameRepository.save(exame);
        return converterParaDTO(salvo);
    }

    // 2. LISTAR TODOS
    public List<ExameResponseDTO> listarTodos() {
        return exameRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // 3. BUSCAR ENTIDADE POR ID (Uso Interno)
    public Exame buscarEntidadePorId(Long id) {
        return exameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exame não encontrado com o ID: " + id));
    }

    // BUSCAR DTO POR ID
    public ExameResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarEntidadePorId(id));
    }

    // 4. BUSCAR POR NÚMERO DO EXAME
    public ExameResponseDTO buscarPorNumeroExame(String numeroExame) {
        Exame exame = exameRepository.findByNumeroExame(numeroExame)
                .orElseThrow(() -> new RuntimeException("Exame não encontrado com o número: " + numeroExame));
        return converterParaDTO(exame);
    }

    // 5. BUSCAR POR PRONTUÁRIO DO PACIENTE
    public List<ExameResponseDTO> buscarPorPacienteProntuario(String pacienteProntuario) {
        List<Exame> exames = exameRepository.findByPacienteProntuario(pacienteProntuario);
        if (exames.isEmpty()) {
            throw new RuntimeException("Nenhum exame encontrado para o prontuário: " + pacienteProntuario);
        }
        return exames.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // 6. ATUALIZAR EXAME EXISTENTE
    @Transactional
    public ExameResponseDTO atualizar(Long id, ExameRequestDTO dto) {
        validarRegrasClinicas(dto);
        Exame exameExistente = buscarEntidadePorId(id);
        exameExistente.getFrascos().clear();
        mapDtoParaEntidade(dto, exameExistente);
        Exame atualizado = exameRepository.save(exameExistente);
        return converterParaDTO(atualizado);
    }

    // 7. DELETAR
    @Transactional
    public void deletar(Long id) {
        Exame exame = buscarEntidadePorId(id);
        exameRepository.delete(exame);
    }

    // BUSCAR LISTA POR CID SUGERIDO
    public List<ExameResponseDTO> buscarPorCidSugerido(String cidSugerido) {
        List<Exame> exames = exameRepository.findByCidSugerido(cidSugerido);
        if (exames.isEmpty()) {
            throw new RuntimeException("Nenhum exame encontrado para o CID sugerido: " + cidSugerido);
        }
        return exames.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // CONTAR TOTAL DE EXAMES POR CID SUGERIDO
    public long contarPorCidSugerido(String cidSugerido) {
        return exameRepository.countByCidSugerido(cidSugerido);
    }

    // MÉTODO AUXILIAR: Validação das Regras Clínicas Histopatológicas
    private void validarRegrasClinicas(ExameRequestDTO dto) {
        if (dto.frascos() == null || dto.frascos().isEmpty()) {
            throw new IllegalArgumentException("O exame deve conter ao menos um frasco de amostra.");
        }

        for (FrascoDTO frasco : dto.frascos()) {
            if (frasco.blocos() != null) {
                for (BlocoDTO bloco : frasco.blocos()) {
                    List<LaminaDTO> laminas = bloco.laminas();

                    if (laminas == null || laminas.isEmpty()) {
                        throw new IllegalArgumentException("Regra Clínica Violada: O bloco " 
                            + bloco.codigoBloco() + " deve conter ao menos uma lâmina.");
                    }

                    LaminaDTO primeiraLamina = laminas.get(0);
                    if (primeiraLamina.coloracao() == null || !"HE".equalsIgnoreCase(primeiraLamina.coloracao().trim())) {
                        throw new IllegalArgumentException("Regra Clínica Violada: A primeira lâmina do bloco " 
                            + bloco.codigoBloco() + " deve possuir obrigatoriamente a coloração 'HE'.");
                    }
                }
            }
        }
    }

    // MÉTODO AUXILIAR: Converte o DTO para a Entidade JPA
    private void mapDtoParaEntidade(ExameRequestDTO dto, Exame exame) {
        exame.setNumeroExame(dto.numeroExame());
        exame.setTipoExame(dto.tipoExame());
        exame.setPacienteProntuario(dto.pacienteProntuario());
        exame.setCidSugerido(dto.cidSugerido());

        if (dto.pacienteProntuario() != null) {
            Optional<Prontuario> prontuarioOpt = prontuarioRepository.findByNumeroProntuario(dto.pacienteProntuario());
            prontuarioOpt.ifPresent(exame::setProntuario);
        }

        exame.setDataColetaProcedimento(dto.dataColetaProcedimento());
        exame.setDataSolicitacao(dto.dataSolicitacao());
        exame.setDataRecebimento(dto.dataRecebimento());
        exame.setDataMacroscopia(dto.dataMacroscopia());
        exame.setDataProcessamentoHistologico(dto.dataProcessamentoHistologico());
        exame.setDataRecebimentoLamina(dto.dataRecebimentoLamina());
        exame.setDataLiberacaoLaudo(dto.dataLiberacaoLaudo());

        exame.setMacroscopia(dto.macroscopia());
        exame.setMicroscopia(dto.microscopia());
        exame.setDiagnosticoSugerido(dto.diagnosticoSugerido());

        if (dto.frascos() != null) {
            for (FrascoDTO frascoDTO : dto.frascos()) {
                FrascoAmostra frasco = new FrascoAmostra();
                frasco.setPosicao(frascoDTO.posicao());
                frasco.setTopografia(frascoDTO.topografia());
                frasco.setQuantidadeFragmentos(frascoDTO.quantidadeFragmentos());

                if (frascoDTO.blocos() != null) {
                    for (BlocoDTO blocoDTO : frascoDTO.blocos()) {
                        Bloco bloco = new Bloco(blocoDTO.codigoBloco());

                        if (blocoDTO.laminas() != null) {
                            for (LaminaDTO laminaDTO : blocoDTO.laminas()) {
                                Lamina lamina = new Lamina(
                                    laminaDTO.codigoLamina(),
                                    laminaDTO.coloracao()
                                );
                                bloco.adicionarLamina(lamina);
                            }
                        }
                        frasco.adicionarBloco(bloco);
                    }
                }
                exame.adicionarFrasco(frasco);
            }
        }
    }
}