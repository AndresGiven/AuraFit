package week11.stn697771.aurafit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import week11.stn697771.aurafit.data.NutritionGuess
import week11.stn697771.aurafit.model.Meal
import week11.stn697771.aurafit.ui.theme.LocalNutrientColors
import week11.stn697771.aurafit.ui.theme.NutrientColors
import week11.stn697771.aurafit.viewmodel.MainViewModel

@Composable
fun AddMeal(
    textInput: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    vm: MainViewModel,
    step: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 24.dp, 24.dp, 4.dp) // space below the top-right close button
                ) {
                    Text(
                        text = "Add a meal!",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .width(85.dp),
                        thickness = 2.dp,
                        color = Color.White,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = textInput,
                        onValueChange = onTextChange,
                        placeholder = {
                            Text(
                                "e.g. Chicken noodle soup...",
                                color = Color(0xFF90959E),
                                fontSize = 16.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp) // this actually rounds the corners
                    )

                    // Confirm button at center bottom
                    Button(
                        onClick = {
                            val meal = textInput.trim()
                            if (meal.isNotEmpty()) {
                                vm.guessNutrition(meal)
                                step()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                        ),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Macros",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MacrosDialog(
    vm: MainViewModel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Macros",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Calories: ${vm.nutrition.value?.calories?.value} ${vm.nutrition.value?.calories?.unit}")
                    Text("Carbs: ${vm.nutrition.value?.carbs?.value} ${vm.nutrition.value?.carbs?.unit}")
                    Text("Fat: ${vm.nutrition.value?.fat?.value} ${vm.nutrition.value?.fat?.unit}")
                    Text("Protein: ${vm.nutrition.value?.protein?.value} ${vm.nutrition.value?.protein?.unit}")
                }
            }
        }
    }
}




