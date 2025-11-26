package week11.stn697771.aurafit.model


// data class to represent an item
data class TodoItem(
    val content: String = "",
    val urgent: Boolean = false,
    //this email will be copied from user login
    val userEmail: String? = null
)