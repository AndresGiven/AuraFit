package week11.stn697771.aurafit.data

import com.squareup.moshi.JsonClass

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
