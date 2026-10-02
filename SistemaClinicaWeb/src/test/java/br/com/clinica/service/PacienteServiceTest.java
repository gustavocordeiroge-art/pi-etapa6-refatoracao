package br.com.clinica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;
import br.com.clinica.repository.PacienteRepositoryMemoria;
import br.com.clinica.validation.CpfValidador;
import br.com.clinica.validation.DataNascimentoValidador;
import br.com.clinica.validation.NomeObrigatorioValidador;
import br.com.clinica.validation.TelefoneValidador;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Testa o service com repositório em memória: sem banco e sem arquivo. */
class PacienteServiceTest {

    private PacienteService service;

    @BeforeEach
    void preparar() {
        service = new PacienteService(new PacienteRepositoryMemoria(), List.of(
                new NomeObrigatorioValidador(), new CpfValidador(),
                new DataNascimentoValidador(), new TelefoneValidador()));
    }

    private Paciente ana() {
        return new Paciente("Ana Souza", "529.982.247-25", LocalDate.of(1990, 5, 20), "(51) 99999-1234");
    }

    @Test
    void cadastroValidoGeraIdENormalizaCpf() {
        Paciente salvo = service.cadastrar(ana());
        assertTrue(salvo.getId() > 0);
        assertEquals("52998224725", salvo.getCpf());
    }

    @Test
    void cpfDuplicadoEhRejeitado() {
        service.cadastrar(ana());
        RegraNegocioException e = assertThrows(RegraNegocioException.class, () -> service.cadastrar(ana()));
        assertEquals("Já existe paciente cadastrado com este CPF.", e.getMessage());
    }

    @Test
    void cpfInvalidoNaoEhSalvo() {
        Paciente p = new Paciente("Bruno Lima", "111.111.111-11", LocalDate.of(1985, 1, 1), "51988887777");
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(p));
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void atualizacaoAlteraDados() {
        Paciente p = service.cadastrar(ana());
        p.setNome("Ana Souza Lima");
        service.atualizar(p);
        assertEquals("Ana Souza Lima", service.buscar(p.getId()).getNome());
    }

    @Test
    void atualizarComCpfDeOutroPacienteEhRejeitado() {
        service.cadastrar(ana());
        Paciente bia = service.cadastrar(
                new Paciente("Beatriz Melo", "111.444.777-35", LocalDate.of(2000, 3, 3), "5133334444"));
        bia.setCpf("529.982.247-25");
        assertThrows(RegraNegocioException.class, () -> service.atualizar(bia));
    }

    @Test
    void removerPacienteInexistenteLancaExcecao() {
        assertThrows(RegraNegocioException.class, () -> service.remover(999));
    }

    @Test
    void pacienteRemovidoNaoEhMaisEncontrado() {
        Paciente p = service.cadastrar(ana());
        service.remover(p.getId());
        assertThrows(RegraNegocioException.class, () -> service.buscar(p.getId()));
    }
}
