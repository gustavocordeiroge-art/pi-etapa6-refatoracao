package br.com.clinica.service;

import br.com.clinica.exception.RegraNegocioException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * RN09: desconto no valor da consulta por faixa etária.
 * Menores de 12 anos: 20%. Idosos (60 anos ou mais): 30%. Demais: sem desconto.
 * Cálculo puro (sem banco e sem interface), ideal para teste unitário.
 */
public class CalculadoraDesconto {
    public static final int IDADE_CRIANCA_LIMITE = 12;
    public static final int IDADE_IDOSO_MINIMA = 60;
    public static final BigDecimal DESCONTO_CRIANCA = new BigDecimal("0.20");
    public static final BigDecimal DESCONTO_IDOSO = new BigDecimal("0.30");

    public BigDecimal percentual(int idade) {
        if (idade < 0) {
            throw new RegraNegocioException("Idade não pode ser negativa.");
        }
        if (idade < IDADE_CRIANCA_LIMITE) {
            return DESCONTO_CRIANCA;
        }
        if (idade >= IDADE_IDOSO_MINIMA) {
            return DESCONTO_IDOSO;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal valorFinal(int idade, BigDecimal valorBase) {
        if (valorBase == null || valorBase.signum() < 0) {
            throw new RegraNegocioException("Valor da consulta inválido.");
        }
        BigDecimal desconto = valorBase.multiply(percentual(idade));
        return valorBase.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
    }
}
