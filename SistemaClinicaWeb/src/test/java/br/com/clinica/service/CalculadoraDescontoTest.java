package br.com.clinica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.clinica.exception.RegraNegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculadoraDescontoTest {

    private CalculadoraDesconto calculadora;

    @BeforeEach
    void preparar() {
        calculadora = new CalculadoraDesconto();
    }

    @ParameterizedTest(name = "idade {0} -> desconto {1}")
    @CsvSource({"0,0.20", "11,0.20", "12,0", "30,0", "59,0", "60,0.30", "90,0.30"})
    void percentualPorFaixaEtaria(int idade, String esperado) {
        assertEquals(0, new BigDecimal(esperado).compareTo(calculadora.percentual(idade)));
    }

    @Test
    void criancaPaga80PorCento() {
        assertEquals(new BigDecimal("80.00"), calculadora.valorFinal(8, new BigDecimal("100.00")));
    }

    @Test
    void idosoPaga70PorCento() {
        assertEquals(new BigDecimal("70.00"), calculadora.valorFinal(65, new BigDecimal("100.00")));
    }

    @Test
    void adultoPagaValorIntegral() {
        assertEquals(new BigDecimal("150.00"), calculadora.valorFinal(40, new BigDecimal("150")));
    }

    @Test
    void resultadoEhArredondadoParaDuasCasas() {
        assertEquals(new BigDecimal("46.67"), calculadora.valorFinal(70, new BigDecimal("66.67")));
    }

    @Test
    void idadeNegativaEhRejeitada() {
        assertThrows(RegraNegocioException.class, () -> calculadora.percentual(-1));
    }

    @Test
    void valorNegativoOuNuloEhRejeitado() {
        assertThrows(RegraNegocioException.class, () -> calculadora.valorFinal(30, new BigDecimal("-1")));
        assertThrows(RegraNegocioException.class, () -> calculadora.valorFinal(30, null));
    }
}
