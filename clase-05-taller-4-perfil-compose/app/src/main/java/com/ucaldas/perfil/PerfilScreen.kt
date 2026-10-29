package com.ucaldas.perfil

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val TAG = "Taller4"

@Composable
fun PerfilScreen() {
    // Estado que debe sobrevivir a la rotación -> rememberSaveable (Parte 3)
    var nombre by rememberSaveable { mutableStateOf("Ana Gómez") }
    var editando by rememberSaveable { mutableStateOf(false) }
    var borrador by rememberSaveable { mutableStateOf("") }
    var publicaciones by rememberSaveable { mutableStateOf(0) }

    // Lista observable (Parte 2). Limitación conocida: mutableStateListOf no es "saveable" por
    // defecto, así que con remember se reinicia al rotar (haría falta un Saver o un ViewModel).
    val logros = remember { mutableStateListOf("Primer commit") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()                  // respeta barra de estado y notch
            .verticalScroll(rememberScrollState()) // en horizontal el contenido no cabe: permite desplazar
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Avatar(iniciales = inicialesDe(nombre))

        Spacer(modifier = Modifier.height(12.dp))

        // Parte 3.6: si el nombre queda vacío se muestra un texto por defecto
        Text(
            text = nombre.ifBlank { "Usuario sin nombre" },
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Ingeniería de Sistemas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            // El padding va DENTRO de la Card para que el color de la tarjeta llegue al borde
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Publicaciones", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$publicaciones",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Bonus B: deshabilitado cuando el contador está en 0
                    Button(
                        onClick = { if (publicaciones > 0) publicaciones-- },
                        enabled = publicaciones > 0
                    ) { Text("-") }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { publicaciones++ }) { Text("+") }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SeccionLogros(
            logros = logros,
            onAgregar = {
                logros.add("Nuevo logro ${logros.size + 1}")
                Log.d(TAG, "Tamaño de la lista: ${logros.size}")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            borrador = nombre
            editando = !editando
        }) {
            Text(if (editando) "Cancelar" else "Editar perfil")
        }

        // Bonus A: aparición animada del formulario
        AnimatedVisibility(visible = editando) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                OutlinedTextField(
                    value = borrador,
                    onValueChange = { borrador = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        nombre = borrador.ifBlank { "Usuario sin nombre" }
                        editando = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar") }
            }
        }
    }
}

@Composable
fun Avatar(iniciales: String) {
    // Orden obligatorio: size -> clip -> background. Con background antes de clip el fondo sería cuadrado.
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun SeccionLogros(logros: List<String>, onAgregar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text("Logros", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(6.dp))
        logros.forEach { logro ->
            Text(text = "• $logro", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onAgregar) { Text("Agregar logro") }
    }
}

// Bonus C: iniciales a partir del nombre
private fun inicialesDe(nombre: String): String =
    nombre.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "?" }

@Preview(showBackground = true, name = "Perfil")
@Composable
fun PerfilScreenPreview() {
    MaterialTheme { PerfilScreen() }
}
