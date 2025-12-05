package week11.stn697771.aurafit

import android.R.attr.dialogTitle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import week11.stn697771.aurafit.ui.theme.LocalNutrientColors
import week11.stn697771.aurafit.viewmodel.MainViewModel
import java.time.format.TextStyle

@Composable
fun Pedometer(vm: MainViewModel) {
    val scrollState = rememberScrollState()
    val steps by vm.steps.collectAsState()
    val goal = vm.stepsGoal



    DisposableEffect(key1 = vm) {
        vm.startStepTracking()
        onDispose {
            vm.stopStepTracking()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        TopMeasurementBar(vm)
        Spacer(modifier = Modifier.height(36.dp))
        CutCircularPedometer(steps = steps, goal = goal)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBars(vm)
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TopMeasurementBar(vm: MainViewModel) {
    val steps by vm.steps.collectAsState()
    val calories = steps * 0.04
    val distance = steps * 0.75
    val kmDistance = distance / 1000
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
                imageVector = Icons.Filled.Whatshot,
                contentDescription = "Fire Symbol",
                tint = Color.White
            )
            Text(
                text = "$calories kcal",
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
                text = "$kmDistance KM",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }

}

@Composable
fun CutCircularPedometer(
    modifier: Modifier = Modifier,
    steps: Int,
    goal: MutableState<String>,
) {
    // Convert goal string to Float safely
    val goalFloat = goal.value.toFloatOrNull() ?: 1f
    val progress = (steps.toFloat() / goalFloat).coerceIn(0f, 1f)


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

    Box(
        modifier = modifier.size(310.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 36.dp.toPx()

            drawArc(
                color = colors.emptyProgress,
                startAngle = startAngle,
                sweepAngle = totalSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(gradientColors),
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }
        Canvas(
            modifier = Modifier
                .size(255.dp)
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
                text = "Goal: ${goal.value}", // display actual goal string
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
                // handle click
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
fun ProgressBars(vm: MainViewModel) {
    val proteinGoalString by vm.proteinGoal
    val carbsGoalString by vm.carbsGoal
    val fatsGoalString by vm.fatsGoal
    val caloriesGoalString by vm.caloriesGoal

    val proteinGoal = proteinGoalString.toFloatOrNull() ?: 1f
    val carbsGoal = carbsGoalString.toFloatOrNull() ?: 1f
    val fatsGoal = fatsGoalString.toFloatOrNull() ?: 1f
    val caloriesGoal = caloriesGoalString.toFloatOrNull() ?: 1f

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
        NutritionProgressItem("Protein", 59f, proteinGoal, "g",colors.protein)
        NutritionProgressItem("Carbs", 163f, carbsGoal, "g", colors.carb)
        NutritionProgressItem("Fat", 55f, fatsGoal, "g",colors.fat)
        NutritionProgressItem("Cals", 1213f, caloriesGoal, "g",colors.cal)
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
fun AddMealScreen(vm: MainViewModel){
    Text("ADD MEAL SCREEN", color = Color.Black)
}









