package week11.stn697771.aurafit.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.stn697771.aurafit.data.NutritionGuess
import week11.stn697771.aurafit.data.SavedMeal
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.data.UserRepo
import week11.stn697771.aurafit.model.Meal
import week11.stn697771.aurafit.model.Steps
import week11.stn697771.aurafit.util.NavEvent
import week11.stn697771.aurafit.network.SpoonacularService
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

/*
The MainViewModel serves as the central hub for UI-related logic and state management for the main screens
of your application, specifically handling user authentication and the display/management of to-do items.
It acts as an intermediary between the UI (MainActivity) and the data layer (UserRepo).
 */

class MainViewModel(application: Application) :
    AndroidViewModel(application), SensorEventListener {

    private val auth = FirebaseAuth.getInstance()
    private val repo = UserRepo()
    // UiState for login/auth
    // Tracks whether the user is authenticated, or requires authentication, or is in a loading state.
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState
    private val _navEvents = MutableSharedFlow<NavEvent>()
    val navEvents: MutableSharedFlow<NavEvent> = _navEvents

    //public because it must be mutable by the UI
    val nutrition = mutableStateOf<NutritionGuess?>(null)

    private val _weeklySteps = MutableStateFlow<List<Float>>(emptyList())
    val weeklySteps = _weeklySteps.asStateFlow()

    // Error message (for Snackbar)
    // Provides a mechanism to display transient messages or errors to the user (e.g., in a Snackbar).
    private val _message = MutableStateFlow<String?>(null)
    // Sensor Manager used to access the physical step counter sensor.
    private val sensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Exposed steps StateFlow used by the UI (Pedometer screen).
    private val _steps = MutableStateFlow(0)

    val steps: StateFlow<Int> = _steps
    // Stores the initial step count from the sensor so we can calculate relative steps.
    private var initialSteps: Float? = null
    private var hasInitialized = false  // Add this

    private var isTracking = false

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastStepTime = 0L

    private var lastSavedSteps = 0

    // MutableState to control dialog from VM
    val showDialog = mutableStateOf(false)
    val dialogTitle = mutableStateOf("")
    val dialogValue = mutableStateOf("")
    private var onSaveDialog: (String) -> Unit = {}

    val stepsGoal = mutableStateOf("10000")
    val proteinGoal = mutableStateOf("90")
    val carbsGoal = mutableStateOf("310")
    val fatsGoal = mutableStateOf("70")
    val caloriesGoal = mutableStateOf("1800")


    fun startStepTracking() {
        if (isTracking) {
            println("Already tracking, skipping registration")
            return
        }


        // Fallback to accelerometer-based step detection
        accelerometer?.let { sensor ->
            val success = sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
            if (success) {
                isTracking = true
                println("Using accelerometer for step detection")
            }
        } ?: run {
            viewModelScope.launch {
                isTracking = true
                while (isTracking) {
                    delay(1000)
                    _steps.value += 1
                }
            }
        }
    }

    fun stopStepTracking() {
        if (isTracking) {
            sensorManager.unregisterListener(this)
            isTracking = false
            println("Stopped tracking")
        }
    }


    fun simulateSteps(count: Int) {
        viewModelScope.launch {
            _steps.value += count
        }
    }



    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { e ->
            when (e.sensor.type) {

                Sensor.TYPE_STEP_COUNTER -> {
                    val totalSteps = e.values[0]
                    if (!hasInitialized) {
                        initialSteps = totalSteps
                        hasInitialized = true
                        println("INITIALIZED: initial=$initialSteps")
                    }
                    val currentSteps = (totalSteps - (initialSteps ?: 0f)).toInt()
                    _steps.value = currentSteps
                }

                Sensor.TYPE_ACCELEROMETER -> {
                    val x = e.values[0]
                    val y = e.values[1]
                    val z = e.values[2]

                    // magnitude
                    val magnitude = kotlin.math.sqrt((x*x + y*y + z*z).toDouble()).toFloat()

                    val now = System.currentTimeMillis()

                    // 300ms debounce: prevents more than 3 steps per second
                    if (now - lastStepTime < 300) return

                    // Filter real step spikes only
                    if (magnitude > 12.5f) {
                        lastStepTime = now
                        _steps.value += 1
                        println("STEP DETECTED via ACCELEROMETER. Total = ${_steps.value}")

                        if (_steps.value - lastSavedSteps >= 20) {
                            lastSavedSteps = _steps.value
                            saveStepsToFirebase()
                        }
                    }
                }
            }
        }
    }

    fun saveStepsToFirebase() {
        viewModelScope.launch {
            try {
                repo.saveSteps(_steps.value)
                println("SAVED TO FIREBASE: ${_steps.value}")
            } catch (e: Exception) {
                println("Failed to save steps: ${e.message}")
            }
        }
    }

    fun editGoal(goalTitle: String, currentValue: String) {
        dialogTitle.value = goalTitle
        dialogValue.value = currentValue
        onSaveDialog = { newValue ->
            when(goalTitle) {
                "Edit Steps" -> stepsGoal.value = newValue
                "Edit Protein" -> proteinGoal.value = newValue
                "Edit Carbs" -> carbsGoal.value = newValue
                "Edit Fats" -> fatsGoal.value = newValue
                "Edit Calories" -> caloriesGoal.value = newValue
            }

            // Save permanent goal to Firebase
            viewModelScope.launch {
                val goalKey = when(goalTitle) {
                    "Edit Steps" -> "steps"
                    "Edit Protein" -> "protein"
                    "Edit Carbs" -> "carbs"
                    "Edit Fats" -> "fats"
                    "Edit Calories" -> "calories"
                    else -> goalTitle.lowercase()
                }
                repo.saveGoal(goalKey, newValue)
            }
        }
        showDialog.value = true
    }



    fun onDialogSave(newValue: String) {
        onSaveDialog(newValue)
        showDialog.value = false
    }

    fun onDialogCancel() {
        showDialog.value = false
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {  }

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

                // Fetch new user's goals and steps
                // This ensures that every time a new user logs in, the ViewModel state
                // is updated with their personal steps and goals from Firebase.
                viewModelScope.launch {
                    try {
                        _steps.value = repo.getSteps()               // current user's steps
                        stepsGoal.value = repo.getGoal("steps")
                        proteinGoal.value = repo.getGoal("protein")
                        carbsGoal.value = repo.getGoal("carbs")
                        fatsGoal.value = repo.getGoal("fats")
                        caloriesGoal.value = repo.getGoal("calories")
                    } catch (e: Exception) {
                        println("Failed to fetch user data: ${e.message}")
                    }
                }
            }
        }

        // Load initial user data if already logged in (optional)
        viewModelScope.launch {
            val firebaseSteps = repo.getSteps()
            _steps.value = firebaseSteps
            stepsGoal.value = repo.getGoal("steps")
            proteinGoal.value = repo.getGoal("protein")
            carbsGoal.value = repo.getGoal("carbs")
            fatsGoal.value = repo.getGoal("fats")
            caloriesGoal.value = repo.getGoal("calories")
        }
    }

    fun saveStepsToFirebase(currentSteps: Int) {
        viewModelScope.launch {
            repo.saveSteps(currentSteps)
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

//    fun saveMeal(meal: Meal){
//        viewModelScope.launch {
//            repo.addMealItem(meal)
//        }
//    }
    fun fetchStepsFromFirebase() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val docRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .collection("pedometer")
            .document(today)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val stepsData = document.toObject(Steps::class.java)
                    _steps.value = stepsData?.steps ?: 0
                } else {
                    _steps.value = 0 // default if no data
                }
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

    fun guessNutrition(meal: String){
        viewModelScope.launch {
            val result = SpoonacularService.api.guessNutrition(meal)
            Log.d("MLog", result.toString())
            nutrition.value = result
            Log.d("MLog", nutrition.value.toString())
        }
    }

    fun saveNutritionInfo(meal: String) {
        viewModelScope.launch {
            val savedMeal = SavedMeal(
                name = meal,
                calories = nutrition.value?.calories?.value ?: 0.0,
                carbs = nutrition.value?.carbs?.value ?: 0.0,
                protein = nutrition.value?.protein?.value ?: 0.0,
                fat = nutrition.value?.fat?.value ?: 0.0,
                createdAt = Timestamp.now()
            )
            repo.addMealItem(savedMeal)
            clearNutrition()
        }
    }

    fun clearNutrition() {
        nutrition.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadWeeklySteps() {
        viewModelScope.launch {
            val days = (0..6).map { LocalDate.now().minusDays(it.toLong()) }

            val rawMap = mutableMapOf<LocalDate, Float>()

            for (day in days) {
                val doc = repo.getStepsForDay(day.toString())
                rawMap[day] = doc ?: 0f
            }

            _weeklySteps.value = alignToWeek(rawMap)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun alignToWeek(values: Map<LocalDate, Float>): List<Float> {
        val result = MutableList(7) { 0f }

        values.forEach { (date, steps) ->
            val dayIndex = when (date.dayOfWeek) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> 1
                DayOfWeek.WEDNESDAY -> 2
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 4
                DayOfWeek.SATURDAY -> 5
                DayOfWeek.SUNDAY -> 6
            }
            result[dayIndex] = steps
        }
        return result
    }
}