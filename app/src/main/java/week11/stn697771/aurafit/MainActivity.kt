package week11.stn697771.aurafit


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.stn697771.aurafit.model.TodoItem
import week11.stn697771.aurafit.ui.theme.AuraFitTheme
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuraFitTheme {
                val vm: MainViewModel = viewModel()
                val uiState by vm.uiState.collectAsState()
                val todos by vm.todos.collectAsState()

                when (uiState) {
                    UiState.Loading -> Text("Loading...")
                    UiState.AuthRequired -> LoginScreen(vm)
                    UiState.Authenticated -> Pedometer(vm, todos)
                    UiState.AuthSetup -> SignUpScreen(vm)
                    UiState.AuthForgot -> PasswordScreen(vm)
                }
            }
        }
    }
}


@Composable
fun LoginScreen(vm: MainViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        // 1. Top Logo Section (takes 30% of the screen height)
        LogoSection(modifier = Modifier.weight(0.5f))

        // 2. Bottom Login Form Section (takes 60% of the screen height)
        LoginForm(vm, modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun SignUpScreen(vm: MainViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        // 1. Top Logo Section (takes 30% of the screen height)
        LogoSection(modifier = Modifier.weight(0.5f))

        // 2. Bottom SignUp Form Section (takes 70% of the screen height)
        SignUpForm(vm, modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun PasswordScreen(vm: MainViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        // 1. Top Logo Section (takes 30% of the screen height)
        LogoSection(modifier = Modifier.weight(0.5f))

        // 2. Bottom SignUp Form Section (takes 70% of the screen height)
        PasswordForm(vm, modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        Color.Black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.primarylogo),
                contentDescription = "Logo"
            )
        }
    }
}


@Composable
fun LoginForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(topEnd = 100.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Hello!",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineLarge.copy(letterSpacing = (-3).sp),
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 2.dp)
            )
            Text(
                "Sign in",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 15.dp, bottom = 4.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(75.dp)
                    .padding(start = 15.dp, bottom = 10.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.background,

            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ){
            // Email Input
            var email by remember { mutableStateOf("email@example.com") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") }, // Color is handled by the theme
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                shape = RoundedCornerShape(15.dp),
                // 5. Use theme colors for text fields
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Password Input
            var password by remember { mutableStateOf("password") }
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                // Toggle visibility icon
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            //FORGOT PASSWORD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Forgot your password?",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            vm.changeUIState(UiState.AuthForgot)
                        }
                )
            }

            // Login Button
            Button(
                onClick = { vm.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // Specific color is a design choice, this is fine
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(15.dp)
            ) {
                // 6. Use theme typography
                Text("Login", style = MaterialTheme.typography.titleLarge)
            }

            // Sign Up Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Don't have an Account? ",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sign up",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable { vm.changeUIState(UiState.AuthSetup) }
                )
            }
        }
    }
}

@Composable
fun SignUpForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(topEnd = 100.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Welcome!",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineLarge.copy(letterSpacing = (-3).sp),
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 2.dp)
            )
            Text(
                "Sign up",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 15.dp, bottom = 4.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(140.dp)
                    .padding(start = 80.dp, bottom = 10.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.background,

                )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ){
            // Email Input
            var email by remember { mutableStateOf("email@example.com") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                shape = RoundedCornerShape(12.dp),
                // 3. Use theme colors for the text field
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Password Input
            var password by remember { mutableStateOf("password") }
            var passwordVisible by remember { mutableStateOf(false) }
            val isPasswordValid by remember(password) { mutableStateOf(password.length >= 6) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                // 2. Conditionally change the text field's state
                isError = password.isNotEmpty() && !isPasswordValid,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isPasswordValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    unfocusedBorderColor = if (isPasswordValid) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.error
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    // Remove bottom padding to place error text right below
                    .padding(bottom = 4.dp)
            )

            // 3. Add helper/error text that appears when needed
            if (password.isNotEmpty() && !isPasswordValid) {
                Text(
                    text = "Password must be at least 6 characters.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 16.dp) // Add padding to align with TextField
                )
            } else {
                // Add a spacer to keep the layout consistent when the error message is not showing
                Spacer(modifier = Modifier.height(36.dp))
            }

            // SignUp Button
            Button(
                onClick = { vm.signUp(email, password) },
                enabled = isPasswordValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // Using a specific color here is a valid design choice for emphasis
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(15.dp)
            ) {
                // 5. Use theme typography for the button text
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Sign In Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 6. Use theme styles for the footer text
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sign in",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable { vm.changeUIState(UiState.AuthRequired) }
                )
            }
        }
    }
}

