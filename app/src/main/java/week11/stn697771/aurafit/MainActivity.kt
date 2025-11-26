package week11.stn697771.aurafit


import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import week11.stn697771.aurafit.ui.theme.AuraFitTheme

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.stn697771.aurafit.model.TodoItem
import week11.stn697771.aurafit.util.UiState
import week11.stn697771.aurafit.viewmodel.MainViewModel

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
            }
        }
    }
}

@Composable
fun LoginScreen(vm: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("FireBase MVVM Ver.2")
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Row {
            Button(onClick = { vm.login(email, password) }, modifier = Modifier.padding(end = 8.dp)) {
                Text("Login")
            }
            Button(onClick = { vm.signUp(email, password) }) {
                Text("Sign Up")
            }
        }
    }
}

@Composable
fun TodoScreen(vm: MainViewModel, todos: List<TodoItem>) {
    var text by remember { mutableStateOf("") }
    var urgent by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("New Todo") })
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
                todo.userEmail?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Red) }
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
    }}