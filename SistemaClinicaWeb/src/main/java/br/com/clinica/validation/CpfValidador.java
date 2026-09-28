package br.com.clinica.validation;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;
import br.com.clinica.util.CpfUtil;

public class CpfValidador implements ValidadorPaciente {
    @Override
    public void validar(Paciente p) {
        if (!CpfUtil.valido(p.getCpf())) {
            throw new RegraNegocioException("CPF inválido.");
        }
    }
}
