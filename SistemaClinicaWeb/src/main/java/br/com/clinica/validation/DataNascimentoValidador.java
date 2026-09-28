package br.com.clinica.validation;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;
import java.time.LocalDate;

public class DataNascimentoValidador implements ValidadorPaciente {
    @Override
    public void validar(Paciente p) {
        LocalDate nasc = p.getDataNascimento();
        if (nasc == null || nasc.isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Data de nascimento inválida.");
        }
    }
}
