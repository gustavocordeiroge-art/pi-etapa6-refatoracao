package br.com.clinica.validation;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;

/** Strategy: cada regra de validação é uma implementação independente. */
public interface ValidadorPaciente {
    void validar(Paciente paciente) throws RegraNegocioException;
}
