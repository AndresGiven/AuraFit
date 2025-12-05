package week11.stn697771.aurafit.viewmodel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.stn697771.aurafit.R
import android.graphics.Paint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.CornerRadius

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Insights(vm: MainViewModel) {
    val scrollState = rememberScrollState()
    val steps = vm.weeklySteps.collectAsState()
    Log.d("MyLog", "LOOK HERE PLEASE:  ${steps.toString()}")

    LaunchedEffect(Unit) {
        vm.loadWeeklySteps()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Spacer(modifier = Modifier.height(20.dp))

        val testValues5 = listOf(100f, 8000f, 300f, 4000f, 6000f, 50f, 7600f)
        Spacer(modifier = Modifier.height(20.dp))
        WeeklyBarChart(values = steps.value)
    }
}

@Composable
fun WeeklyBarChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = Color.White,
    axisColor: Color = Color.White
) {
    val maxValue = 10000
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("STEPS", fontWeight = FontWeight.Bold)
            Text("${values.sum().toInt()}", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold)
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(16.dp)
            ) {
                val chartWidth = size.width
                val chartHeight = size.height

                val leftPadding = 40f

                val contentWidth = chartWidth - leftPadding

                val spacing = contentWidth * 0.005f
                val totalSpacing = spacing * (values.size + 1)
                val barWidth = (contentWidth - totalSpacing) / values.size
                val axisStroke = Stroke(width = 8f)

                drawLine(
                    color = axisColor,
                    start = Offset(leftPadding, 0f),
                    end = Offset(leftPadding, chartHeight),
                    strokeWidth = axisStroke.width
                )

                drawLine(
                    color = axisColor,
                    start = Offset(leftPadding, chartHeight),
                    end = Offset(chartWidth, chartHeight),
                    strokeWidth = axisStroke.width
                )

                values.forEachIndexed { index, value ->
                    val barHeight = (value / maxValue) * (chartHeight - 30f)

                    val x = leftPadding + spacing + index * (barWidth + spacing)
                    val y = chartHeight - barHeight

                    val cornerRadius = CornerRadius(25f, 25f)

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = cornerRadius
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        days[index],
                        x + barWidth / 2,
                        chartHeight + 32f,
                        Paint().apply {
                            textAlign = Paint.Align.CENTER
                            textSize = 32f
                            color = android.graphics.Color.WHITE
                        }
                    )
                }

                val stepsHeight = chartHeight / 5f
                for (i in 0..5) {
                    val y = chartHeight - (i * stepsHeight)

                    val labelValue = (maxValue / 5 * i).toString()

                    drawContext.canvas.nativeCanvas.drawText(
                        labelValue,
                        leftPadding - 12f,
                        y,
                        Paint().apply {
                            textSize = 28f
                            textAlign = Paint.Align.RIGHT
                            color = android.graphics.Color.WHITE
                        }
                    )
                }
            }
        }
    }
}



