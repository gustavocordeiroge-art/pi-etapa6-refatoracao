package br.com.clinica.util;

import java.time.LocalDate;
import java.time.Period;

/** Cálculo de idade isolado em método próprio para facilitar o teste unitário. */
public final class IdadeUtil {
    private IdadeUtil() { }

    public static int calcular(LocalDate nascimento, LocalDate referencia) {
        if (nascimento == null || referencia == null || nascimento.isAfter(referencia)) {
            throw new IllegalArgumentException("Datas inválidas para o cálculo de idade.");
        }
        return Period.between(nascimento, referencia).getYears();
    }
}
