package week11.stn697771.aurafit


import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.stn697771.aurafit.model.TodoItem
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.viewmodel.MainViewModel
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import week11.stn697771.aurafit.ui.theme.AuraFitTheme

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
                    UiState.Authenticated -> TodoScreen(vm, todos)
                    UiState.AuthSetup -> SignUpScreen(vm)
                    UiState.AuthForgot -> PasswordScreen(vm)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Logo Section (takes 30% of the screen height)
        LogoSection(modifier = Modifier.weight(0.3f))

        // 2. Bottom Login Form Section (takes 60% of the screen height)
        LoginForm(vm, modifier = Modifier.weight(0.7f))
    }
}

@Composable
fun SignUpScreen(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Logo Section (takes 30% of the screen height)
        LogoSection(modifier = Modifier.weight(0.3f))

        // 2. Bottom SignUp Form Section (takes 70% of the screen height)
        SignUpForm(vm, modifier = Modifier.weight(0.7f))
    }
}

@Composable
fun PasswordScreen(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Logo Section (takes 30% of the screen height)
        LogoSection(modifier = Modifier.weight(0.3f))

        // 2. Bottom SignUp Form Section (takes 70% of the screen height)
        PasswordForm(vm, modifier = Modifier.weight(0.7f))
    }
}

@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            // It's okay to use a specific color here for this design element
            .background(Color.Black), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 2. Use the theme's typography
            Text(
                "AURA FIT", color = Color.White, // Specific color for dark background
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun LoginForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp))
            // 3. Use theme colors for background
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Hello!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Sign in",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textDecoration = TextDecoration.Underline
            )
        }


        // Email Input
        var email by remember { mutableStateOf("email@example.com") }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") }, // Color is handled by the theme
            // ... (icon)
            shape = RoundedCornerShape(12.dp),
            // 5. Use theme colors for text fields
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
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
                val image = if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
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
                color = MaterialTheme.colorScheme.primary,
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
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            // 6. Use theme typography
            Text(
                "Login", color = Color.White, style = MaterialTheme.typography.bodyLarge
            )
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
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sign up",
                color = Color.Black, // Specific color for emphasis
                style = MaterialTheme.typography.labelMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { vm.changeUIState(UiState.AuthSetup) })
        }
    }
}

@Composable
fun SignUpForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp))
            // 1. Use theme color for the background
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // 2. Use theme colors and typography for text
            Text(
                text = "Welcome!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textDecoration = TextDecoration.Underline
            )
        }

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
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            // 5. Use theme typography for the button text
            Text(
                text = "SignUp",
                color = Color.White, // Specific color for this button's dark background
                style = MaterialTheme.typography.bodyLarge
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
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sign in",
                color = Color.Black, // Keeping black for emphasis is fine
                style = MaterialTheme.typography.labelMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    vm.changeUIState(UiState.AuthRequired)
                })
        }
    }
}

@Composable
fun PasswordForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // 2. Use theme colors and typography for text
            Text(
                text = "Reset Password",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

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
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            // 5. Use theme typography for the button text
            Text(
                text = "Reset Password",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
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
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sign in",
                color = Color.Black,
                style = MaterialTheme.typography.labelMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    vm.changeUIState(UiState.AuthRequired)
                })
        }
    }
}

@Composable
fun TodoScreen(vm: MainViewModel, todos: List<TodoItem>) {
    var text by remember { mutableStateOf("") }
    var urgent by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New Todo") })
            Checkbox(checked = urgent, onCheckedChange = { urgent = it })
            Button(onClick = {
                if (text.isNotBlank()) {
                    vm.addTodo(text, urgent)
                    text = ""
                    urgent = false
                }
            }) { Text("+") }
        }
        LazyColumn {
            items(todos) { todo ->
                todo.userEmail?.let {
                    Text(
                        it, style = MaterialTheme.typography.bodySmall, color = Color.Red
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Text(
                        text = "${todo.content} ${if (todo.urgent) "⚠️" else ""}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        Button(onClick = { vm.logout() }) {
            Text("Logout")
        }
    }
}