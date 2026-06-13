package com.example.ranksprint.ui.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import com.example.ranksprint.ui.viewmodels.EnquiryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnquiryScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
    viewModel: EnquiryViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var subjectError by remember { mutableStateOf<String?>(null) }
    var messageError by remember { mutableStateOf<String?>(null) }

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val submitResult by viewModel.submitResult.collectAsState()
    val context = LocalContext.current

    fun validate(): Boolean {
        var isValid = true
        if (name.isBlank()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = null
        }

        if (email.isBlank()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        if (phone.isBlank()) {
            phoneError = "Phone is required"
            isValid = false
        } else if (phone.length != 10 || !phone.all { it.isDigit() }) {
            phoneError = "Phone must be exactly 10 digits"
            isValid = false
        } else {
            phoneError = null
        }

        if (subject.isBlank()) {
            subjectError = "Subject is required"
            isValid = false
        } else {
            subjectError = null
        }

        if (message.isBlank()) {
            messageError = "Message is required"
            isValid = false
        } else {
            messageError = null
        }

        return isValid
    }

    LaunchedEffect(submitResult) {
        submitResult?.let { result ->
            result.onSuccess { msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                viewModel.resetResult()
                onNavigateBack()
            }.onFailure { err ->
                Toast.makeText(context, err.message ?: "Submission failed", Toast.LENGTH_SHORT).show()
                viewModel.resetResult()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enquiry", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Send us an enquiry",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004AAD),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    if (nameError != null) nameError = null
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    if (emailError != null) emailError = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { 
                    phone = it
                    if (phoneError != null) phoneError = null
                },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneError != null,
                supportingText = { phoneError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = { 
                    subject = it
                    if (subjectError != null) subjectError = null
                },
                label = { Text("Subject") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                isError = subjectError != null,
                supportingText = { subjectError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { 
                    message = it
                    if (messageError != null) messageError = null
                },
                label = { Text("Message") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(8.dp),
                isError = messageError != null,
                supportingText = { messageError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validate()) {
                        viewModel.submitEnquiry(name, phone, subject, message,  Utils.user_id!!,email)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Submit Enquiry", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
