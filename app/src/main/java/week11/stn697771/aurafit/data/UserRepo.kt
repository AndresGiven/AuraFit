package week11.stn697771.aurafit.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
}

