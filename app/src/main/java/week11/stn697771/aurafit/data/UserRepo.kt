package week11.stn697771.aurafit.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.stn697771.aurafit.model.Meal

/*
The UserRepo class is responsible for handling all interactions with Firebase
for managing a user's data. It acts as an abstraction layer between the rest of application
(e.g., our ViewModel) and the Firebase backend (Authentication and Firestore database)
 */
class UserRepo {
    // get instance of Firebase
    private val auth = FirebaseAuth.getInstance()

    // get instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    suspend fun addMealItem(item: SavedMeal) {
        // It retrieves the currently authenticated user. If no user is logged in, it returns
        val user = auth.currentUser ?: return

        // it stores the item in a specific path in Firestore: users/{user.uid}/meals/.
        db.collection("users")
            .document(user.uid)
            .collection("meals")
            .add(item)
            .await()
    }
}

