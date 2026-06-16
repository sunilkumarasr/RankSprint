package com.example.ranksprint.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object Otp : Screen("otp")
    object Home : Screen("home")
    object Categories : Screen("categories")
    object CategoryTests : Screen("category_tests/{categoryId}") {
        fun createRoute(categoryId: String?) = "category_tests/$categoryId"
    }
    object HomeSubCategoryTests : Screen("home_subcategory_tests/{categoryId}/{categoryName}") {
        fun createRoute(
            categoryId: String,
            categoryName: String
        ) = "home_subcategory_tests/$categoryId/$categoryName"
    }
    object HomeSubCategoryItemsTests : Screen("home_subcategory_items_tests/{categoryId}/{subCategoryItemName}") {
        fun createRoute(
            categoryId: String,
            subCategoryItemName: String
        ) = "home_subcategory_items_tests/$categoryId/$subCategoryItemName"
    }
    object MyTests : Screen("my_tests")
    object Profile : Screen("profile")
    object MockTestInfo : Screen("mock_test_info/{testId}") {
        fun createRoute(testId: String) = "mock_test_info/$testId"
    }
    object MockTestEngine : Screen("mock_test_engine/{testId}") {
        fun createRoute(testId: String) = "mock_test_engine/$testId"
    }
    object Subscription : Screen("subscription")
    object Scorecard : Screen("scorecard/{productId}") {
        fun createRoute(productId: String) = "scorecard/$productId"
    }
    object AboutUs : Screen("about_us")
    object Terms : Screen("terms")
    object Privacy : Screen("privacy")
    object Contact : Screen("contact")
    object Refund : Screen("refund")
    object NotLoggedIn : Screen("not_logged_in")
    object FAQ : Screen("faq")
    object Notification : Screen("notification")
}
