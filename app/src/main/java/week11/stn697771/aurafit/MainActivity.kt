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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


val DarkBackground = Color(0xFF000000)
val PrimaryBlue = Color(0xFF3B82F6)
val TextBlack = Color(0xFF1F2937)
val TextLight = Color(0xFFF3F4F6)
val OutlineColor = Color(0xFFE5E7EB)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MainViewModel = viewModel()
            val uiState by vm.uiState.collectAsState()
            val todos by vm.todos.collectAsState()

            when (uiState) {
                UiState.Loading -> Text("Loading...")
                UiState.AuthRequired -> LoginScreen(vm)
                UiState.Authenticated -> TodoScreen(vm, todos)
                UiState.AuthSetup -> SignUpScreen(vm)
            }
        }
    }
}

@Composable
fun LoginScreen(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Logo Section (takes 40% of the screen height)
        LogoSection(modifier = Modifier.weight(0.4f))

        // 2. Bottom Login Form Section (takes 60% of the screen height)
        LoginForm(vm, modifier = Modifier.weight(0.6f))
    }
}

@Composable
fun SignUpScreen(vm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Logo Section (takes 40% of the screen height)
        LogoSection(modifier = Modifier.weight(0.4f))

        // 2. Bottom SignUp Form Section (takes 60% of the screen height)
        SignUpForm(vm, modifier = Modifier.weight(0.6f))
    }
}

@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            //Add the logo After when I Figure out how to do that
            Text(
                "AURA FIT", color = TextLight, fontSize = 28.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoginForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp)
            )
            .background(Color.White)
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
                color = TextBlack,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Sign in", color = TextBlack, fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline, modifier = Modifier
            )
        }


        // Email Input
        var email by remember { mutableStateOf("email@example.com") }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue, unfocusedBorderColor = OutlineColor
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
                focusedBorderColor = PrimaryBlue, unfocusedBorderColor = OutlineColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Login Button
        Button(
            onClick = { vm.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBackground),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Login", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
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
                "Don't have an Account? ", color = TextBlack, fontSize = 14.sp
            )

            Text(
                text = "Sign up",
                color = DarkBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    vm.changeUIState(UiState.AuthSetup)
                    println("Sign up link clicked!")
                    })

        }
    }
}

@Composable
fun SignUpForm(vm: MainViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(topStart = 64.dp, topEnd = 64.dp)
            )
            .background(Color.White)
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
                "Welcome!",
                color = TextBlack,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Sign Up", color = TextBlack, fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline, modifier = Modifier
            )
        }


        // Email Input
        var email by remember { mutableStateOf("email@example.com") }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
            // Match the desired border radius
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue, unfocusedBorderColor = OutlineColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password Input
        var password by remember { mutableStateOf("password") }
        var passwordVisible by remember { mutableStateOf(true) }

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
                focusedBorderColor = PrimaryBlue, unfocusedBorderColor = OutlineColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Login Button
        Button(
            onClick = { vm.signUp(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBackground),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SignUp", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        //Sign Up Footer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), // Replaced spacer with padding on the row
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Already have an account? ", color = TextBlack, fontSize = 14.sp
            )

            Text(
                text = "Sign in",
                color = DarkBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    vm.changeUIState(UiState.AuthRequired)
                    println("Sign up link clicked!")
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