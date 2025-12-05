package week11.stn697771.aurafit.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


data class NutrientColors(
    val protein: Color,
    val proteinLight: Color,
    val proteinLightest: Color,

    val carb: Color,
    val carbLight: Color,
    val carbLightest: Color,

    val fat: Color,
    val fatLight: Color,
    val fatLightest: Color,

    val cal: Color,
    val calLight: Color,
    val calLightest: Color,

    val step: Color,

    val emptyProgress: Color,
    val disabled: Color,
)

val LocalNutrientColors = staticCompositionLocalOf {
    NutrientColors(
        Protein, ProteinLight, ProteinLightest,
        Carb, CarbLight, CarbLightest,
        Fat, FatLight, FatLightest,
        Cal, CalLight, CalLightest,
        Step, EmptyProgress, Disabled
    )
}
