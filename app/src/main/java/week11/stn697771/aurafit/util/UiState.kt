package week11.stn697771.aurafit.util

//# Sealed class for Loading/Unauthenticated/Authenticated
sealed class UiState {
    object Loading : UiState()
    object Unauthenticated : UiState()
    object Authenticated : UiState()

    //    object AuthSetup : UiState()
    //    object AuthForgot : UiState()

}