package com.example.ranksprint.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ranksprint.common.Utils
import com.example.ranksprint.ui.screens.AboutUsScreen
import com.example.ranksprint.ui.screens.AddTicketScreen
import com.example.ranksprint.ui.screens.AllCategoriesScreen
import com.example.ranksprint.ui.screens.CategoryScreen
import com.example.ranksprint.ui.screens.ContactUsScreen
import com.example.ranksprint.ui.screens.EnquiryScreen
import com.example.ranksprint.ui.screens.FAQScreen
import com.example.ranksprint.ui.screens.HomeScreen
import com.example.ranksprint.ui.screens.HomeSubCategoryItems
import com.example.ranksprint.ui.screens.HomeSubCategoryScreen
import com.example.ranksprint.ui.screens.MockTestEngineScreen
import com.example.ranksprint.ui.screens.MyRanksScreen
import com.example.ranksprint.ui.screens.MyTestsScreen
import com.example.ranksprint.ui.screens.NotLoggedInScreen
import com.example.ranksprint.ui.screens.NotificationScreen
import com.example.ranksprint.ui.screens.OTPScreen
import com.example.ranksprint.ui.screens.PaymentSuccessScreen
import com.example.ranksprint.ui.screens.PersonalInfoScreen
import com.example.ranksprint.ui.screens.PrivacyPolicyScreen
import com.example.ranksprint.ui.screens.ProfileScreen
import com.example.ranksprint.ui.screens.RefundPolicyScreen
import com.example.ranksprint.ui.screens.ScoreScreen
import com.example.ranksprint.ui.screens.ScorecardScreen
import com.example.ranksprint.ui.screens.SignInScreen
import com.example.ranksprint.ui.screens.SignUpScreen
import com.example.ranksprint.ui.screens.SplashScreen
import com.example.ranksprint.ui.screens.SubCategoryTestsScreen
import com.example.ranksprint.ui.screens.SubjectSelectionScreen
import com.example.ranksprint.ui.screens.SubscriptionPlansScreen
import com.example.ranksprint.ui.screens.SubscriptionScreen
import com.example.ranksprint.ui.screens.TermsConditionsScreen
import com.example.ranksprint.ui.screens.TestInstructionsScreen
import com.example.ranksprint.ui.screens.TicketDetailsScreen
import com.example.ranksprint.ui.screens.TicketListScreen
import com.example.ranksprint.ui.viewmodels.SharedViewModel

