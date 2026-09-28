package br.com.clinica.repository;

import br.com.clinica.model.Paciente;
import java.util.Optional;

/** Abstração usada pelos services (DIP). Não menciona banco, arquivo ou Swing. */
public interface PacienteRepository extends Leitura<Paciente>, Escrita<Paciente> {
    Optional<Paciente> buscarPorCpf(String cpfSomenteDigitos);
}
