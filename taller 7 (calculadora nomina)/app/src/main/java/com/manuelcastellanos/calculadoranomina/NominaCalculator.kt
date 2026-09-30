package com.manuelcastellanos.calculadoranomina

// Salario mínimo mensual legal vigente (SMMLV) 2026 - Decreto 1469 de 2025
const val SMMLV_2026 = 1_750_905.0

// Auxilio de transporte 2026 - Decreto 1470 de 2025
const val AUXILIO_TRANSPORTE_2026 = 249_095.0

// Horas ordinarias mensuales: jornada de 42 horas semanales (Ley 2101 de 2021),
// vigente desde el 15 de julio de 2026
const val HORAS_ORDINARIAS_MES = 210.0

// Aporte a salud del trabajador - Ley 100 de 1993
const val PORCENTAJE_SALUD = 0.04

// Aporte a pensión del trabajador - Ley 100 de 1993
const val PORCENTAJE_PENSION = 0.04

// Fondo de Solidaridad Pensional - Ley 797 de 2003
const val PORCENTAJE_FONDO_SOLIDARIDAD = 0.01

// Factores de la Regla 2 del taller: recargo del 25 % (diurna) o 75 % (nocturna),
// más 90 % dominical cuando aplica
const val FACTOR_EXTRA_DIURNA_HABIL = 1.25
const val FACTOR_EXTRA_DIURNA_DOMINICAL = 2.15
const val FACTOR_EXTRA_NOCTURNA_HABIL = 1.75
const val FACTOR_EXTRA_NOCTURNA_DOMINICAL = 2.65

// Máximo de horas extra al mes: 12 semanales durante 4 semanas
const val MAX_HORAS_EXTRA_MES = 48.0

data class ResultadoNomina(
    val salarioBasico: Double,
    val valorHora: Double,
    val totalHorasExtra: Double,
    val auxilioTransporte: Double,
    val totalDevengado: Double,
    val aporteSalud: Double,
    val aportePension: Double,
    val fondoSolidaridad: Double,
    val totalDeducciones: Double,
    val salarioNeto: Double
)

enum class RangoSalarial { RANGO_1, RANGO_2, RANGO_3 }

enum class ErrorValidacion {
    SALARIO_INVALIDO,
    SALARIO_BAJO_MINIMO,
    HORAS_INVALIDAS,
    EXCESO_HORAS
}

data class EntradaNomina(
    val salarioBasico: Double,
    val horasDiurnas: Double,
    val horasNocturnas: Double
)

sealed interface ResultadoValidacion {
    data class Valida(val entrada: EntradaNomina) : ResultadoValidacion
    data class Invalida(val error: ErrorValidacion) : ResultadoValidacion
}

fun calcularNomina(
    salarioBasico: Double,
    horasDiurnas: Double,
    horasNocturnas: Double,
    esDominical: Boolean,
    transporteEmpresa: Boolean
): ResultadoNomina {
    val valorHora = salarioBasico / HORAS_ORDINARIAS_MES

    val factorDiurno =
        if (esDominical) FACTOR_EXTRA_DIURNA_DOMINICAL else FACTOR_EXTRA_DIURNA_HABIL
    val factorNocturno =
        if (esDominical) FACTOR_EXTRA_NOCTURNA_DOMINICAL else FACTOR_EXTRA_NOCTURNA_HABIL
    val pagoExtrasDiurnas = horasDiurnas * valorHora * factorDiurno
    val pagoExtrasNocturnas = horasNocturnas * valorHora * factorNocturno
    val totalHorasExtra = pagoExtrasDiurnas + pagoExtrasNocturnas

    // El auxilio de transporte no es salario, por eso no hace parte del IBC
    val ibc = salarioBasico + totalHorasExtra

    val recibeAuxilio = salarioBasico <= 2 * SMMLV_2026 && !transporteEmpresa
    val auxilioTransporte = if (recibeAuxilio) AUXILIO_TRANSPORTE_2026 else 0.0
    val totalDevengado = ibc + auxilioTransporte

    val aporteSalud = ibc * PORCENTAJE_SALUD
    val aportePension = ibc * PORCENTAJE_PENSION
    val fondoSolidaridad =
        if (ibc >= 4 * SMMLV_2026) ibc * PORCENTAJE_FONDO_SOLIDARIDAD else 0.0
    val totalDeducciones = aporteSalud + aportePension + fondoSolidaridad

    return ResultadoNomina(
        salarioBasico = salarioBasico,
        valorHora = valorHora,
        totalHorasExtra = totalHorasExtra,
        auxilioTransporte = auxilioTransporte,
        totalDevengado = totalDevengado,
        aporteSalud = aporteSalud,
        aportePension = aportePension,
        fondoSolidaridad = fondoSolidaridad,
        totalDeducciones = totalDeducciones,
        salarioNeto = totalDevengado - totalDeducciones
    )
}

fun clasificarRango(salarioBasico: Double): RangoSalarial = when {
    salarioBasico <= 2 * SMMLV_2026 -> RangoSalarial.RANGO_1
    salarioBasico < 4 * SMMLV_2026 -> RangoSalarial.RANGO_2
    else -> RangoSalarial.RANGO_3
}

// Convierte el texto a número sin lanzar excepciones. Acepta coma decimal y
// descarta valores no finitos como "NaN" o "Infinity", que toDoubleOrNull sí admite.
private fun textoANumero(texto: String): Double? =
    texto.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() }

fun validarEntradas(
    salario: String,
    horasDiurnas: String,
    horasNocturnas: String
): ResultadoValidacion {
    val salarioBasico = textoANumero(salario)
        ?: return ResultadoValidacion.Invalida(ErrorValidacion.SALARIO_INVALIDO)
    if (salarioBasico < SMMLV_2026) {
        return ResultadoValidacion.Invalida(ErrorValidacion.SALARIO_BAJO_MINIMO)
    }

    // Las horas extra son opcionales: un campo vacío equivale a 0
    val diurnas = if (horasDiurnas.isBlank()) 0.0 else textoANumero(horasDiurnas)
    val nocturnas = if (horasNocturnas.isBlank()) 0.0 else textoANumero(horasNocturnas)
    if (diurnas == null || nocturnas == null || diurnas < 0 || nocturnas < 0) {
        return ResultadoValidacion.Invalida(ErrorValidacion.HORAS_INVALIDAS)
    }
    if (diurnas + nocturnas > MAX_HORAS_EXTRA_MES) {
        return ResultadoValidacion.Invalida(ErrorValidacion.EXCESO_HORAS)
    }

    return ResultadoValidacion.Valida(EntradaNomina(salarioBasico, diurnas, nocturnas))
}
