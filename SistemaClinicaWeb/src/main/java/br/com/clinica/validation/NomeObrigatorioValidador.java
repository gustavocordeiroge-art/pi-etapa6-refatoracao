package br.com.clinica.validation;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;

public class NomeObrigatorioValidador implements ValidadorPaciente {
    @Override
    public void validar(Paciente p) {
        if (p.getNome() == null || p.getNome().trim().length() < 3) {
            throw new RegraNegocioException("Nome é obrigatório e deve ter ao menos 3 caracteres.");
        }
    }
}
