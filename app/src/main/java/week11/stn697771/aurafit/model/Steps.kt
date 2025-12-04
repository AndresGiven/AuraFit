package week11.stn697771.aurafit.model

import com.google.firebase.firestore.DocumentId

data class Steps (
    val steps: Int = 0,
    val userEmail: String? = null,
    val timestamp: String? = null,

    @DocumentId
    val id: String = "",
)
