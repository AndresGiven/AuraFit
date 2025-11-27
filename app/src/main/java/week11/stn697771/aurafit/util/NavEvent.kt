package week11.stn697771.aurafit.util

sealed interface NavEvent {
    object ToLogin : NavEvent
    object ToSignUp : NavEvent
    object ToForgot : NavEvent
    object ToPedometer : NavEvent
}