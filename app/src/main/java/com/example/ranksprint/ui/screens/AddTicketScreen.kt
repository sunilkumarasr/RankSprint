package com.example.ranksprint.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiztech.model.CreateTicketResponse
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTicketScreen(
    navController: NavController,
    onNavigateBack: () -> Unit
) {

    var isLoading by remember { mutableStateOf(false) }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var subjectError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Ticket",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                        subjectError = false
                    },
                    placeholder = {
                        Text("Enter Subject")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = subjectError
                )

                if (subjectError) {
                    Text(
                        text = "Please enter subject",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = false
                    },
                    placeholder = {
                        Text("Enter your issue or query")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    isError = descriptionError
                )

                if (descriptionError) {
                    Text(
                        text = "Please enter description",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {

                        subjectError = subject.trim().isEmpty()
                        descriptionError = description.trim().isEmpty()

                        if (subjectError || descriptionError) {
                            return@Button
                        }

                        isLoading = true
                        val userId = Utils.getData(context, "user_id", "") as String

                        ServiceManager.getDataManager().createTicket(
                            object : Callback<CreateTicketResponse> {

                                override fun onResponse(
                                    call: Call<CreateTicketResponse>,
                                    response: Response<CreateTicketResponse>
                                ) {

                                    isLoading = false

                                    if (response.isSuccessful && response.body() != null) {

                                        val body = response.body()!!

                                        if (body.status == 1) {

                                            Toast.makeText(
                                                context,
                                                body.message ?: "Ticket submitted successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            navController.popBackStack()

                                        } else {

                                            Toast.makeText(
                                                context,
                                                body.message ?: "Failed to submit ticket",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    } else {

                                        Toast.makeText(
                                            context,
                                            "Something went wrong",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CreateTicketResponse>,
                                    t: Throwable
                                ) {

                                    isLoading = false

                                    Toast.makeText(
                                        context,
                                        t.message ?: "Network error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            userId = userId,
                            subject = subject,
                            message = description
                        )

                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = subject.isNotBlank() &&
                            description.isNotBlank() &&
                            !isLoading
                ) {
                    Text("Submit Ticket")
                }

            }
        }
    }
}