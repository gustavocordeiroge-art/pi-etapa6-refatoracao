package br.com.clinica.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.clinica.exception.RegraNegocioException;
import br.com.clinica.model.Paciente;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ValidadoresTest {

    private Paciente paciente(String nome, String cpf, LocalDate nasc, String tel) {
        return new Paciente(nome, cpf, nasc, tel);
    }

    @Test
    void nomeComTresCaracteresEhAceito() {
        assertDoesNotThrow(() -> new NomeObrigatorioValidador()
                .validar(paciente("Ana", "x", LocalDate.of(2000, 1, 1), "x")));
    }

    @Test
    void nomeCurtoOuNuloEhRejeitado() {
        NomeObrigatorioValidador v = new NomeObrigatorioValidador();
        assertThrows(RegraNegocioException.class, () -> v.validar(paciente("Al", "x", null, "x")));
        assertThrows(RegraNegocioException.class, () -> v.validar(paciente(null, "x", null, "x")));
    }

    @Test
    void cpfInvalidoEhRejeitado() {
        assertThrows(RegraNegocioException.class, () -> new CpfValidador()
                .validar(paciente("Ana", "123", LocalDate.of(2000, 1, 1), "x")));
    }

    @Test
    void dataDeNascimentoHojeEhAceitaEFuturaEhRejeitada() {
        DataNascimentoValidador v = new DataNascimentoValidador();
        assertDoesNotThrow(() -> v.validar(paciente("Ana", "x", LocalDate.now(), "x")));
        assertThrows(RegraNegocioException.class,
                () -> v.validar(paciente("Ana", "x", LocalDate.now().plusDays(1), "x")));
    }

    @Test
    void telefoneAceitaFixoECelularComMascara() {
        TelefoneValidador v = new TelefoneValidador();
        assertDoesNotThrow(() -> v.validar(paciente("Ana", "x", null, "(51) 3333-4444")));
        assertDoesNotThrow(() -> v.validar(paciente("Ana", "x", null, "(51) 99999-1234")));
    }

    @Test
    void telefoneCurtoOuLongoDemaisEhRejeitado() {
        TelefoneValidador v = new TelefoneValidador();
        assertThrows(RegraNegocioException.class, () -> v.validar(paciente("Ana", "x", null, "1234")));
        assertThrows(RegraNegocioException.class, () -> v.validar(paciente("Ana", "x", null, "519999912345")));
    }
}
