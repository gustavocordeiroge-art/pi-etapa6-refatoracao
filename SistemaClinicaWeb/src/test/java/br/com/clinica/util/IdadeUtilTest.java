package br.com.clinica.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IdadeUtilTest {

    private static final LocalDate REFERENCIA = LocalDate.of(2026, 10, 2);

    @Test
    void aniversarioJaOcorridoNoAno() {
        assertEquals(36, IdadeUtil.calcular(LocalDate.of(1990, 5, 20), REFERENCIA));
    }

    @Test
    void aniversarioAindaNaoOcorridoNoAno() {
        assertEquals(35, IdadeUtil.calcular(LocalDate.of(1990, 12, 25), REFERENCIA));
    }

    @Test
    void aniversarioNoProprioDiaContaOAno() {
        assertEquals(30, IdadeUtil.calcular(LocalDate.of(1996, 10, 2), REFERENCIA));
    }

    @Test
    void recemNascidoTemZeroAnos() {
        assertEquals(0, IdadeUtil.calcular(REFERENCIA, REFERENCIA));
    }

    @Test
    void nascimentoNoFuturoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> IdadeUtil.calcular(REFERENCIA.plusDays(1), REFERENCIA));
    }

    @Test
    void dataNulaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> IdadeUtil.calcular(null, REFERENCIA));
    }
}
