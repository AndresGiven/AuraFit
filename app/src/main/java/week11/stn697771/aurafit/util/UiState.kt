package week11.stn697771.aurafit.util

//# Sealed class for Loading/AuthRequired/Authenticated
sealed class UiState {
    object Loading : UiState()
    object AuthRequired : UiState()
    object AuthSetup : UiState()

    object AuthForgot : UiState()
    object Authenticated : UiState()

}