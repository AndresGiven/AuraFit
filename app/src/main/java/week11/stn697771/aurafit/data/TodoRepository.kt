package week11.stn697771.aurafit.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.stn697771.aurafit.model.TodoItem

/*
The TodoRepository class is responsible for handling all interactions with Firebase
for managing a user's to-do items. It acts as an abstraction layer between the rest of application
(e.g., our ViewModel) and the Firebase backend (Authentication and Firestore database)
 */
class TodoRepository {
    // get instance of Firebase
    private val auth = FirebaseAuth.getInstance()

    // get instance of Firestore
    private val db = FirebaseFirestore.getInstance()

    /*
    To add a new TodoItem to the current user's list of to-dos in Firestore
     */
    suspend fun addTodoItem(item: TodoItem) {
        // It retrieves the currently authenticated user. If no user is logged in, it returns
        val user = auth.currentUser ?: return

        // It creates a copy of the TodoItem and populates the userEmail field with the current user's email
        val itemWithEmail = item.copy(userEmail = user.email)
        // it stores the item in a specific path in Firestore: users/{user.uid}/todos/.
        db.collection("users")
            .document(user.uid)
            .collection("todos")
            .add(itemWithEmail)
        // It uses await() from kotlinx.coroutines.tasks to make the Firestore add operation a suspending function,
            // allowing for asynchronous handling
            .await()
    }

    /*
    To provide a real-time stream of the current user's to-do items from Firestore
     */
   fun getTodos(): Flow<List<TodoItem>> = callbackFlow {
        val user = auth.currentUser
        if (user == null) {
         //   if no user is logged in, it returns
            trySend(emptyList())  // sends an empty list to the Flow if no user is logged in
            close() // closes the Flow channel since there's nothing to observe
            return@callbackFlow // exits the callbackFlow block early (no active listener)
        }

        val reg = db.collection("users")
            .document(user.uid)
            .collection("todos")
            // the listener below trigger whenever there are changes in that collection
            .addSnapshotListener { snapshot, _ ->
            // when a snapshot is received, it converts the documents to TodoItem objects and emits the list of items through the Flow
                trySend(snapshot?.toObjects(TodoItem::class.java) ?: emptyList())
            }
       //ensures that the Firestore listener is properly unregistered when the Flow is no longer collected
        awaitClose { reg.remove() }
    }
}

