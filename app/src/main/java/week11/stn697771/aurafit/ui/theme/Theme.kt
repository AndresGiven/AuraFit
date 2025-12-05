package week11.stn697771.aurafit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = AuraPrimary,
    onPrimary = Color.White,

    secondary = AuraSecondary,
    onSecondary = Color.White,

    tertiary = AuraAccent,
    onTertiary = Color.White,

    background = AuraBackground,
    onBackground = Color.White,

    surface = AuraBackground,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = AuraPrimary,
    onPrimary = Color.White,

    secondary = AuraSecondary,
    onSecondary = Color.White,

    tertiary = AuraAccent,
    onTertiary = Color.White,

    background = AuraBackground,
    onBackground = Color.White,

    surface = AuraBackground,
    onSurface = Color.White,
)

@Composable
fun AuraFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColorScheme
            else LightColorScheme
        }

    val nutrientColors = NutrientColors(
        protein = Protein,
        proteinLight = ProteinLight,
        proteinLightest = ProteinLightest,
        carb = Carb,
        carbLight = CarbLight,
        carbLightest = CarbLightest,
        fat = Fat,
        fatLight = FatLight,
        fatLightest = FatLightest,
        cal = Cal,
        calLight = CalLight,
        calLightest = CalLightest,
        step = Step,
        emptyProgress = EmptyProgress,
        disabled = Disabled
    )

    CompositionLocalProvider(
        LocalNutrientColors provides nutrientColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}