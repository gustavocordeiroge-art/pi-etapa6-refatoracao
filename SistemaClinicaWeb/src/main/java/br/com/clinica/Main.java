package br.com.clinica;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.StatusConsulta;
import br.com.clinica.repository.ConsultaRepositoryMemoria;
import br.com.clinica.repository.PacienteRepository;
import br.com.clinica.repository.PacienteRepositoryArquivo;
import br.com.clinica.repository.PacienteRepositoryMemoria;
import br.com.clinica.service.ConsultaService;
import br.com.clinica.service.PacienteService;
import br.com.clinica.validation.CpfValidador;
import br.com.clinica.validation.DataNascimentoValidador;
import br.com.clinica.validation.NomeObrigatorioValidador;
import br.com.clinica.validation.TelefoneValidador;
import br.com.clinica.validation.ValidadorPaciente;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Raiz de composição + testes manuais no main(), conforme pedido no enunciado.
 * Aqui (e só aqui) as implementações concretas são escolhidas e injetadas.
 */
public class Main {
    private static int passou = 0;
    private static int falhou = 0;

    public static void main(String[] args) throws IOException {
        List<ValidadorPaciente> validadores = List.of(new NomeObrigatorioValidador(), new CpfValidador(),
                new DataNascimentoValidador(), new TelefoneValidador());

        System.out.println("=== Testes com repositório em MEMÓRIA ===");
        PacienteRepository repoMem = new PacienteRepositoryMemoria();
        testarPacientes(new PacienteService(repoMem, validadores));
        testarConsultas(new PacienteService(new PacienteRepositoryMemoria(), validadores), repoMem);

        System.out.println("\n=== Testes com repositório em ARQUIVO (troca sem alterar o service) ===");
        Path tmp = Files.createTempFile("pacientes", ".csv");
        Files.delete(tmp);
        PacienteService svcArquivo = new PacienteService(new PacienteRepositoryArquivo(tmp), validadores);
        testarPacientes(svcArquivo);
        PacienteService recarregado = new PacienteService(new PacienteRepositoryArquivo(tmp), validadores);
        verificar("Dados persistem após reabrir o arquivo", recarregado.listar().size() == 1);
        Files.deleteIfExists(tmp);

        System.out.println("\nResultado: " + passou + " passaram, " + falhou + " falharam.");
        if (falhou > 0) {
            System.exit(1);
        }
    }

    private static Paciente ana() {
        return new Paciente("Ana Souza", "529.982.247-25", LocalDate.of(1990, 5, 20), "(51) 99999-1234");
    }

    private static void testarPacientes(PacienteService service) {
        Paciente ana = service.cadastrar(ana());
        verificar("Cadastro válido gera id", ana.getId() > 0);
        verificar("CPF é armazenado só com dígitos", ana.getCpf().equals("52998224725"));
        verificar("Busca por id retorna o paciente", service.buscar(ana.getId()).getNome().equals("Ana Souza"));

        esperarErro("CPF inválido é rejeitado", () -> service.cadastrar(
                new Paciente("Bruno Lima", "111.111.111-11", LocalDate.of(1985, 1, 1), "51988887777")));
        esperarErro("Nome curto é rejeitado", () -> service.cadastrar(
                new Paciente("Al", "111.444.777-35", LocalDate.of(1985, 1, 1), "51988887777")));
        esperarErro("Data de nascimento futura é rejeitada", () -> service.cadastrar(
                new Paciente("Carlos Dias", "111.444.777-35", LocalDate.now().plusDays(1), "51988887777")));
        esperarErro("Telefone curto é rejeitado", () -> service.cadastrar(
                new Paciente("Carlos Dias", "111.444.777-35", LocalDate.of(1985, 1, 1), "1234")));
        esperarErro("CPF duplicado é rejeitado", () -> service.cadastrar(ana()));

        ana.setNome("Ana Souza Lima");
        service.atualizar(ana);
        verificar("Atualização altera o nome", service.buscar(ana.getId()).getNome().equals("Ana Souza Lima"));

        Paciente bia = service.cadastrar(
                new Paciente("Beatriz Melo", "111.444.777-35", LocalDate.of(2000, 3, 3), "5133334444"));
        service.remover(bia.getId());
        esperarErro("Paciente removido não é mais encontrado", () -> service.buscar(bia.getId()));
        verificar("Lista contém apenas 1 paciente", service.listar().size() == 1);
    }

    private static void testarConsultas(PacienteService ignorado, PacienteRepository repoPacientes) {
        ConsultaService service = new ConsultaService(new ConsultaRepositoryMemoria(), repoPacientes);
        int pacienteId = repoPacientes.listarTodos().get(0).getId();
        LocalDateTime amanha = LocalDateTime.now().plusDays(1).withNano(0);

        Consulta c = service.agendar(pacienteId, amanha, "Clínica Geral");
        verificar("Consulta nasce com status AGENDADA", c.getStatus() == StatusConsulta.AGENDADA);
        esperarErro("Conflito de horário é rejeitado", () -> service.agendar(pacienteId, amanha, "Cardiologia"));
        esperarErro("Data passada é rejeitada", () -> service.agendar(pacienteId, LocalDateTime.now().minusDays(1), "Clínica Geral"));
        esperarErro("Paciente inexistente é rejeitado", () -> service.agendar(999, amanha, "Clínica Geral"));
        esperarErro("Especialidade vazia é rejeitada", () -> service.agendar(pacienteId, amanha.plusHours(1), " "));

        service.cancelar(c.getId());
        verificar("Cancelamento muda o status", service.listarPorPaciente(pacienteId).get(0).getStatus() == StatusConsulta.CANCELADA);
        esperarErro("Não cancela duas vezes", () -> service.cancelar(c.getId()));
        service.agendar(pacienteId, amanha, "Cardiologia");
        verificar("Após cancelar, o horário pode ser reutilizado", service.listarPorPaciente(pacienteId).size() == 2);
    }

    private static void verificar(String nome, boolean condicao) {
        if (condicao) {
            passou++;
            System.out.println("  [OK]     " + nome);
        } else {
            falhou++;
            System.out.println("  [FALHOU] " + nome);
        }
    }

    private static void esperarErro(String nome, Runnable acao) {
        try {
            acao.run();
            falhou++;
            System.out.println("  [FALHOU] " + nome + " (nenhuma exceção lançada)");
        } catch (RegraNegocioException e) {
            passou++;
            System.out.println("  [OK]     " + nome + " -> \"" + e.getMessage() + "\"");
        }
    }
}
