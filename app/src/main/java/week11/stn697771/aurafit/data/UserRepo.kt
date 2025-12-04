package week11.stn697771.aurafit.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.stn697771.aurafit.model.Meal
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

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
    suspend fun saveSteps(steps: Int) {
        val user = auth.currentUser ?: return

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val stepData = mapOf(
            "steps" to steps,
            "timestamp" to Timestamp.now()
        )

        db.collection("users")
            .document(user.uid)
            .collection("pedometer")
            .document(today)
            .set(stepData)
            .await()

    }
    suspend fun getSteps(): Int {
        val user = auth.currentUser ?: return 0

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return try {
            val docSnapshot = db.collection("users")
                .document(user.uid)
                .collection("pedometer")
                .document(today)
                .get()
                .await()

            docSnapshot.getLong("steps")?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }


    suspend fun getStepsForDay(dateId: String): Float? {
        Log.d("MyLog", "Getting steps for date: $dateId")
        val user = auth.currentUser ?: return null
        return try {
            val snap = db.collection("users")
                .document(user.uid)
                .collection("pedometer")
                .document(dateId)
                .get()
                .await()

            snap.getLong("steps")?.toFloat()

        } catch (e: Exception) {
            null
        }
    }
}

