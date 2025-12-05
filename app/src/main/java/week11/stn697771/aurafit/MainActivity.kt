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

// --- ADDED IMPORTS FOR PEDOMETER PERMISSION ---
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import week11.stn697771.aurafit.viewmodel.Insights

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            println(" Activity Recognition permission GRANTED!")
        } else {
            println("Activity Recognition permission DENIED!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    println("Permission already granted")
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                }
            }
        }
        checkActivityRecognitionPermission() // <-- already present
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
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
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
                                "Loading...",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        composable("login") { LoginScreen(vm) }
                        composable("signup") { SignUpScreen(vm) }
                        composable("forgot") { PasswordScreen(vm) }
                        composable("pedometer") { Pedometer(vm) }
                        composable("insights") { Insights(vm) }
                        composable("addMeal") { AddMealScreen(vm) }
                        composable("profile") { Profile(vm) }
                    }
                }
            }
        }

    }

    // --- ADDED FUNCTIONS FOR PEDOMETER PERMISSION ---
    private fun checkActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    101
                )
            } else {

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied. Pedometer won't work.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
