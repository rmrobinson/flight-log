package ca.faltung.flightlog.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val flightListRoute = "flight_list_route"
const val addFlightRoute = "flight_add_route"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FlightLogApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = flightListRoute

) {
    Scaffold (
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(addFlightRoute)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add flight")
            }
        },
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(flightListRoute) {
                FlightListRoute()
            }
            composable(addFlightRoute) {
                AddFlightRoute(
                    onFlightAdded = {
                        navController.navigate(flightListRoute)
                    }
                )
            }
        }
    }
}