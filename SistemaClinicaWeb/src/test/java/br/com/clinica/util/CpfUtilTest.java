package br.com.clinica.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CpfUtilTest {

    @Test
    void cpfValidoComMascaraEhAceito() {
        assertTrue(CpfUtil.valido("529.982.247-25"));
    }

    @Test
    void cpfValidoSemMascaraEhAceito() {
        assertTrue(CpfUtil.valido("11144477735"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"111.111.111-11", "123.456.789-00", "529.982.247-24", "123", ""})
    void cpfsInvalidosSaoRejeitados(String cpf) {
        assertFalse(CpfUtil.valido(cpf));
    }

    @Test
    void cpfNuloEhInvalido() {
        assertFalse(CpfUtil.valido(null));
    }

    @Test
    void somenteDigitosRemoveMascara() {
        assertEquals("52998224725", CpfUtil.somenteDigitos("529.982.247-25"));
    }
}
