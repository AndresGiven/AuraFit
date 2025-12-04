package week11.stn697771.aurafit.data

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.squareup.moshi.JsonClass
import week11.stn697771.aurafit.ui.theme.NutrientColors
import week11.stn697771.aurafit.viewmodel.MainViewModel

@JsonClass(generateAdapter = true)
data class NutritionGuess(
    val recipesUsed: Int,
    val calories: NutrientInfo,
    val fat: NutrientInfo,
    val protein: NutrientInfo,
    val carbs: NutrientInfo
)

@JsonClass(generateAdapter = true)
data class NutrientInfo(
    val value: Double,
    val unit: String,
    val confidenceRange95Percent: ConfidenceRange,
    val standardDeviation: Double
)

@JsonClass(generateAdapter = true)
data class ConfidenceRange(
    val min: Double,
    val max: Double
)

data class SavedMeal(
    val name: String,
    val calories: Double,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
    val createdAt: Timestamp = Timestamp.now(),
    @DocumentId
    val id: String = "",
)

sealed class Macro(
    val label: String,
    val showUnit: Boolean,
    val colorKey: (NutrientColors) -> Color,
    val valueProvider: (MainViewModel) -> Double?
) {
    object Protein : Macro(
        label = "Protein",
        showUnit = true,
        colorKey = { it.proteinLight },
        valueProvider = { it.nutrition.value?.protein?.value }
    )

    object Fat : Macro(
        label = "Fat",
        showUnit = true,
        colorKey = { it.fatLight },
        valueProvider = { it.nutrition.value?.fat?.value }
    )

    object Carbs : Macro(
        label = "Carbs",
        showUnit = true,
        colorKey = { it.carbLight },
        valueProvider = { it.nutrition.value?.carbs?.value }
    )

    object Calories : Macro(
        label = "Calories",
        showUnit = false,
        colorKey = { it.calLight },
        valueProvider = { it.nutrition.value?.calories?.value }
    )
}
