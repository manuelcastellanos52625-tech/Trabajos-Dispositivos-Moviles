package com.example.taller6

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ListaScreen(elementos: List<Elemento>, onElementoClick: (Int) -> Unit) {
    // rememberSaveable para que no se borre la busqueda al volver del detalle
    var query by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf<String?>(null) }

    val categorias = elementos.map { it.categoria }.distinct()

    val filtrados = remember(query, categoria) {
        elementos.filter {
            (categoria == null || it.categoria == categoria) &&
                it.titulo.contains(query, ignoreCase = true)
        }
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        LazyRow {
            item {
                FilterChip(
                    selected = categoria == null,
                    onClick = { categoria = null },
                    label = { Text("Todas") },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
            items(categorias) { cat ->
                FilterChip(
                    selected = categoria == cat,
                    onClick = { categoria = if (categoria == cat) null else cat },
                    label = { Text(cat) },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        LazyColumn {
            items(filtrados, key = { it.id }) { elemento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onElementoClick(elemento.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(iconoDeCategoria(elemento.categoria)),
                            contentDescription = elemento.categoria,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                .padding(10.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(elemento.titulo, style = MaterialTheme.typography.titleMedium)
                            Text(elemento.descripcionCorta, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
