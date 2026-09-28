package br.com.clinica.service;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;
import br.com.clinica.repository.PacienteRepository;
import br.com.clinica.util.CpfUtil;
import br.com.clinica.validation.ValidadorPaciente;
import java.util.List;

/**
 * Regras de negócio de Paciente. Depende de abstrações (repositório e validadores)
 * recebidas no construtor: DIP + OCP (novas regras entram sem alterar esta classe).
 */
public class PacienteService {
    private final PacienteRepository repositorio;
    private final List<ValidadorPaciente> validadores;

    public PacienteService(PacienteRepository repositorio, List<ValidadorPaciente> validadores) {
        this.repositorio = repositorio;
        this.validadores = validadores;
    }

    public Paciente cadastrar(Paciente paciente) {
        validadores.forEach(v -> v.validar(paciente));
        paciente.setCpf(CpfUtil.somenteDigitos(paciente.getCpf()));
        repositorio.buscarPorCpf(paciente.getCpf()).ifPresent(existente -> {
            throw new RegraNegocioException("Já existe paciente cadastrado com este CPF.");
        });
        return repositorio.salvar(paciente);
    }

    public Paciente atualizar(Paciente paciente) {
        buscar(paciente.getId());
        validadores.forEach(v -> v.validar(paciente));
        paciente.setCpf(CpfUtil.somenteDigitos(paciente.getCpf()));
        repositorio.buscarPorCpf(paciente.getCpf()).ifPresent(existente -> {
            if (existente.getId() != paciente.getId()) {
                throw new RegraNegocioException("Já existe outro paciente com este CPF.");
            }
        });
        return repositorio.salvar(paciente);
    }

    public Paciente buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Paciente não encontrado: " + id));
    }

    public List<Paciente> listar() {
        return repositorio.listarTodos();
    }

    public void remover(int id) {
        buscar(id);
        repositorio.remover(id);
    }
}
