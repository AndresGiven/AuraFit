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
    val goal = 10000



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
        TopMeasurementBar()
        Spacer(modifier = Modifier.height(36.dp))
        CutCircularPedometer(progress = steps.toFloat()/goal, steps = steps, goal = goal)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBars()
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { vm.logout() }) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(20.dp)) // optional bottom spacing
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
                imageVector = Icons.Filled.Whatshot,
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
                imageVector = Icons.Filled.Schedule,
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
    steps: Int,
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

//// Placeholder screens
@Composable
fun InsightsScreen(vm: MainViewModel){
    Text("INSIGHTS SCREEN", color = Color.Black)
}

@Composable
fun AddMealScreen(vm: MainViewModel){
    Text("ADD MEAL SCREEN", color = Color.Black)
}

@Composable
fun Profile(vm: MainViewModel) {
    val scrollState = rememberScrollState()

    // Expansion states
    var proteinExpanded by remember { mutableStateOf(false) }
    var carbsExpanded by remember { mutableStateOf(false) }
    var fatsExpanded by remember { mutableStateOf(false) }
    var caloriesExpanded by remember { mutableStateOf(false) }

    val stepsGoal by vm.stepsGoal
    val proteinGoal by vm.proteinGoal
    val carbsGoal by vm.carbsGoal
    val fatsGoal by vm.fatsGoal
    val caloriesGoal by vm.caloriesGoal

    var onSave by remember { mutableStateOf<(String) -> Unit>({}) }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1F29))
            .padding(16.dp)
    ) {

        Surface(
            color = Color(0xFF262837),
            shape = RoundedCornerShape(30.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "GOALS",
                        fontWeight = FontWeight.ExtraBold,
                        style = androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.Underline),
                        fontSize = 28.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Steps",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(stepsGoal, color = Color.White, modifier = Modifier.padding(end = 8.dp))
                    IconButton(
                        onClick = { vm.editGoal("Edit Steps", stepsGoal) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    drawLine(
                        color = Color.Green, //set color
                        start = Offset(x = 0f, y = canvasHeight / 2),
                        end = Offset(x = canvasWidth, y = canvasHeight / 2),
                        strokeWidth = 5f
                    )

                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { proteinExpanded = !proteinExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Protein",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(proteinGoal, color = Color.White, modifier = Modifier.padding(end = 8.dp))
                    IconButton(
                        onClick = { vm.editGoal("Edit Protein", proteinGoal) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    }
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    drawLine(
                        color = Color.Blue, //set color
                        start = Offset(x = 0f, y = canvasHeight / 2),
                        end = Offset(x = canvasWidth, y = canvasHeight / 2),
                        strokeWidth = 5f
                    )

                }


                if (proteinExpanded) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3B3D4D), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            "Protein helps build and repair muscle, supports your\n" +
                                    "immune system, and keeps you feeling full after\n" +
                                    "meals.\n" +
                                    "Needs depend on level of activity:\n" +
                                    "Light activity: ~0.8-1.0g per kg of body weight\n" +
                                    "Moderate activity: ~1.0-1.2g/kg\n" +
                                    "heavy activity: ~1.2-1.8/kg",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { carbsExpanded = !carbsExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Carbohydrates",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(carbsGoal, color = Color.White, modifier = Modifier.padding(end = 8.dp))
                    IconButton(
                        onClick = { vm.editGoal("Edit Carbs", carbsGoal) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    }
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    drawLine(
                        color = Color(0xFF800080), //set color
                        start = Offset(x = 0f, y = canvasHeight / 2),
                        end = Offset(x = canvasWidth, y = canvasHeight / 2),
                        strokeWidth = 5f
                    )

                }


                if (carbsExpanded) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3B3D4D), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            "Carbs are your body’s main energy source. They\n" +
                                    "fuel your movements and brain function.\n" +
                                    "Goals depend on your overall caloric needs.\n" +
                                    "Light activity → low carb target\n" +
                                    "High activity → high carb target\n" +
                                    "Most balanced diets allocate ~40-50% of daily\n" +
                                    "calories to carbohydrates.",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { fatsExpanded = !fatsExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Fats",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(fatsGoal, color = Color.White, modifier = Modifier.padding(end = 8.dp))
                    IconButton(
                        onClick = { vm.editGoal("Edit Fats", fatsGoal) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    }
                }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    drawLine(
                        color = Color.Red, //set color
                        start = Offset(x = 0f, y = canvasHeight / 2),
                        end = Offset(x = canvasWidth, y = canvasHeight / 2),
                        strokeWidth = 5f
                    )

                }


                if (fatsExpanded) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3B3D4D), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            "Fats support hormone production, brain function,\n" +
                                    "vitamin absorption and long-lasting energy.\n" +
                                    " Fat is calorie-dense and goals must be set \n" +
                                    "accordingly.\n" +
                                    "A Typical healthy range is ~20-35% of daily calories\n" +
                                    "from fat",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { caloriesExpanded = !caloriesExpanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Calories",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(caloriesGoal, color = Color.White, modifier = Modifier.padding(end = 8.dp))
                    IconButton(
                        onClick = { vm.editGoal("Edit Calories", caloriesGoal) },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    }
                }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height


                    drawLine(
                        color = Color(0xFFFFA500), //set color
                        start = Offset(x = 0f, y = canvasHeight / 2),
                        end = Offset(x = canvasWidth, y = canvasHeight / 2),
                        strokeWidth = 5f
                    )

                }


                if (caloriesExpanded) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF3B3D4D), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            "Calories measure the total energy your body uses\n" +
                                    "each day. To lose weight, burn more calories than\n" +
                                    "you consume, to gain weight, ingest more calories\n" +
                                    "than you burn.\n" +
                                    "Higher activity → higher daily calorie needs\n" +
                                    "Lower activity → lower daily calorie needs",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "INFO",
                        style = androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.Underline),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                InfoRow("Age", "28")
                InfoRow("Height", "181cm")
                InfoRow("Weight", "91kg")

                Spacer(modifier = Modifier.height(20.dp))
                if (vm.showDialog.value) {
                    EditableValueDialog(
                        title = vm.dialogTitle.value,
                        initialValue = vm.dialogValue.value,
                        onSave = { newValue ->
                            vm.onDialogSave(newValue) // updates VM state directly
                        },
                        onCancel = { vm.onDialogCancel() }
                    )
                }



            }
            }
    }
}




@Composable
fun ExpandableGoalRow(
    title: String,
    value: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    description: String
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        ProfileGoalRow(title, value, onEdit = {})
        if (expanded) {
            Spacer(Modifier.height(6.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3B3D4D), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(description, color = Color.White, fontSize = 13.sp)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Text(
        text = "$label: $value",
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}


@Composable
fun ProfileGoalRow(label: String, value: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            value,
            color = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )

        IconButton(
            onClick = { onEdit() },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
        }
    }
}


@Composable
fun EditableValueDialog(
    title: String,
    initialValue: String,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title, color = Color.White) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(text) }) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = Color.White)
            }
        },
        containerColor = Color(0xFF2D2F3A)
    )
}









