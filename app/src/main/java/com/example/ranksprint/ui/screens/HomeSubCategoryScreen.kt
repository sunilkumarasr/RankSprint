package com.example.ranksprint.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quiztech.model.HomeSubCategoryResponse
import com.example.quiztech.model.HomeSubcategoriesItem
import com.example.quiztech.services.ServiceManager
import com.example.ranksprint.navigation.Screen
import com.example.ranksprint.ui.components.BottomNavigationBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

@Composable
fun HomeSubCategoryScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
    onNavigateToTestInstructions: (String) -> Unit,
    categoryId: String,
    categoryName: String
) {

    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("") }

    var subCategories by remember {
        mutableStateOf<List<HomeSubcategoriesItem>>(emptyList())
    }

    LaunchedEffect(categoryId) {
        Log.e("asdf_", "1")
        ServiceManager.getDataManager().getHomeSubCategories(
            object : Callback<HomeSubCategoryResponse> {
                override fun onResponse(
                    call: Call<HomeSubCategoryResponse>,
                    response: Response<HomeSubCategoryResponse>
                ) {

                    isLoading = false

                    if (response.isSuccessful) {

                        val body = response.body()

                        if (body?.status == 1) {

                            subCategories =
                                body.homeSubCategoriesItems.toList()

                        } else {

                            message =
                                body?.message ?: "No subcategories found"
                        }
                    } else {
                        message = "Failed to load subcategories"
                    }
                }

                override fun onFailure(
                    call: Call<HomeSubCategoryResponse>,
                    t: Throwable
                ) {

                    isLoading = false
                    message = t.message ?: "Something went wrong"
                }
            },
            categoryId
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        when {

            isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            subCategories.isEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message.ifEmpty {
                            "No subcategories found"
                        }
                    )
                }
            }

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 16.dp,
                                top = 12.dp,
                                bottom = 16.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                        Column {

                            Text(
                                text = categoryName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Choose a sub-category to start practicing",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(subCategories) { subCategory ->

                            SubCategoryGridItem(
                                subCategory = subCategory,
                                onClick = {
                                    navController.navigate(
                                        Screen.HomeSubCategoryItemsTests.createRoute(
                                            subCategory.id.toString(),
                                            subCategory.name ?: ""
                                        )
                                    )
                                }

                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubCategoryGridItem(
    subCategory: HomeSubcategoriesItem,
    onClick: () -> Unit
) {

    val colors = listOf(
        Pair(Color(0xFFE3F2FD), Color(0xFF2196F3)),
        Pair(Color(0xFFFFF3E0), Color(0xFFFF9800)),
        Pair(Color(0xFFE8F5E9), Color(0xFF4CAF50)),
        Pair(Color(0xFFF3E5F5), Color(0xFF7E57C2)),
        Pair(Color(0xFFFFEBEE), Color(0xFFF44336)),
        Pair(Color(0xFFFFF8E1), Color(0xFFFF6F00))
    )

    val colorPair =
        colors[abs(subCategory.name.hashCode()) % colors.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorPair.first
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = subCategory.name?.first().toString(),
                color = colorPair.second.copy(alpha = 0.10f),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(colorPair.second),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = subCategory.name?.first().toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {

                    subCategory.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Test Exams",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}