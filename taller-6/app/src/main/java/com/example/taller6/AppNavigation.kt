package com.example.taller6

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                elementos = elementos,
                onElementoClick = { id ->
                    navController.navigate("detalle/$id") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "detalle/{elementoId}",
            arguments = listOf(navArgument("elementoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("elementoId") ?: 0
            val elemento = elementos.firstOrNull { it.id == id }
            if (elemento != null) {
                DetalleScreen(elemento = elemento, onBack = { navController.popBackStack() })
            }
        }
    }
}
