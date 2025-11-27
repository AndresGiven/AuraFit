package week11.stn697771.aurafit


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import week11.stn697771.aurafit.ui.theme.AuraFitTheme
import week11.stn697771.aurafit.util.NavEvent
import week11.stn697771.aurafit.util.UiState
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
                        }
                    }
                }
                // NavHost handles all screens
                NavHost(
                    navController = navController,
                    startDestination = "loading",
                    enterTransition = { EnterTransition.None},
                    exitTransition = { ExitTransition.None},
                    popEnterTransition = { EnterTransition.None},
                    popExitTransition = { ExitTransition.None},

                ) {
                    composable("loading") { Text("RAHHHHHHHHH", color = MaterialTheme.colorScheme.primary) }

                    composable("login") { LoginScreen(vm) }

                    composable("signup") { SignUpScreen(vm) }

                    composable("forgot") { PasswordScreen(vm) }

                    composable("pedometer") { Pedometer(vm) }
                }



//                when (uiState) {
//                    UiState.Loading -> Text("Loading...")
//                    UiState.Unauthenticated -> LoginScreen(vm)
//                    UiState.Authenticated -> Pedometer(vm)
//                    UiState.AuthSetup -> SignUpScreen(vm)
//                    UiState.AuthForgot -> PasswordScreen(vm)
//                }
            }
        }
    }
}




