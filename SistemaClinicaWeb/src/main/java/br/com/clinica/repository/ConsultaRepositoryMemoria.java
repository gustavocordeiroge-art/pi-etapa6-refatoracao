package br.com.clinica.repository;

import br.com.clinica.model.Consulta;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConsultaRepositoryMemoria implements ConsultaRepository {
    private final Map<Integer, Consulta> dados = new LinkedHashMap<>();
    private int proximoId = 1;

    @Override
    public Consulta salvar(Consulta c) {
        if (c.getId() == 0) {
            c.setId(proximoId++);
        }
        dados.put(c.getId(), c);
        return c;
    }

    @Override
    public void remover(int id) {
        dados.remove(id);
    }

    @Override
    public Optional<Consulta> buscarPorId(int id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Consulta> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public List<Consulta> listarPorPaciente(int pacienteId) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : dados.values()) {
            if (c.getPacienteId() == pacienteId) {
                resultado.add(c);
            }
        }
        return resultado;
    }
}
