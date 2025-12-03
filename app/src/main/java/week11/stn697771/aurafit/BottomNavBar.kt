package week11.stn697771.aurafit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import week11.stn697771.aurafit.data.NutritionGuess
import week11.stn697771.aurafit.util.NavEvent
import week11.stn697771.aurafit.viewmodel.MainViewModel


sealed class BottomNavScreen(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavScreen("pedometer", Icons.Default.Home, "Home")
    object Insights : BottomNavScreen("insights", Icons.AutoMirrored.Filled.ShowChart, "Insights")
    object AddMeal : BottomNavScreen("addMeal", Icons.Default.Add, "Add Meal")
    object Profile : BottomNavScreen("profile", Icons.Default.AccountCircle, "Profile")
}

val bottomNavItems = listOf(
    BottomNavScreen.Home,
    BottomNavScreen.Insights,
    BottomNavScreen.AddMeal,
    BottomNavScreen.Profile
)


@Composable
fun BottomNavigationBar(
    items: List<BottomNavScreen>,
    currentRoute: String?,
    vm: MainViewModel,
) {
    var showDialog by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    var step by remember { mutableIntStateOf(1) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(100.dp))
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label, modifier = Modifier.size(32.dp)) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    indicatorColor = MaterialTheme.colorScheme.tertiary
                ),
                onClick = {
                    if(screen.route == "addMeal"){
                        showDialog = true
                        step = 1
                        //vm.guessNutrition("Alfredo")
                    }
                    else if (currentRoute != screen.route) {
                        vm.navigate(NavEvent.NavigateTo(screen.route))
                    }
                }
            )
        }
    }

    if (showDialog) {
        when (step){
            1 -> AddMeal(
                textInput = textInput,
                onTextChange = { textInput = it },
                onDismiss = { showDialog = false },
                vm = vm,
                step = { step = 2},
            )
            2 -> MacrosDialog(
                vm,
                onDismiss = { showDialog = false },
            )
        }
    }
}



@Composable
fun BottomNavBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        BottomNavButton(
            icon = Icons.Default.Home,
            contentDescription = "Home",
            onClick = { }
        )

        BottomNavButton(
            icon = Icons.AutoMirrored.Filled.ShowChart,
            contentDescription = "Graph",
            onClick = { }
        )

        BottomNavButton(
            icon = Icons.Default.Add,
            contentDescription = "Add button",
            onClick = {  }
        )
        BottomNavButton(
            icon = Icons.Default.AccountCircle,
            contentDescription = "Account Icon",
            onClick = {  }
        )
    }
}

@Composable
fun BottomNavButton(
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}
