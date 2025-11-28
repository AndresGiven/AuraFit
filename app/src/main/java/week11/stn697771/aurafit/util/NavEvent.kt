package week11.stn697771.aurafit.util

sealed interface NavEvent {
    object ToLogin : NavEvent
    object ToSignUp : NavEvent
    object ToForgot : NavEvent
    object ToPedometer : NavEvent
    object ToInsights : NavEvent
    object ToAddMeal : NavEvent
    object ToProfile : NavEvent
    data class NavigateTo(val route: String) : NavEvent

}