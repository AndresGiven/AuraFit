package week11.stn697771.aurafit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.data.UserRepo
import week11.stn697771.aurafit.model.Meal
import week11.stn697771.aurafit.util.NavEvent

/*
The MainViewModel serves as the central hub for UI-related logic and state management for the main screens
of your application, specifically handling user authentication and the display/management of to-do items.
It acts as an intermediary between the UI (MainActivity) and the data layer (UserRepo).
 */

class MainViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val repo = UserRepo()

    // UiState for login/auth
    // Tracks whether the user is authenticated, or requires authentication, or is in a loading state.
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _navEvents = MutableSharedFlow<NavEvent>()
    val navEvents: MutableSharedFlow<NavEvent> = _navEvents


    // Error message (for Snackbar)
    // Provides a mechanism to display transient messages or errors to the user (e.g., in a Snackbar).
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        // Observe FirebaseAuth state
        // Sets up an addAuthStateListener to automatically update the _uiState when the user's authentication status changes (login, logout).
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                _uiState.value = UiState.Unauthenticated
                sendEvent(NavEvent.ToLogin)
            } else {
                _uiState.value = UiState.Authenticated
                sendEvent(NavEvent.ToPedometer)
            }
        }
    }

    // login Firebase function
    fun login(email: String, password: String) {
        //Passwords got to be 6 characters at least
        if (password.length < 6) {
            _message.value = "Password must be at least 6 characters."
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _uiState.value = UiState.Authenticated
                sendEvent(NavEvent.ToPedometer)
            }
            .addOnFailureListener { e ->
                _uiState.value = UiState.Unauthenticated
                sendEvent(NavEvent.ToLogin)
                _message.value = e.localizedMessage ?: "Login failed"
            }
    }

    // signUp Firebase function
    fun signUp(email: String, password: String) {
        //Passwords got to be 6 characters at least
        if (password.length < 6) {
            _message.value = "Password must be at least 6 characters."
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _uiState.value = UiState.Unauthenticated
                sendEvent(NavEvent.ToLogin)
            }
            .addOnFailureListener { e ->
                _uiState.value = UiState.Unauthenticated
                sendEvent(NavEvent.ToSignUp)
                _message.value = e.localizedMessage ?: "Sign up failed"
            }
    }

    // logOut Firebase function
    fun logout() {
        auth.signOut()
        _uiState.value = UiState.Unauthenticated
        sendEvent(NavEvent.ToLogin)
    }


     //Triggers Firebase to send a password reset email to the specified address.
     // This is a secure, standard, and free Firebase feature.
    fun sendPasswordReset(email: String) {
        _uiState.value = UiState.Loading

        if (email.isBlank()) {
            _message.value = "Email address cannot be empty."
            _uiState.value = UiState.Unauthenticated
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _message.value = "Password reset link sent to your email."
                } else {
                    _message.value = task.exception?.localizedMessage ?: "An unknown error occurred."
                }
                // Whether it succeeds or fails, always navigate back to the login screen afterwards
                _uiState.value = UiState.Unauthenticated
                sendEvent(NavEvent.ToLogin)
            }
    }

    fun saveMeal(meal: Meal){
        viewModelScope.launch {
            repo.addMealItem(meal)
        }
    }

// Provides a way for the UI to clear any displayed error messages once they have been shown to the user.
    fun changeUIState(state: UiState) {
        _uiState.value = state
    }

    //For use inside viewmodel
    private fun sendEvent(e: NavEvent) {
        viewModelScope.launch {
            _navEvents.emit(e)
        }
    }

    //Able to be called from outside
    fun navigate(event: NavEvent) {
        viewModelScope.launch { sendEvent(event) }
    }

}