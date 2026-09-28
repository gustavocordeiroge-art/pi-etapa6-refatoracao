package br.com.clinica.model;

import java.time.LocalDateTime;

public class Consulta {
    private int id;
    private final int pacienteId;
    private final LocalDateTime dataHora;
    private final String especialidade;
    private StatusConsulta status;

    public Consulta(int pacienteId, LocalDateTime dataHora, String especialidade) {
        this.pacienteId = pacienteId;
        this.dataHora = dataHora;
        this.especialidade = especialidade;
        this.status = StatusConsulta.AGENDADA;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getEspecialidade() { return especialidade; }
    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }

    @Override
    public String toString() {
        return "Consulta{id=" + id + ", pacienteId=" + pacienteId + ", " + dataHora
                + ", " + especialidade + ", " + status + "}";
    }
}
