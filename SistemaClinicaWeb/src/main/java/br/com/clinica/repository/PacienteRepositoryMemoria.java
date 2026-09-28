package br.com.clinica.repository;

import br.com.clinica.model.Paciente;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Implementação em memória: útil para testes rápidos, sem banco de dados. */
public class PacienteRepositoryMemoria implements PacienteRepository {
    protected final Map<Integer, Paciente> dados = new LinkedHashMap<>();
    protected int proximoId = 1;

    @Override
    public Paciente salvar(Paciente p) {
        if (p.getId() == 0) {
            p.setId(proximoId++);
        }
        dados.put(p.getId(), p);
        return p;
    }

    @Override
    public void remover(int id) {
        dados.remove(id);
    }

    @Override
    public Optional<Paciente> buscarPorId(int id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public Optional<Paciente> buscarPorCpf(String cpf) {
        return dados.values().stream().filter(p -> p.getCpf().equals(cpf)).findFirst();
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(dados.values());
    }
}
