package br.edu.ufcspa.pathol_api.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufcspa.pathol_api.model.Bloco;
import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.model.FrascoAmostra;
import br.edu.ufcspa.pathol_api.model.Lamina;
import br.edu.ufcspa.pathol_api.model.Paciente;
import br.edu.ufcspa.pathol_api.model.Prontuario;
import br.edu.ufcspa.pathol_api.model.SexoBiologico;
import br.edu.ufcspa.pathol_api.model.TipoExame;
import br.edu.ufcspa.pathol_api.repository.ExameRepository;
import br.edu.ufcspa.pathol_api.repository.PacienteRepository;
import br.edu.ufcspa.pathol_api.repository.ProntuarioRepository;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final PacienteRepository pacienteRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final ExameRepository exameRepository;

    public DataLoader(PacienteRepository pacienteRepository,
                      ProntuarioRepository prontuarioRepository,
                      ExameRepository exameRepository) {
        this.pacienteRepository = pacienteRepository;
        this.prontuarioRepository = prontuarioRepository;
        this.exameRepository = exameRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Se a base já tiver sido populada no PostgreSQL, não executa o DataLoader novamente
        if (pacienteRepository.count() > 0) {
            return;
        }

        // ------------------------------------------------------------------
        // 1. CASO DERMATOLÓGICO: JOÃO DA SILVA (MELANOMA / PUNCH DE PELE)
        // ------------------------------------------------------------------
        Paciente joao = pacienteRepository.save(
            new Paciente("João da Silva", "123.456.789-00", LocalDate.of(1988, 5, 15), SexoBiologico.MASCULINO)
        );

        Prontuario prontuarioJoao = prontuarioRepository.save(
            new Prontuario("DERM-2026-8841", "Lesão enegrecida em dorso com bordas irregulares. Suspeita de Melanoma.", joao)
        );

        Exame exameJoao = new Exame();
        exameJoao.setNumeroExame("EX-2024-9901");
        exameJoao.setTipoExame(TipoExame.ANATOMOPATOLOGICO);
        exameJoao.setPacienteProntuario(prontuarioJoao.getNumeroProntuario());
        exameJoao.setProntuario(prontuarioJoao);
        exameJoao.setCidSugerido("C43.5");

        LocalDateTime coletaJoao = LocalDateTime.of(2024, 6, 10, 9, 30);
        configurarSeteMarcos(exameJoao, coletaJoao, 4);

        exameJoao.setMacroscopia("Punch de pele medindo 0,6 cm com lesão enegrecida central de 0,3 cm.");
        exameJoao.setMicroscopia("Proliferação atípica de melanócitos com invasão da derme reticular.");
        exameJoao.setDiagnosticoSugerido("Melanoma Extensivo Superficial, Breslow 0,8 mm. Margens livres.");

        FrascoAmostra frascoJoao = new FrascoAmostra(1, "Pele - Dorso", 1);
        Bloco blocoJoao = new Bloco("B1");
        blocoJoao.adicionarLamina(new Lamina("L1", "HE"));
        frascoJoao.adicionarBloco(blocoJoao);
        exameJoao.adicionarFrasco(frascoJoao);

        exameRepository.save(exameJoao);

        // ------------------------------------------------------------------
        // 2. COORTE PEDIÁTRICA: GASTRITE E H. PYLORI (PED-1001 A PED-1010)
        // ------------------------------------------------------------------
        String[] cidsPediatricos = {"K29.5", "K29.3", "K29.5", "K29.5", "K29.1", "K29.5", "K29.3", "K29.5", "K29.5", "K29.3"};
        boolean[] hPyloriPositivo = {true, false, true, true, false, true, false, true, true, false};

        for (int i = 1; i <= 10; i++) {
            String numPed = String.format("PED-%04d", 1000 + i);
            String numExame = String.format("EX-2026-%04d", 1000 + i);

            LocalDate nascimento = LocalDate.of(2010 + (i % 7), (i % 12) + 1, (i * 2) % 28 + 1);
            SexoBiologico sexo = (i % 2 == 0) ? SexoBiologico.FEMININO : SexoBiologico.MASCULINO;

            Paciente pacPed = pacienteRepository.save(
                new Paciente("Paciente Pediátrico " + i, String.format("%03d.%03d.%03d-%02d", i*11, i*22, i*33, i), nascimento, sexo)
            );

            Prontuario prontPed = prontuarioRepository.save(
                new Prontuario(numPed, "Investigação de dor epigástrica, dispepsia e náuseas em acompanhamento pediátrico.", pacPed)
            );

            Exame examePed = new Exame();
            examePed.setNumeroExame(numExame);
            examePed.setTipoExame(TipoExame.ANATOMOPATOLOGICO);
            examePed.setPacienteProntuario(prontPed.getNumeroProntuario());
            examePed.setProntuario(prontPed);
            examePed.setCidSugerido(cidsPediatricos[i - 1]);

            int anoColeta = 2023 + (i % 3);
            LocalDateTime coletaPed = LocalDateTime.of(anoColeta, (i % 12) + 1, (i * 2) % 28 + 1, 8, 0);
            configurarSeteMarcos(examePed, coletaPed, 3);

            examePed.setMacroscopia("Frasco 1 (Antro): Fragmentos de mucosa gástrica pardo-claros.");
            
            if (hPyloriPositivo[i - 1]) {
                examePed.setMicroscopia("Mucosa antral com moderado infiltrado inflamatório e incontáveis bacilos espiralados na lâmina Giemsa.");
                examePed.setDiagnosticoSugerido("Gastrite crônica moderada ativa associada a Helicobacter pylori.");
            } else {
                examePed.setMicroscopia("Mucosa antral com infiltrado mononuclear discreto em lâmina própria. Ausência de bacilos.");
                examePed.setDiagnosticoSugerido("Gastrite crônica leve inativa. Pesquisa de H. pylori negativa.");
            }

            FrascoAmostra frascoPed = new FrascoAmostra(1, "Estômago - Antro", 2);
            Bloco blocoPed = new Bloco("B1");
            blocoPed.adicionarLamina(new Lamina("L1", "HE"));
            if (hPyloriPositivo[i - 1]) {
                blocoPed.adicionarLamina(new Lamina("L2", "Giemsa"));
            }
            frascoPed.adicionarBloco(blocoPed);
            examePed.adicionarFrasco(frascoPed);

            exameRepository.save(examePed);
        }
    }

    private void configurarSeteMarcos(Exame exame, LocalDateTime dataColeta, int diasProcessamento) {
        exame.setDataColetaProcedimento(dataColeta);
        exame.setDataSolicitacao(dataColeta.plusHours(2));
        exame.setDataRecebimento(dataColeta.plusDays(1));
        exame.setDataMacroscopia(dataColeta.plusDays(1).plusHours(5));
        exame.setDataProcessamentoHistologico(dataColeta.plusDays(2));
        exame.setDataRecebimentoLamina(dataColeta.plusDays(2).plusHours(6));
        exame.setDataLiberacaoLaudo(dataColeta.plusDays(diasProcessamento));
    }
}