@Composable
fun PasswordForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(topEnd = 100.dp)),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            // 2. Use theme colors and typography for text
            Text(
                "Password Reset",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.headlineLarge.copy(letterSpacing = (-3).sp),
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 2.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ){
            // Email Input
            var email by remember { mutableStateOf("email@example.com") }
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("email") }, // Color is now handled by the theme
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                shape = RoundedCornerShape(12.dp),
                // 3. Use theme colors for the text field
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                textStyle = TextStyle(
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Rest Password Button
            Button(
                onClick = { vm.sendPasswordReset(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(12.dp)
            ) {
                // 5. Use theme typography for the button text
                Text(
                    text = "Reset Password",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Sign In Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sign in",
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable {
                        vm.changeUIState(UiState.AuthRequired)
                    }
                )
            }
        }
    }
}



@Composable
fun Pedometer(vm: MainViewModel, todos: List<TodoItem>) {
    val DarkThemeColor = Color(0xFF1B212A)
    Column(
        Modifier
            .fillMaxSize()
            .background(DarkThemeColor),
        horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aura Fit",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        TopMeasurementBar()
        Spacer(modifier = Modifier.height(16.dp))
        CutCircularPedometer(progress = 1f)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressBar()
        Spacer(modifier = Modifier.height(40.dp))
        BottomNavBar()

        Button(onClick = { vm.logout() }) {
            Text("Logout")
        }
    }
}

//Used from https://m3.material.io/components/progress-indicators/overview
@Composable
fun LinearDeterminateIndicator() {
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Create a coroutine scope

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            loading = true
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false // Reset loading when the coroutine finishes
            }
        }, enabled = !loading) {
            Text("Start loading")
        }

        if (loading) {
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}

@Composable
fun CutCircularPedometer(
    modifier: Modifier = Modifier,
    progress: Float,
    steps: Int = 6543,
    goal: Int = 10000
) {
    val gradientColors = listOf(
        Color(0xFF6A00FF),
        Color(0xFFE91E63),
        Color(0xFFFF9800),
        Color(0xFFFFEB3B)
    )

    val totalSweep = 260f
    val startAngle = -220f
    val progressSweep = progress * totalSweep

    Box(
        modifier = modifier.size(250.dp), //change this for outer ring
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 22.dp.toPx()

            drawArc(
                color = Color(0xFFBDBDBD),
                startAngle = startAngle,
                sweepAngle = totalSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(gradientColors),
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }
        Canvas(
            modifier = Modifier
                .size(210.dp) //change this for inner ring
                .align(Alignment.Center)
        ) {
            drawCircle(
                color = Color(0xFF2C3440),
                radius = size.minDimension / 2
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "%,d".format(steps),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Goal: ${"%,d".format(goal)}",
                fontSize = 16.sp,
                color = Color.LightGray
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Spacer(Modifier.size(12.dp))

            CircularButton {
                // adding on click code later
            }
        }
    }
}

@Composable
fun TopMeasurementBar() {
    Row(
        modifier = Modifier.fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF2C3440))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = "Fire Symbol",
                tint = Color.White
            )
            Text(
                text = "330 k cal",
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                contentDescription = "Walking dude",
                tint = Color.White
            )
            Text(
                text = "5.4 KM",
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Time Clock",
                tint = Color.White
            )
            Text(
                text = "1.2 Hours",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }

}

@Composable
fun ProgressBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF2C3440))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Colored progress bar on top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(59f / 90f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Protein 59g / 90g",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(163f / 310f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Carbs 163g / 310g",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(55f / 70f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Fat 55g / 70g",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1213f / 1800f)
                        .fillMaxHeight()
                        .background(Color(0xFFFFC107))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Cals 1213 / 1800",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
        }

    }
}


@Composable
fun CircularButton(
    onClick: () -> Unit,
    ) {
    var pausePlay by remember { mutableStateOf(false) } // State to track if music is playing
    IconButton(
        onClick = { pausePlay = !pausePlay },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color(0xFF0000FF ),
            contentColor = Color.White
        ),
        modifier = Modifier.size(68.dp),

    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = if (pausePlay) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Basically swaps from pause to play with a click",
        )
    }
}
@Composable
fun BottomNavButton(
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Composable
fun BottomNavBar(){
    Row(
        modifier = Modifier.fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF2C3440)),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        BottomNavButton(
            icon = Icons.Default.Home,
            contentDescription = "Home",
            onClick = { }
        )

        BottomNavButton(
            icon = Icons.AutoMirrored.Filled.ShowChart,
            contentDescription = "Graph ",
            onClick = { }
        )

        BottomNavButton(
            icon = Icons.Default.AddAlarm,
            contentDescription = "Add button",
            onClick = {  }
        )
        BottomNavButton(
            icon = Icons.Default.AccountCircle,
            contentDescription = "Account Icon",
            onClick = {  }
        )
    }
}




