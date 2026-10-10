package br.edu.ufcspa.pathol_api.dto;

public record LaminaDTO(
    String codigoLamina, // Ex: AP-2026-08912-2/3-GIEMSA
    String coloracao     // Ex: HE, GIEMSA, PAS, IMUNOHISTOQUIMICA
) {}