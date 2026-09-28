package br.com.clinica.repository;

import br.com.clinica.model.Consulta;
import java.util.List;

public interface ConsultaRepository extends Leitura<Consulta>, Escrita<Consulta> {
    List<Consulta> listarPorPaciente(int pacienteId);
}
