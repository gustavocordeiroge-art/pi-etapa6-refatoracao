package br.com.clinica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.StatusConsulta;
import br.com.clinica.repository.ConsultaRepositoryMemoria;
import br.com.clinica.repository.PacienteRepositoryMemoria;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultaServiceTest {

    private ConsultaService service;
    private int pacienteId;
    private LocalDateTime amanha;

    @BeforeEach
    void preparar() {
        PacienteRepositoryMemoria pacientes = new PacienteRepositoryMemoria();
        Paciente p = pacientes.salvar(new Paciente("Ana Souza", "52998224725", LocalDate.of(1990, 5, 20), "51999991234"));
        pacienteId = p.getId();
        service = new ConsultaService(new ConsultaRepositoryMemoria(), pacientes);
        amanha = LocalDateTime.now().plusDays(1).withNano(0);
    }

    @Test
    void consultaNasceAgendada() {
        assertEquals(StatusConsulta.AGENDADA, service.agendar(pacienteId, amanha, "Clínica Geral").getStatus());
    }

    @Test
    void pacienteInexistenteEhRejeitado() {
        assertThrows(RegraNegocioException.class, () -> service.agendar(999, amanha, "Clínica Geral"));
    }

    @Test
    void dataPassadaEhRejeitada() {
        assertThrows(RegraNegocioException.class,
                () -> service.agendar(pacienteId, LocalDateTime.now().minusDays(1), "Clínica Geral"));
    }

    @Test
    void especialidadeEmBrancoEhRejeitada() {
        assertThrows(RegraNegocioException.class, () -> service.agendar(pacienteId, amanha, "  "));
    }

    @Test
    void conflitoDeHorarioEhRejeitado() {
        service.agendar(pacienteId, amanha, "Clínica Geral");
        assertThrows(RegraNegocioException.class, () -> service.agendar(pacienteId, amanha, "Cardiologia"));
    }

    @Test
    void cancelarLiberaOHorario() {
        Consulta c = service.agendar(pacienteId, amanha, "Clínica Geral");
        service.cancelar(c.getId());
        assertEquals(StatusConsulta.CANCELADA, service.listarPorPaciente(pacienteId).get(0).getStatus());
        service.agendar(pacienteId, amanha, "Cardiologia");
        assertEquals(2, service.listarPorPaciente(pacienteId).size());
    }

    @Test
    void naoCancelaDuasVezes() {
        Consulta c = service.agendar(pacienteId, amanha, "Clínica Geral");
        service.cancelar(c.getId());
        assertThrows(RegraNegocioException.class, () -> service.cancelar(c.getId()));
    }
}
