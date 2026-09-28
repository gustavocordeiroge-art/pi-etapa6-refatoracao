package br.com.clinica.service;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.StatusConsulta;
import br.com.clinica.repository.ConsultaRepository;
import br.com.clinica.repository.Leitura;
import br.com.clinica.model.Paciente;
import java.time.LocalDateTime;
import java.util.List;

/** Regras de agendamento. Só precisa LER pacientes, então depende de Leitura (ISP). */
public class ConsultaService {
    private final ConsultaRepository consultas;
    private final Leitura<Paciente> pacientes;

    public ConsultaService(ConsultaRepository consultas, Leitura<Paciente> pacientes) {
        this.consultas = consultas;
        this.pacientes = pacientes;
    }

    public Consulta agendar(int pacienteId, LocalDateTime dataHora, String especialidade) {
        if (pacientes.buscarPorId(pacienteId).isEmpty()) {
            throw new RegraNegocioException("Paciente não encontrado: " + pacienteId);
        }
        if (especialidade == null || especialidade.isBlank()) {
            throw new RegraNegocioException("Especialidade é obrigatória.");
        }
        if (dataHora == null || !dataHora.isAfter(LocalDateTime.now())) {
            throw new RegraNegocioException("A consulta deve ser agendada para uma data futura.");
        }
        boolean conflito = consultas.listarPorPaciente(pacienteId).stream()
                .anyMatch(c -> c.getStatus() == StatusConsulta.AGENDADA && c.getDataHora().equals(dataHora));
        if (conflito) {
            throw new RegraNegocioException("O paciente já possui consulta neste horário.");
        }
        return consultas.salvar(new Consulta(pacienteId, dataHora, especialidade.trim()));
    }

    public void cancelar(int consultaId) {
        Consulta c = consultas.buscarPorId(consultaId)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada: " + consultaId));
        if (c.getStatus() != StatusConsulta.AGENDADA) {
            throw new RegraNegocioException("Só é possível cancelar consultas agendadas.");
        }
        c.setStatus(StatusConsulta.CANCELADA);
        consultas.salvar(c);
    }

    public List<Consulta> listarPorPaciente(int pacienteId) {
        return consultas.listarPorPaciente(pacienteId);
    }
}
