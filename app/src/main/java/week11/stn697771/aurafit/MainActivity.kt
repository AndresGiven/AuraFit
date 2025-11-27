package week11.stn697771.aurafit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import week11.stn697771.aurafit.ui.theme.AuraFitTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val DarkThemeColor = Color(0xFF1B212A)
        enableEdgeToEdge()
        setContent {
            AuraFitTheme {
                Scaffold(
                    containerColor = DarkThemeColor,
                    content = { padding ->
                        Pedometer(modifier = Modifier.padding(padding))
                    }
                )
                }
            }
        }
    }



@Composable
fun Pedometer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Aura Fit",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        TopMeasurementBar()
        Spacer(modifier = Modifier.height(16.dp))
        CutCircularPedometer(progress = 1f)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBar()
        Spacer(modifier = Modifier.height(40.dp))
        BottomNavBar()
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

@Composable
fun CutCircularPedometer(
    progress: Float,
    steps: Int = 6543,
    goal: Int = 10000,
    modifier: Modifier = Modifier
) {
    val gradientColors = listOf(
        Color(0xFF6A00FF),
        Color(0xFFE91E63),
        Color(0xFFFF9800),
        Color(0xFFFFEB3B)
    )

    val totalSweep = 260f
    val startAngle = -220f
    val progressSweep = progress * totalSweep

    Box(
        modifier = modifier.size(250.dp), //change this for outer ring
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 22.dp.toPx()

            drawArc(
                color = Color(0xFFBDBDBD),
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
                .size(210.dp) //change this for inner ring
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = Color(0xFF2C3440),
                radius = size.minDimension / 2
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%,d".format(steps),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Goal: ${"%,d".format(goal)}",
                fontSize = 16.sp,
                color = Color.LightGray
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(Modifier.size(12.dp))

            CircularButton {
                // adding on click code later
            }
        }
    }
}

@Composable
fun TopMeasurementBar() {
    Row(
        modifier = Modifier.fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF2C3440))
            .padding(vertical = 10.dp),
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
                text = "330 k cal",
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
fun ProgressBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF2C3440))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Colored progress bar on top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(59f / 90f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Protein 59g / 90g",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(163f / 310f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Carbs 163g / 310g",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(55f / 70f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Fat 55g / 70g",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1213f / 1800f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Cals 1213 / 1800",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

    }
}


@Composable
fun CircularButton(
    onClick: () -> Unit,
    ) {
    var pausePlay by remember { mutableStateOf(false) } // State to track if music is playing
    IconButton(
        onClick = { pausePlay = !pausePlay },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color(0xFF0000FF ),
            contentColor = Color.White
        ),
        modifier = Modifier.size(68.dp),

    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = if (pausePlay) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Basically swaps from pause to play with a click",
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
            tint = Color.White
        )
    }
}

@Composable
fun BottomNavBar(){
    Row(
        modifier = Modifier.fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF2C3440)),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        BottomNavButton(
            icon = Icons.Default.Home,
            contentDescription = "Home",
            onClick = { }
        )

        BottomNavButton(
            icon = Icons.AutoMirrored.Filled.ShowChart,
            contentDescription = "Graph ",
            onClick = { }
        )

        BottomNavButton(
            icon = Icons.Default.AddAlarm,
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




