package br.edu.ufcspa.pathol_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufcspa.pathol_api.dto.BlocoDTO;
import br.edu.ufcspa.pathol_api.dto.ExameRequestDTO;
import br.edu.ufcspa.pathol_api.dto.FrascoDTO;
import br.edu.ufcspa.pathol_api.dto.LaminaDTO;
import br.edu.ufcspa.pathol_api.model.Bloco;
import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.model.FrascoAmostra;
import br.edu.ufcspa.pathol_api.model.Lamina;
import br.edu.ufcspa.pathol_api.repository.ExameRepository;

@Service
public class ExameService {

    private final ExameRepository exameRepository;

    public ExameService(ExameRepository exameRepository) {
        this.exameRepository = exameRepository;
    }

    // 1. SALVAR (Criar Exame a partir do DTO)
    @Transactional
    public Exame salvar(ExameRequestDTO dto) {
        validarRegrasClinicas(dto);
        Exame exame = new Exame();
        mapDtoParaEntidade(dto, exame);
        return exameRepository.save(exame);
    }

    // 2. LISTAR TODOS
    public List<Exame> listarTodos() {
        return exameRepository.findAll();
    }

    // 3. BUSCAR POR ID
    public Exame buscarPorId(Long id) {
        return exameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exame não encontrado com o ID: " + id));
    }

    // 4. ATUALIZAR EXAME EXISTENTE
    @Transactional
    public Exame atualizar(Long id, ExameRequestDTO dto) {
        validarRegrasClinicas(dto);
        Exame exameExistente = buscarPorId(id);
        exameExistente.getFrascos().clear(); // Limpa as amostras antigas para reescrever
        mapDtoParaEntidade(dto, exameExistente);
        return exameRepository.save(exameExistente);
    }

    // 5. DELETAR
    @Transactional
    public void deletar(Long id) {
        Exame exame = buscarPorId(id);
        exameRepository.delete(exame);
    }

    // MÉTODO AUXILIAR: Validação das Regras Clínicas Histopatológicas
    private void validarRegrasClinicas(ExameRequestDTO dto) {
        if (dto.frascos() == null || dto.frascos().isEmpty()) {
            throw new IllegalArgumentException("O exame deve conter ao menos um frasco de amostra.");
        }

        boolean possuiColoracaoGiemsa = false;

        for (FrascoDTO frasco : dto.frascos()) {
            if (frasco.blocos() != null) {
                for (BlocoDTO bloco : frasco.blocos()) {
                    List<LaminaDTO> laminas = bloco.laminas();

                    if (laminas == null || laminas.isEmpty()) {
                        throw new IllegalArgumentException("Regra Clínica Violada: O bloco " 
                            + bloco.codigoBloco() + " deve conter ao menos uma lâmina.");
                    }

                    // 1. A primeira lâmina do bloco deve possuir obrigatoriamente a coloração HE
                    LaminaDTO primeiraLamina = laminas.get(0);
                    if (primeiraLamina.coloracao() == null || !"HE".equalsIgnoreCase(primeiraLamina.coloracao().trim())) {
                        throw new IllegalArgumentException("Regra Clínica Violada: A primeira lâmina do bloco " 
                            + bloco.codigoBloco() + " deve possuir obrigatoriamente a coloração 'HE'.");
                    }

                    // Verifica se há alguma lâmina com Giemsa na estrutura
                    for (LaminaDTO lamina : laminas) {
                        if (lamina.coloracao() != null && "Giemsa".equalsIgnoreCase(lamina.coloracao().trim())) {
                            possuiColoracaoGiemsa = true;
                        }
                    }
                }
            }
        }

        // 2. Se a pesquisa de Helicobacter pylori está marcada, deve haver coloração Giemsa
        if (Boolean.TRUE.equals(dto.pesquisaHelicobacterPylori()) && !possuiColoracaoGiemsa) {
            throw new IllegalArgumentException("Regra Clínica Violada: Quando a pesquisa de Helicobacter pylori está ativada, é obrigatório incluir ao menos uma lâmina com coloração 'Giemsa'.");
        }
    }

    // MÉTODO AUXILIAR: Converte a árvore de DTOs para o Grafo de Entidades JPA
    private void mapDtoParaEntidade(ExameRequestDTO dto, Exame exame) {
        exame.setNumeroExame(dto.numeroExame());
        exame.setTipoExame(dto.tipoExame());
        exame.setPacienteProntuario(dto.pacienteProntuario());
        exame.setMacroscopia(dto.macroscopia()); // Atribuído do DTO para a entidade
        exame.setMicroscopia(dto.microscopia());
        exame.setDiagnosticoSugerido(dto.diagnosticoSugerido());
        exame.setPesquisaHelicobacterPylori(dto.pesquisaHelicobacterPylori());

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