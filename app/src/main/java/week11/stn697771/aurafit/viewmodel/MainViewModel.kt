package week11.stn697771.aurafit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.data.TodoRepository
import week11.stn697771.aurafit.model.TodoItem

/*
The MainViewModel serves as the central hub for UI-related logic and state management for the main screens
of your application, specifically handling user authentication and the display/management of to-do items.
It acts as an intermediary between the UI (MainActivity) and the data layer (TodoRepository).
 */

class MainViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val repo = TodoRepository()

    // UiState for login/auth
    // Tracks whether the user is authenticated, or requires authentication, or is in a loading state.
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    // Todos
    // Holds the current list of TodoItems for the authenticated user.
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos

    // Error message (for Snackbar)
    // Provides a mechanism to display transient messages or errors to the user (e.g., in a Snackbar).
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        // Observe FirebaseAuth state
        // Sets up an addAuthStateListener to automatically update the _uiState when the user's authentication status changes (login, logout). When a user logs in, it also triggers the observation of their to-do list.
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                _uiState.value = UiState.AuthRequired
                _todos.value = emptyList()
            } else {
                _uiState.value = UiState.Authenticated
                observeTodoList()
            }
        }
    }

    // login Firebase function
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _uiState.value = UiState.Authenticated }
            .addOnFailureListener { e ->
                _uiState.value = UiState.AuthRequired
                _message.value = e.localizedMessage ?: "Login failed"
            }
    }

    // signUp Firebase function
    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { _uiState.value = UiState.Authenticated }
            .addOnFailureListener { e ->
                _uiState.value = UiState.AuthRequired
                _message.value = e.localizedMessage ?: "Sign up failed"
            }
    }

    // logOut Firebase function
    fun logout() {
        auth.signOut()
        _uiState.value = UiState.AuthRequired
    }

    // observe TodoList content
    // This private function is called when a user authenticates. It launches a coroutine in viewModelScope
    // to collect the Flow<List<TodoItem>> provided by repo.getTodos().
    private fun observeTodoList() {
        viewModelScope.launch {
            repo.getTodos().collect { list ->
                _todos.value = list
            }
        }
    }

    // Delegates the task of adding a new to-do item to the repo.addTodoItem().
    // This operation is also launched in a viewModelScope coroutine.
    fun addTodo(content: String, urgent: Boolean) {
        viewModelScope.launch {
            repo.addTodoItem(TodoItem(content, urgent))// using method from class TodoRepository
        }
    }

// Provides a way for the UI to clear any displayed error messages once they have been shown to the user.
    fun clearMessage() {
        _message.value = null
    }
}