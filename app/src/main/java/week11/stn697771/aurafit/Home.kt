package week11.stn697771.aurafit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import week11.stn697771.aurafit.ui.theme.LocalNutrientColors
import week11.stn697771.aurafit.viewmodel.BasicAlertDialogSample
import week11.stn697771.aurafit.viewmodel.MainViewModel

@Composable
fun Pedometer(vm: MainViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ){
            Image(
                painter = painterResource(id = R.drawable.thickemptylogo),
                contentScale = ContentScale.Inside,
                contentDescription = "Logo",
            )
            Text(
                text = "AuraFit",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        TopMeasurementBar()
        Spacer(modifier = Modifier.height(36.dp))
        CutCircularPedometer(progress = 1f)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBars()
        Spacer(modifier = Modifier.height(16.dp))
        BottomNavBar()
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { vm.logout() }) {
            Text("Logout")
        }
    }
}

@Composable
fun TopMeasurementBar() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = "Fire Symbol",
                tint = Color.White
            )
            Text(
                text = "330 kcal",
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                contentDescription = "Walking dude",
                tint = Color.White
            )
            Text(
                text = "5.4 KM",
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Time Clock",
                tint = Color.White
            )
            Text(
                text = "1.2 Hours",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }

}

@Composable
fun CutCircularPedometer(
    modifier: Modifier = Modifier,
    progress: Float,
    steps: Int = 6543,
    goal: Int = 10000
) {
    val auraPrimary = MaterialTheme.colorScheme.primary
    val colors = LocalNutrientColors.current
    val gradientColors = listOf(
        Color(0xFF55FE2A),
        Color(0xFF10C5DE),
        Color(0xFF4638F2),
        Color(0xFF4638F2),
        Color(0xFFAD20B4),
        Color(0xFFED1D5E),
        Color(0xFFFD8D02),
        Color(0xFFD6CC08),
        Color(0xFF55FE2A),
    )

    val totalSweep = 260f
    val startAngle = 140f
    val progressSweep = progress * totalSweep
    val progressSweepTEMP = progressSweep - 90f

    Box(
        modifier = modifier.size(310.dp), //change this for outer ring
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 36.dp.toPx()

            drawArc(
                color = colors.emptyProgress,
                startAngle = startAngle,
                sweepAngle = totalSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
            )

            drawArc(
                brush = Brush.sweepGradient(gradientColors),
                startAngle = startAngle,
                sweepAngle = progressSweepTEMP,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }
        Canvas(
            modifier = Modifier
                .size(255.dp) //change this for inner CIRCLE
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = auraPrimary,
                radius = size.minDimension / 2
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%,d".format(steps),
                fontSize = 46.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Goal: ${"%,d".format(goal)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            CircularButton {
                // adding on click code later
            }
        }
    }
}

@Composable
fun CircularButton(
    onClick: () -> Unit,
) {
    val colors = LocalNutrientColors.current
    var pausePlay by remember { mutableStateOf(false) } // State to track if steps are tracked

    IconButton(
        onClick = { pausePlay = !pausePlay },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.protein,
            contentColor = Color.White
        ),
        modifier = Modifier.size(82.dp),

        ) {
        Icon(
            modifier = Modifier.size(58.dp),
            imageVector = if (pausePlay) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Basically swaps from pause to play with a click",
        )
    }
}

@Composable
fun ProgressBars() {
    val colors = LocalNutrientColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NutritionProgressItem("Protein", 59f, 90f, "g",colors.protein)
        NutritionProgressItem("Carbs", 163f, 310f, "g", colors.carb)
        NutritionProgressItem("Fat", 55f, 70f, "g",colors.fat)
        NutritionProgressItem("Cals", 1213f, 1800f, "",colors.cal)
    }
}

@Composable
fun NutritionProgressItem(
    label: String,
    value: Float,
    goal: Float,
    unit: String = "g",
    color: Color = Color(0xFFFFC107)
) {
    val colors = LocalNutrientColors.current
    val progress = (value / goal).coerceIn(0f, 1f)

    Column(modifier = Modifier.fillMaxWidth()) {

        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(colors.emptyProgress)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${value.toInt()}$unit / ${goal.toInt()}$unit",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Edit",
                tint = Color.White,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.End)
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


//Used from https://m3.material.io/components/progress-indicators/overview
@Composable
fun LinearDeterminateIndicator() {
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            loading = true
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false // Reset loading when the coroutine finishes
            }
        }, enabled = !loading) {
            Text("Start loading")
        }

        if (loading) {
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}