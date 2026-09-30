package com.example.calculadoranomina

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// Casos de verificación A a E de la sección 3.4 del taller (tolerancia de $1 por redondeo)
class NominaCalculatorTest {

    private val delta = 1.0

    @Test
    fun casoA_salarioMinimoConHorasExtraEnDiaHabil() {
        val r = calcularNomina(1_750_905.0, 10.0, 4.0, esDominical = false, transporteEmpresa = false)
        assertEquals(8_338.0, r.valorHora, delta)
        assertEquals(162_584.0, r.totalHorasExtra, delta)
        assertEquals(249_095.0, r.auxilioTransporte, delta)
        assertEquals(2_162_584.0, r.totalDevengado, delta)
        assertEquals(76_540.0, r.aporteSalud, delta)
        assertEquals(76_540.0, r.aportePension, delta)
        assertEquals(0.0, r.fondoSolidaridad, delta)
        assertEquals(2_009_505.0, r.salarioNeto, delta)
        assertEquals(RangoSalarial.RANGO_1, clasificarRango(1_750_905.0))
    }

    @Test
    fun casoB_horasExtraEnDomingoOFestivo() {
        val r = calcularNomina(2_500_000.0, 6.0, 2.0, esDominical = true, transporteEmpresa = false)
        assertEquals(11_905.0, r.valorHora, delta)
        assertEquals(216_667.0, r.totalHorasExtra, delta)
        assertEquals(249_095.0, r.auxilioTransporte, delta)
        assertEquals(2_965_762.0, r.totalDevengado, delta)
        assertEquals(108_667.0, r.aporteSalud, delta)
        assertEquals(108_667.0, r.aportePension, delta)
        assertEquals(0.0, r.fondoSolidaridad, delta)
        assertEquals(2_748_428.0, r.salarioNeto, delta)
        assertEquals(RangoSalarial.RANGO_1, clasificarRango(2_500_000.0))
    }

    @Test
    fun casoC_transporteSuministradoPorLaEmpresa() {
        val r = calcularNomina(3_000_000.0, 5.0, 0.0, esDominical = false, transporteEmpresa = true)
        assertEquals(14_286.0, r.valorHora, delta)
        assertEquals(89_286.0, r.totalHorasExtra, delta)
        assertEquals(0.0, r.auxilioTransporte, delta)
        assertEquals(3_089_286.0, r.totalDevengado, delta)
        assertEquals(123_571.0, r.aporteSalud, delta)
        assertEquals(123_571.0, r.aportePension, delta)
        assertEquals(0.0, r.fondoSolidaridad, delta)
        assertEquals(2_842_143.0, r.salarioNeto, delta)
        assertEquals(RangoSalarial.RANGO_1, clasificarRango(3_000_000.0))
    }

    @Test
    fun casoD_salarioAltoConFondoDeSolidaridad() {
        val r = calcularNomina(8_000_000.0, 0.0, 0.0, esDominical = false, transporteEmpresa = false)
        assertEquals(38_095.0, r.valorHora, delta)
        assertEquals(0.0, r.totalHorasExtra, delta)
        assertEquals(0.0, r.auxilioTransporte, delta)
        assertEquals(8_000_000.0, r.totalDevengado, delta)
        assertEquals(320_000.0, r.aporteSalud, delta)
        assertEquals(320_000.0, r.aportePension, delta)
        assertEquals(80_000.0, r.fondoSolidaridad, delta)
        assertEquals(7_280_000.0, r.salarioNeto, delta)
        assertEquals(RangoSalarial.RANGO_3, clasificarRango(8_000_000.0))
    }

    @Test
    fun rangos_enLosLimites() {
        assertEquals(RangoSalarial.RANGO_1, clasificarRango(2 * SMMLV_2026))
        assertEquals(RangoSalarial.RANGO_2, clasificarRango(2 * SMMLV_2026 + 1))
        assertEquals(RangoSalarial.RANGO_2, clasificarRango(4 * SMMLV_2026 - 1))
        assertEquals(RangoSalarial.RANGO_3, clasificarRango(4 * SMMLV_2026))
    }

    @Test
    fun casoE_salarioVacioONoNumerico() {
        assertInvalida(ErrorValidacion.SALARIO_INVALIDO, validarEntradas("", "", ""))
        assertInvalida(ErrorValidacion.SALARIO_INVALIDO, validarEntradas("abc", "", ""))
        assertInvalida(ErrorValidacion.SALARIO_INVALIDO, validarEntradas("Infinity", "", ""))
        assertInvalida(ErrorValidacion.SALARIO_INVALIDO, validarEntradas("NaN", "", ""))
    }

    @Test
    fun casoE_salarioInferiorAlMinimo() {
        assertInvalida(
            ErrorValidacion.SALARIO_BAJO_MINIMO,
            validarEntradas("1000000", "", "")
        )
    }

    @Test
    fun casoE_excesoDeHorasExtra() {
        assertInvalida(
            ErrorValidacion.EXCESO_HORAS,
            validarEntradas("2000000", "30", "20")
        )
    }

    @Test
    fun casoE_horasExtraVaciasSeTomanComoCero() {
        val validacion = validarEntradas("2000000", "", "")
        assertTrue(validacion is ResultadoValidacion.Valida)
        val entrada = (validacion as ResultadoValidacion.Valida).entrada
        assertEquals(0.0, entrada.horasDiurnas, 0.0)
        assertEquals(0.0, entrada.horasNocturnas, 0.0)
    }

    @Test
    fun horasExtraNegativasONoNumericas() {
        assertInvalida(ErrorValidacion.HORAS_INVALIDAS, validarEntradas("2000000", "-1", ""))
        assertInvalida(ErrorValidacion.HORAS_INVALIDAS, validarEntradas("2000000", "", "x"))
    }

    @Test
    fun limiteExactoDe48HorasEsValido() {
        assertTrue(validarEntradas("2000000", "24", "24") is ResultadoValidacion.Valida)
    }

    private fun assertInvalida(esperado: ErrorValidacion, actual: ResultadoValidacion) {
        assertTrue("Se esperaba $esperado pero fue $actual", actual is ResultadoValidacion.Invalida)
        assertEquals(esperado, (actual as ResultadoValidacion.Invalida).error)
    }
}
