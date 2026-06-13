package com.example.ranksprint.ui.screens

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quiztech.model.BannerList
import com.example.quiztech.model.Category
import com.example.quiztech.model.MockList
import com.example.ranksprint.R
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.components.BottomNavigationBar
import com.example.ranksprint.ui.theme.RankSprintTheme
import com.example.ranksprint.ui.viewmodels.HomeViewModel
import com.example.ranksprint.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToTestInstructions: (String) -> Unit,
    onViewAllCategories: () -> Unit,
    onViewNotification: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()
    val banners by viewModel.banners.collectAsState()
    val popularMockTests by viewModel.popularMockTests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val sharedUserInfo by sharedViewModel.userInfo.collectAsState()

    val userName = remember { mutableStateOf("") }
    val userPhone = remember { mutableStateOf("") }
    val userProfileImage = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (Utils.user_id.isNullOrEmpty()) {
            Utils.user_id = Utils.getData(context, "user_id", "") as String
        }
        if (Utils.access_token.isEmpty()) {
            Utils.access_token = Utils.getData(context, "access_token", "") as String
        }
    }

    LaunchedEffect(sharedUserInfo) {
        if (sharedUserInfo != null) {
            userName.value = sharedUserInfo?.fullName ?: ""
            userPhone.value = sharedUserInfo?.phone ?: ""
            userProfileImage.value = sharedUserInfo?.profileImage ?: ""
        } else {
            userName.value = Utils.getData(context, "full_name", "") as String
            userPhone.value = Utils.getData(context, "phone", "") as String
            userProfileImage.value = Utils.getData(context, "profile_image", "") as String
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                HeaderSection(onViewNotification,userName.value, userPhone.value, userProfileImage.value)
                BannerSection(banners)

                if (error != null) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                CategoriesSection(categories, onNavigateToCategory, onViewAllCategories)
                RecentTestSection(popularMockTests, onNavigateToTestInstructions)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFFE5D10)
                )
            }
        }
    }
}

@Composable
fun HeaderSection(onViewNotification: () -> Unit, userName: String, userPhone: String, userProfileImage: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = userProfileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.banner_1),
                error = painterResource(R.drawable.banner_1)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = userName.ifEmpty { "User" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Phone: ${userPhone.ifEmpty { "N/A" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }


        IconButton(
            onClick = {
                // Handle notification click
                onViewNotification()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications"
            )
        }

//       Surface(
//            color = Color(0xFF004AAD),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Row(
//                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(Icons.Outlined.Diamond, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(text = "160", color = Color.White, fontWeight = FontWeight.Bold)
//            }
//        }
    }
}

@Composable
fun BannerSection(banners: List<BannerList>) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF001D36))
        ) { page ->
            AsyncImage(
                model = banners[page].image,
                contentDescription = "Banner ${page + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(R.drawable.banner_1),
                error = painterResource(R.drawable.banner_1)
            )
        }

        // Pager indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(width = 32.dp, height = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<Category>,
    onNavigateToCategory: (String) -> Unit,
    onViewAllCategories: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = Color(0xFF43A047),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(text = "Categories", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            TextButton(onClick = onViewAllCategories) {
                Text(text = "View All", color = Color(0xFF43A047), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val colors = listOf(
            Pair(Color(0xFFE1F5FE), Color(0xFF039BE5)),
            Pair(Color(0xFFFFF3E0), Color(0xFFFFB300)),
            Pair(Color(0xFFE8F5E9), Color(0xFF43A047)),
            Pair(Color(0xFFF3E5F5), Color(0xFF8E24AA)),
            Pair(Color(0xFFFFF1F0), Color(0xFFE53935)),
            Pair(Color(0xFFFFF8E1), Color(0xFFF4511E))
        )

        val displayCategories = categories.take(6)

        Column {
            for (i in displayCategories.indices step 2) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    val color1 = colors[i % colors.size]
                    CategoryCard(
                        CategoryItemData(
                            categories[i].name,
                            color1.first,
                            color1.second,
                            categories[i].image
                        ),
                        Modifier.weight(1f)
                    ) { onNavigateToCategory(categories[i].id) }

                    Spacer(modifier = Modifier.width(16.dp))

                    if (i + 1 < categories.size) {
                        val color2 = colors[(i + 1) % colors.size]
                        CategoryCard(
                            CategoryItemData(
                                categories[i + 1].name,
                                color2.first,
                                color2.second,
                                categories[i + 1].image
                            ),
                            Modifier.weight(1f)
                        ) { onNavigateToCategory(categories[i + 1].id) }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class CategoryItemData(
    val name: String,
    val bgColor: Color,
    val iconColor: Color,
    val image: String
)

@Composable
fun CategoryCard(data: CategoryItemData, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = data.bgColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(data.iconColor),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = data.image,
                    contentDescription = data.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Test Exams",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun RecentTestSection(
    popularMockTests: List<MockList>,
    onNavigateToTestInstructions: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recent Test",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            for (i in popularMockTests.indices step 2) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    RecentTestCard(
                        mockTest = popularMockTests[i],
                        modifier = Modifier.weight(1f),
                        onNavigateToTestInstructions = onNavigateToTestInstructions
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    if (i + 1 < popularMockTests.size) {
                        RecentTestCard(
                            mockTest = popularMockTests[i + 1],
                            modifier = Modifier.weight(1f),
                            onNavigateToTestInstructions = onNavigateToTestInstructions
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Composable
fun RecentTestCard(
    mockTest: MockList,
    modifier: Modifier = Modifier,
    onNavigateToTestInstructions: (String) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mockTest.title ?: "Mock Test",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF004AAD),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This mock test helps you quickly assess your preparation.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                HomeInfoRow("Questions : ", mockTest.questions ?: "0", Modifier.fillMaxWidth())
                HomeInfoRow(
                    "Duration : ",
                    "${mockTest.pDuration ?: "0"} Min",
                    Modifier.fillMaxWidth()
                )

                HomeInfoRow("Mark : ", "${mockTest.marks ?: "0"} Marks", Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(16.dp))
            /* Text(
                 text = buildAnnotatedString {
                     append("Price : ")
                     withStyle(style = SpanStyle(color = Color(0xFF004AAD), fontWeight = FontWeight.Bold)) {
                         append(mockTest.ourPrice ?: "Free")
                     }
                 },
                 style = MaterialTheme.typography.bodyMedium
             )
             Spacer(modifier = Modifier.height(16.dp))*/
            Button(
                onClick = { onNavigateToTestInstructions(mockTest.id ?: "") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE5D10))
            ) {
                Text(text = "Start Test", color = Color.White)
            }
        }
    }
}

@Composable
fun HomeInfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,

        ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF004AAD),
            fontWeight = FontWeight.Bold
        )
    }
}

// Removed local BottomNavigationBar as it is now in a separate file

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RankSprintTheme {
        HomeScreen(
            navController = rememberNavController(),
            sharedViewModel = SharedViewModel(),
            onNavigateToCategory = {},
            onNavigateToTestInstructions = {},
            onViewAllCategories = {},
            onViewNotification = {},
            viewModel = HomeViewModel() // Ideally use a mock or pre-filled VM for preview
        )
    }
}