@Composable
fun RankSprintNavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = {
                val isRegistered = Utils.getData(context, Utils.IS_REGISTERED, false) as Boolean
                val nextRoute = if (isRegistered) Screen.Home.route else Screen.SignIn.route
                navController.navigate(nextRoute) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.SignIn.route) {
            SignInScreen(
                onSignInSuccess = { userId, email ->
                    navController.navigate(Screen.Otp.route + "/$userId/$email")
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = { userId, email ->
                    navController.navigate(Screen.Otp.route + "/$userId/$email")
                }
            )
        }
        composable(Screen.Otp.route + "/{userId}/{email}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OTPScreen(
                userId = userId,
                email = email,
                onOtpVerified = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToTerms = { navController.navigate(Screen.Terms.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                onNavigateToCategory = { categoryId ->
                    navController.navigate(Screen.CategoryTests.createRoute(categoryId))
                },
                onNavigateToTestInstructions = { testId ->
                    Log.e("testId", "testId $testId")
                    navController.navigate("test_instructions/$testId")
                },
                onViewAllCategories = {
                    navController.navigate(Screen.Categories.route)
                },
                onViewNotification = {
                    navController.navigate("notificationScreen")
                }
            )
        }

        composable(Screen.Categories.route) {
            AllCategoriesScreen(
                navController = navController,
                onNavigateToCategoryTests = { categoryId ->
                    navController.navigate(Screen.CategoryTests.createRoute(categoryId))
                },
                onNavigateToHomeSubCategoryTests = { categoryId, categoryName ->
                    navController.navigate(Screen.HomeSubCategoryTests.createRoute(categoryId, categoryName))
                }
            )
        }

        composable(Screen.CategoryTests.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryScreen(
                navController = navController,
                categoryId = categoryId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTestInstructions = { testId ->
                    navController.navigate("test_instructions/$testId")
                }
            )
        }

        composable(Screen.HomeSubCategoryTests.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            HomeSubCategoryScreen(
                navController = navController,
                categoryId = categoryId,
                categoryName = categoryName,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTestInstructions = { testId ->
                    navController.navigate("test_instructions/$testId")
                }
            )
        }

        composable(Screen.HomeSubCategoryItemsTests.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val subCategoryItemName = backStackEntry.arguments?.getString("subCategoryItemName") ?: ""
            HomeSubCategoryItems(
                navController = navController,
                categoryId = categoryId,
                subCategoryItemName = subCategoryItemName,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTestInstructions = { testId ->
                    navController.navigate("test_instructions/$testId")
                }
            )
        }

        composable("subcategory_tests/{subCategoryId}/{subCategoryName}") { backStackEntry ->
            val subCategoryId = backStackEntry.arguments?.getString("subCategoryId") ?: ""
            val subCategoryName = backStackEntry.arguments?.getString("subCategoryName") ?: ""
            SubCategoryTestsScreen(
                navController = navController,
                subCategoryId = subCategoryId,
                subCategoryName = subCategoryName,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTestInstructions = { testId ->
                    navController.navigate("test_instructions/$testId")
                }
            )
        }

        composable("subject_selection/{testId}") { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            SubjectSelectionScreen(
                navController = navController,
                categoryName = "SBI Clerk",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToInstructions = { _ ->
                    navController.navigate("test_instructions/$testId")
                }
            )
        }

        composable("test_instructions/{testId}") { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            TestInstructionsScreen(
                navController = navController,
                testId = testId,
                onNavigateBack = { navController.popBackStack() },
                onStartTest = {
                    navController.navigate(Screen.MockTestEngine.createRoute(testId))
                }
            )
        }

        composable(Screen.MockTestEngine.route) { backStackEntry ->
            val testId = backStackEntry.arguments?.getString("testId") ?: ""
            MockTestEngineScreen(
                navController = navController,
                testId = testId,
                onNavigateBack = { navController.popBackStack() },
                onFinishTest = { response ->
                    sharedViewModel.setSubmitResponse(response)
                    navController.navigate("score")
                }
            )
        }

        composable("score") {
            val submitResponse by sharedViewModel.lastSubmitResponse.collectAsState()
            val totalQuestions = submitResponse?.data?.totalQuestions ?: 0
            val correctAnswers = submitResponse?.data?.correctAnswers ?: 0
            val wrongAnswers = submitResponse?.data?.wrongAnswers ?: 0
            val notAttempted = totalQuestions - correctAnswers - wrongAnswers

            ScoreScreen(
                navController = navController,
                score = submitResponse?.data?.score?.toIntOrNull() ?: 0,
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                wrongAnswers = wrongAnswers,
                notAttempted = if (notAttempted > 0) notAttempted else 0,
                scorePercentage = submitResponse?.data?.scorePercentage,
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MyTests.route) {
            MyTestsScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                onNavigateToEditProfile = { navController.navigate("personal_info") },
                onNavigateToMyRanksScreen = { navController.navigate(Screen.MyRanksScreen.route) },
                onNavigateToMyTicketListScreen = { navController.navigate(Screen.TicketListScreen.route) },
                onNavigateToAboutUs = { navController.navigate(Screen.AboutUs.route) },
                onNavigateToTerms = { navController.navigate(Screen.Terms.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) },
                onNavigateToContact = { navController.navigate(Screen.Contact.route) },
                onNavigateToFaq = { navController.navigate("faq") },
                onNavigateToEnquiry = { navController.navigate("enquiry") },
                onNavigateToSubscriptions = { navController.navigate(Screen.Subscription.route) },
                onLogout = {
                    Utils.clearPref(context)
                    Utils.user_id = ""
                    Utils.access_token = ""
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("personal_info") {
            PersonalInfoScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }

        composable(Screen.Subscription.route) {
            SubscriptionPlansScreen(
                navController = navController,
                onSubscribe = { planId ->
                    navController.navigate("subscription_details/$planId")
                }
            )
        }

        composable("subscription_details/{planId}") { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            SubscriptionScreen(
                navController = navController,
                planId = planId,
                onNavigateBack = { navController.popBackStack() },
                onPayNow = { navController.navigate("payment_success") }
            )
        }

        composable(Screen.Scorecard.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ScorecardScreen(
                navController = navController,
                productId = productId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Overriding SubscriptionScreen logic in NavGraph for now or better, update SubscriptionScreen.kt

        composable("payment_success") {
            PaymentSuccessScreen(
                navController = navController,
                onReturnHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TicketListScreen.route) {
            TicketListScreen(
                navController = navController,
                onViewAddTicketScreen = {
                    navController.navigate("add_ticket_screen")
                },
                onViewTicketDetailsScreen = { ticketId ->
                    navController.navigate(
                        Screen.TicketDetailsScreen.createRoute(ticketId)
                    )
                },
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.TicketDetailsScreen.route) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
            TicketDetailsScreen(
                ticketId=  ticketId,
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AddTicketScreen.route) {
            AddTicketScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.MyRanksScreen.route) {
            MyRanksScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AboutUs.route) {
            AboutUsScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Terms.route) {
            TermsConditionsScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Privacy.route) {
            PrivacyPolicyScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Contact.route) {
            ContactUsScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Refund.route) {
            RefundPolicyScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.NotLoggedIn.route) {
            NotLoggedInScreen(
                navController = navController,
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable("faq") {
            FAQScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("enquiry") {
            EnquiryScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Notification.route) {
            NotificationScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("notificationScreen") {
            NotificationScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$name Screen")
    }
}
