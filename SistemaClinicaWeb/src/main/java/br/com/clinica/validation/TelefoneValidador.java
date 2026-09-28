package br.com.clinica.validation;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;

public class TelefoneValidador implements ValidadorPaciente {
    @Override
    public void validar(Paciente p) {
        String digitos = p.getTelefone() == null ? "" : p.getTelefone().replaceAll("\\D", "");
        if (digitos.length() < 10 || digitos.length() > 11) {
            throw new RegraNegocioException("Telefone deve ter 10 ou 11 dígitos (com DDD).");
        }
    }
}
