package com.example.calculadoranomina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadoranomina.ui.theme.CalculadoraNominaTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraNominaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaNomina()
                }
            }
        }
    }
}

// Permite conservar el resultado calculado al rotar la pantalla
private val ResultadoNominaSaver = listSaver<ResultadoNomina?, Double>(
    save = { r ->
        if (r == null) emptyList()
        else listOf(
            r.salarioBasico, r.valorHora, r.totalHorasExtra, r.auxilioTransporte,
            r.totalDevengado, r.aporteSalud, r.aportePension, r.fondoSolidaridad,
            r.totalDeducciones, r.salarioNeto
        )
    },
    restore = { v ->
        if (v.isEmpty()) null
        else ResultadoNomina(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9])
    }
)

@Composable
fun PantallaNomina() {
    var salario by rememberSaveable { mutableStateOf("") }
    var horasDiurnas by rememberSaveable { mutableStateOf("") }
    var horasNocturnas by rememberSaveable { mutableStateOf("") }
    var esDominical by rememberSaveable { mutableStateOf(false) }
    var transporteEmpresa by rememberSaveable { mutableStateOf(false) }
    var resultado by rememberSaveable(stateSaver = ResultadoNominaSaver) {
        mutableStateOf<ResultadoNomina?>(null)
    }
    var error by rememberSaveable { mutableStateOf<ErrorValidacion?>(null) }

    fun calcular() {
        when (val validacion = validarEntradas(salario, horasDiurnas, horasNocturnas)) {
            is ResultadoValidacion.Valida -> {
                error = null
                resultado = calcularNomina(
                    salarioBasico = validacion.entrada.salarioBasico,
                    horasDiurnas = validacion.entrada.horasDiurnas,
                    horasNocturnas = validacion.entrada.horasNocturnas,
                    esDominical = esDominical,
                    transporteEmpresa = transporteEmpresa
                )
            }

            is ResultadoValidacion.Invalida -> {
                error = validacion.error
                resultado = null
            }
        }
    }

    fun limpiar() {
        salario = ""
        horasDiurnas = ""
        horasNocturnas = ""
        esDominical = false
        transporteEmpresa = false
        resultado = null
        error = null
    }

    val errorSalario = error == ErrorValidacion.SALARIO_INVALIDO ||
            error == ErrorValidacion.SALARIO_BAJO_MINIMO
    val errorHoras = error == ErrorValidacion.HORAS_INVALIDAS ||
            error == ErrorValidacion.EXCESO_HORAS

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        CampoNumerico(
            etiqueta = R.string.salario_basico,
            valor = salario,
            onValueChange = { salario = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            isError = errorSalario,
            modifier = Modifier.fillMaxWidth()
        )
        CampoNumerico(
            etiqueta = R.string.horas_extra_diurnas,
            valor = horasDiurnas,
            onValueChange = { horasDiurnas = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            isError = errorHoras,
            modifier = Modifier.fillMaxWidth()
        )
        CampoNumerico(
            etiqueta = R.string.horas_extra_nocturnas,
            valor = horasNocturnas,
            onValueChange = { horasNocturnas = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = errorHoras,
            modifier = Modifier.fillMaxWidth()
        )

        FilaInterruptor(
            etiqueta = R.string.horas_dominical,
            valor = esDominical,
            onCheckedChange = {
                esDominical = it
                if (resultado != null) calcular()
            }
        )
        FilaInterruptor(
            etiqueta = R.string.transporte_empresa,
            valor = transporteEmpresa,
            onCheckedChange = {
                transporteEmpresa = it
                if (resultado != null) calcular()
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { calcular() }, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.calcular))
            }
            OutlinedButton(onClick = { limpiar() }, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.limpiar))
            }
        }

        error?.let {
            Text(
                text = mensajeError(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        resultado?.let { ResultadoNominaCompleto(it) }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CampoNumerico(
    @StringRes etiqueta: Int,
    valor: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(stringResource(etiqueta)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        isError = isError,
        modifier = modifier
    )
}

@Composable
fun FilaInterruptor(
    @StringRes etiqueta: Int,
    valor: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(etiqueta),
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )
        Switch(checked = valor, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ResultadoNominaCompleto(resultado: ResultadoNomina) {
    val rango = clasificarRango(resultado.salarioBasico)

    Image(
        painter = painterResource(imagenDeRango(rango)),
        contentDescription = stringResource(descripcionDeRango(rango))
    )
    Text(
        text = stringResource(textoDeRango(rango)),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SeccionTitulo(R.string.seccion_devengado)
            LineaDesglose(R.string.valor_hora, resultado.valorHora)
            LineaDesglose(R.string.salario_basico_linea, resultado.salarioBasico)
            LineaDesglose(R.string.horas_extra_linea, resultado.totalHorasExtra)
            LineaDesglose(R.string.auxilio_transporte, resultado.auxilioTransporte)
            LineaDesglose(R.string.total_devengado, resultado.totalDevengado, destacada = true)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SeccionTitulo(R.string.seccion_deducciones)
            LineaDesglose(R.string.salud, resultado.aporteSalud)
            LineaDesglose(R.string.pension, resultado.aportePension)
            LineaDesglose(R.string.fondo_solidaridad, resultado.fondoSolidaridad)
            LineaDesglose(R.string.total_deducciones, resultado.totalDeducciones, destacada = true)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SeccionTitulo(R.string.seccion_resultado)
            Text(
                text = stringResource(R.string.salario_neto),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = formatearMoneda(resultado.salarioNeto),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(
                    R.string.equivale_smmlv,
                    resultado.salarioNeto / SMMLV_2026
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SeccionTitulo(@StringRes titulo: Int) {
    Text(
        text = stringResource(titulo),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun LineaDesglose(
    @StringRes etiqueta: Int,
    valor: Double,
    destacada: Boolean = false
) {
    val peso = if (destacada) FontWeight.Bold else FontWeight.Normal
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(etiqueta),
            fontWeight = peso,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Text(text = formatearMoneda(valor), fontWeight = peso)
    }
}

@Composable
private fun mensajeError(error: ErrorValidacion): String = when (error) {
    ErrorValidacion.SALARIO_INVALIDO -> stringResource(R.string.error_salario_invalido)
    ErrorValidacion.SALARIO_BAJO_MINIMO ->
        stringResource(R.string.error_salario_minimo, formatearMoneda(SMMLV_2026))

    ErrorValidacion.HORAS_INVALIDAS -> stringResource(R.string.error_horas_invalidas)
    ErrorValidacion.EXCESO_HORAS -> stringResource(R.string.error_exceso_horas)
}

@DrawableRes
private fun imagenDeRango(rango: RangoSalarial): Int = when (rango) {
    RangoSalarial.RANGO_1 -> R.drawable.ic_rango_1
    RangoSalarial.RANGO_2 -> R.drawable.ic_rango_2
    RangoSalarial.RANGO_3 -> R.drawable.ic_rango_3
}

@StringRes
private fun textoDeRango(rango: RangoSalarial): Int = when (rango) {
    RangoSalarial.RANGO_1 -> R.string.rango_1_texto
    RangoSalarial.RANGO_2 -> R.string.rango_2_texto
    RangoSalarial.RANGO_3 -> R.string.rango_3_texto
}

@StringRes
private fun descripcionDeRango(rango: RangoSalarial): Int = when (rango) {
    RangoSalarial.RANGO_1 -> R.string.rango_1_descripcion
    RangoSalarial.RANGO_2 -> R.string.rango_2_descripcion
    RangoSalarial.RANGO_3 -> R.string.rango_3_descripcion
}

// Pesos colombianos sin decimales, por ejemplo: $ 2.009.505
fun formatearMoneda(valor: Double): String {
    val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    formato.maximumFractionDigits = 0
    formato.minimumFractionDigits = 0
    return formato.format(valor)
}

@Preview(showBackground = true)
@Composable
fun PantallaNominaPreview() {
    CalculadoraNominaTheme {
        PantallaNomina()
    }
}
