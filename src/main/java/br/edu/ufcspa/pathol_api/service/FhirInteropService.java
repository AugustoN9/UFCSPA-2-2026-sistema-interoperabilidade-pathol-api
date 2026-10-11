package br.edu.ufcspa.pathol_api.service;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Specimen;
import org.springframework.stereotype.Service;

import br.edu.ufcspa.pathol_api.model.Exame;
import br.edu.ufcspa.pathol_api.model.FrascoAmostra;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class FhirInteropService {

    private final FhirContext fhirContext;

    public FhirInteropService() {
        // Inicialização do contexto FHIR R4
        this.fhirContext = FhirContext.forR4();
    }

    /**
     * Mapeia a entidade Exame do banco de dados para um Bundle FHIR R4 em JSON.
     */
    public String exportarParaFhirJson(Exame exame) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);

        // 1. Criar o recurso DiagnosticReport (Laudo Histopatológico)
        DiagnosticReport report = new DiagnosticReport();
        report.setId(new IdType("DiagnosticReport", "DR-" + exame.getNumeroExame()));
        report.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

        // Código LOINC para laudo de Anatomia Patológica (11526-1)
        CodeableConcept code = new CodeableConcept();
        code.addCoding()
            .setSystem("http://loinc.org")
            .setCode("11526-1")
            .setDisplay("Pathology study report");
        report.setCode(code);

        // Identificação pseudonimizada do paciente (Cumprimento da LGPD)
        if (exame.getPacienteProntuario() != null) {
            report.setSubject(new Reference("Patient/PRONTUARIO-" + exame.getPacienteProntuario()));
        }

        // Mapeamento do CID-10/CID-11 sugerido no DiagnosticReport
        if (exame.getCidSugerido() != null && !exame.getCidSugerido().isBlank()) {
            CodeableConcept conclusionCode = new CodeableConcept();
            conclusionCode.addCoding()
                .setSystem("http://hl7.org/fhir/sid/icd-10")
                .setCode(exame.getCidSugerido())
                .setDisplay("CID Sugerido");
            report.addConclusionCode(conclusionCode);
        }

        // Formatação estruturada da conclusão do laudo incluindo Macroscopia, Microscopia e Diagnóstico
        StringBuilder conclusaoLaudo = new StringBuilder();

        if (exame.getMacroscopia() != null && !exame.getMacroscopia().isBlank()) {
            conclusaoLaudo.append("MACROSCOPIA:\n").append(exame.getMacroscopia()).append("\n\n");
        }
        if (exame.getMicroscopia() != null && !exame.getMicroscopia().isBlank()) {
            conclusaoLaudo.append("MICROSCOPIA:\n").append(exame.getMicroscopia()).append("\n\n");
        }
        if (exame.getDiagnosticoSugerido() != null && !exame.getDiagnosticoSugerido().isBlank()) {
            conclusaoLaudo.append("DIAGNÓSTICO:\n").append(exame.getDiagnosticoSugerido());
        }

        report.setConclusion(conclusaoLaudo.toString().trim());

        // 2. Mapeamento dos Frascos/Amostras em recursos Specimen
        if (exame.getFrascos() != null) {
            for (FrascoAmostra frasco : exame.getFrascos()) {
                Specimen specimen = new Specimen();
                specimen.setId(new IdType("Specimen", "FRASCO-" + frasco.getId()));
                
                CodeableConcept specType = new CodeableConcept();
                specType.setText(frasco.getTopografia() != null ? frasco.getTopografia() : "Biópsia Gástrica");
                specimen.setType(specType);

                // Adiciona a referência da amostra no DiagnosticReport
                report.addSpecimen(new Reference("Specimen/FRASCO-" + frasco.getId()));
                bundle.addEntry().setResource(specimen);
            }
        }

        // 3. Mapeamento dinâmico da pesquisa de H. pylori como recurso Observation (SNOMED CT)
        boolean possuiGiemsa = exame.getFrascos() != null && exame.getFrascos().stream()
            .filter(f -> f.getBlocos() != null)
            .flatMap(f -> f.getBlocos().stream())
            .filter(b -> b.getLaminas() != null)
            .flatMap(b -> b.getLaminas().stream())
            .anyMatch(l -> l.getColoracao() != null && "Giemsa".equalsIgnoreCase(l.getColoracao().trim()));

        if (possuiGiemsa) {
            Observation hpObservation = new Observation();
            hpObservation.setId(new IdType("Observation", "OBS-HP-" + exame.getId()));
            hpObservation.setStatus(Observation.ObservationStatus.FINAL);

            // SNOMED CT para Helicobacter pylori (409702008)
            CodeableConcept hpCode = new CodeableConcept();
            hpCode.addCoding()
                .setSystem("http://snomed.info/sct")
                .setCode("409702008")
                .setDisplay("Helicobacter pylori status");
            hpObservation.setCode(hpCode);

            // Determina se o diagnóstico foi positivo avaliando o texto do laudo/diagnóstico
            boolean ehPositivo = exame.getDiagnosticoSugerido() != null && 
                (exame.getDiagnosticoSugerido().toLowerCase().contains("positiv") || 
                 exame.getDiagnosticoSugerido().toLowerCase().contains("associada à infecção por helicobacter") ||
                 exame.getDiagnosticoSugerido().toLowerCase().contains("associado à infecção por helicobacter"));

            CodeableConcept hpValue = new CodeableConcept();
            if (ehPositivo) {
                hpValue.addCoding()
                    .setSystem("http://snomed.info/sct")
                    .setCode("10828004")
                    .setDisplay("Positive");
            } else {
                hpValue.addCoding()
                    .setSystem("http://snomed.info/sct")
                    .setCode("260385009")
                    .setDisplay("Negative");
            }
            hpObservation.setValue(hpValue);

            report.addResult(new Reference("Observation/OBS-HP-" + exame.getId()));
            bundle.addEntry().setResource(hpObservation);
        }

        // Adiciona o DiagnosticReport principal ao Bundle
        bundle.addEntry().setResource(report);

        // Serializa a estrutura para uma string JSON FHIR
        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);
        return parser.encodeResourceToString(bundle);
    }
}