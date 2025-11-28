package week11.stn697771.aurafit.model

import com.google.firebase.firestore.DocumentId

data class Meal (
    val meal: String = "",
    val userEmail: String? = null,

    @DocumentId
    val id: String = "",
)