package week11.stn697771.aurafit


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import week11.stn697771.aurafit.ui.theme.AuraFitTheme
import week11.stn697771.aurafit.util.NavEvent
import week11.stn697771.aurafit.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuraFitTheme {
                val vm: MainViewModel = viewModel()
                val uiState by vm.uiState.collectAsState()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    vm.navEvents.collect { event ->
                        when (event) {
                            NavEvent.ToLogin -> navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            NavEvent.ToSignUp -> navController.navigate("signup")
                            NavEvent.ToForgot -> navController.navigate("forgot")
                            NavEvent.ToPedometer -> navController.navigate("pedometer") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            NavEvent.ToInsights -> navController.navigate("insights") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            NavEvent.ToAddMeal -> navController.navigate("addMeal") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            NavEvent.ToProfile -> navController.navigate("profile") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }

                            is NavEvent.NavigateTo -> {
                                navController.navigate(event.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                }

                // Determine current route to conditionally show BottomNavigationBar
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Screens where the bottom bar should be visible
                val bottomBarRoutes = listOf("pedometer", "insights", "addMeal", "profile")

                Scaffold(
                    bottomBar = {
                        if (currentRoute in bottomBarRoutes) {
                            BottomNavigationBar(bottomNavItems, currentRoute, vm)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "loading",
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
                        popEnterTransition = { EnterTransition.None },
                        popExitTransition = { ExitTransition.None },
                    ) {
                        composable("loading") {
                            Text(
                                "RAHHHHHHHHH",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        composable("login") { LoginScreen(vm) }
                        composable("signup") { SignUpScreen(vm) }
                        composable("forgot") { PasswordScreen(vm) }
                        composable("pedometer") { Pedometer(vm) }
                        composable("insights") { InsightsScreen(vm) }
                        composable("addMeal") { AddMealScreen(vm) }
                        composable("profile") { Profile(vm) }
                    }
                }
            }
        }
    }
}





