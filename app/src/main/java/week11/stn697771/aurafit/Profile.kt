package week11.stn697771.aurafit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.stn697771.aurafit.ui.theme.LocalNutrientColors
import week11.stn697771.aurafit.viewmodel.MainViewModel

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

                PersonalInfoRow("Age", vm.age.value) { vm.editPersonalInfo("Age", vm.age.value) }
                PersonalInfoRow("Height", vm.height.value) { vm.editPersonalInfo("Height", vm.height.value) }
                PersonalInfoRow("Weight", vm.weight.value) { vm.editPersonalInfo("Weight", vm.weight.value) }


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
                Button(onClick = { vm.logout() }) {
                    Text("Logout")
                }


            }
        }
    }
}


@Composable
fun PersonalInfoRow(label: String, value: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // keeps Text and IconButton at ends
    ) {
        Text(
            text = "$label: $value",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(
            onClick = onEdit,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit $label", tint = Color.White)
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