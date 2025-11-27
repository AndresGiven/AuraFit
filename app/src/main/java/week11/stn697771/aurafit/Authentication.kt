package week11.stn697771.aurafit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.viewmodel.MainViewModel


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