package com.example.quiztech.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MainResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    //@SerializedName("data"    ) var data    : Data   = Data(),
//    @SerializedName("profile_image" ) var profile_image :String?  = ""
)


data class Contact(

    @SerializedName("email") var email: String? = "",
    @SerializedName("landline_no") var landline_no: String? = "",
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("phone_2") var phone_2: String? = "",
    @SerializedName("address") var address: String? = "",
    @SerializedName("logo") var logo: String? = "",
    @SerializedName("created_date") var created_date: String? = "",
    @SerializedName("created_time") var created_time: String? = ""

)

data class FAQsMainResponse(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Faqs> = arrayListOf()

)

data class ContactResponse(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var contact: ArrayList<Contact> = arrayListOf(),

    )

data class OTPVerifyResponse(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var userInfo: UserInfo? = null,
    @SerializedName("access_token") var accessToken: String? = null

)

data class PageResponse(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<PageData> = arrayListOf(),
)

data class BannerResponse(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<BannerList> = arrayListOf(),
)

data class ResendOTPResponse(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("otp") var otp: String = "",
)

data class BannerList(
    @SerializedName("image") var image: String? = "",
    @SerializedName("id") var id: String? = "",
)

data class PageData(
    @SerializedName("id") var id: String? = "",
    @SerializedName("information_title") var information_title: String? = "",
    @SerializedName("description") var description: String? = "",
)

data class Faqs(

    @SerializedName("id") var id: String? = null,
    @SerializedName("type_id") var typeId: String? = null,
    @SerializedName("module_id") var moduleId: String? = null,
    @SerializedName("question") var question: String? = null,
    @SerializedName("answer") var answer: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("updated_by") var updatedBy: String? = null,
    @SerializedName("status") var status: String? = null,
    var isExpand: Boolean = false

)

data class EnrollResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: EnrollData? = null
)

data class EnrollData(
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("product_id") var productId: String? = null,
    @SerializedName("enrolled_at") var enrolledAt: String? = null,
    @SerializedName("expires_at") var expiresAt: String? = null
)

data class SubmitEnquiry(
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("subject") var subject: String = "",
    @SerializedName("message") var message: String = "",

    )

data class LoginResponse(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("otp") var otp: Int? = null
)

data class RegistrationMainRes(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: RegistrationData? = RegistrationData()

)

data class RegistrationData(

    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("otp") var otp: Int? = null

)

data class UserInfo(

    @SerializedName("id") var id: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("profile_image") var profileImage: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("active_subscription_id") var activeSubscriptionId: Int? = null,
    @SerializedName("available_quizes_qnty") var availableQuizesQnty: Int? = null,
    @SerializedName("active_subscription_end_date") var activeSubscriptionEndDate: String? = null,
    @SerializedName("is_new_user") var isNewUser: Int? = null

)

data class UserRegisterData(
    @SerializedName("user_id") var user_id: String = "",
    @SerializedName("first_name") var first_name: String = "",
    @SerializedName("last_name") var last_name: String = "",
    @SerializedName("type") var type: String = "",
    @SerializedName("house_no") var house_no: String = "",
    @SerializedName("floor") var floor: String = "",
    @SerializedName("area") var area: String = "",
    @SerializedName("landmark") var landmark: String = "",
    @SerializedName("city_town") var city_town: String = "",
    @SerializedName("zip_code") var zip_code: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("email") var email: String = "",
) : Serializable

data class UserProfileResponse(
    @SerializedName("status") var status: Boolean = false,
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: ArrayList<ProfileData> = arrayListOf(),
)

data class ProfileData(
    @SerializedName("full_name") var full_name: String? = "",
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("email") var email: String? = "",
    @SerializedName("users_id") var users_id: String? = "",
    @SerializedName("wallet") var wallet: String? = "",
    @SerializedName("referred_by") var referred_by: String? = "",
    @SerializedName("profile_image") var profile_image: String? = "",
)


data class CategoryResponse(

    @SerializedName("status") var status: Int = 0,
    @SerializedName("message") var message: String? = null,
    @SerializedName("categories") var categories: ArrayList<Category> = arrayListOf(),
)

data class Category(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("image") var image: String = "",
    @SerializedName("has_subcategories") var hasSubcategories: Int = 0,
    @SerializedName("subcategory_count") var subCategoryCount: Int = 0,
) : Serializable

data class SubCatResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("video_url") val video_url: String = "",
    @SerializedName("data") val data: ArrayList<SubCatData> = arrayListOf()
)

data class SubCatData(
    @SerializedName("id") var id: String = "",
    @SerializedName("product_id") var productId: String = "",
    @SerializedName("category_id") var categoryId: String = "",
    @SerializedName("sub_category_id") var subCategoryId: String = "",
    @SerializedName("category_id_name") var categoryIdName: String = "",
    @SerializedName("sub_category_id_name") var subCategoryIdName: String = "",
    @SerializedName("title") var title: String = "",
    @SerializedName("mrp_price") var mrpPrice: String = "",
    @SerializedName("market_price") var marketPrice: String = "",
    @SerializedName("our_price") var ourPrice: String = "",
    @SerializedName("price_off") var priceOff: String = "",
    @SerializedName("gst") var gst: String = "",
    @SerializedName("short_descriptions") var shortDescriptions: String = "",
    @SerializedName("descriptions") var descriptions: String = "",
    @SerializedName("specifications") var specifications: String = "",
    @SerializedName("image") var image: String = "",
    @SerializedName("broucher") var broucher: String = "",
    @SerializedName("attributes") var attributes: List<Any>,
    @SerializedName("attribute_id") var attributeId: String = "",
    @SerializedName("weight") var weight: String = "",
    @SerializedName("prices") var prices: String = "",
    @SerializedName("discount_prices") var discountPrices: String = "",
    @SerializedName("weight_class_id") var weightClassId: String = "",
    @SerializedName("cart_id") var cartId: String = "",
    @SerializedName("quantity") var quantity: String = "",
    // @SerializedName("video_url") var video_url: String="",
    @SerializedName("user_rating") var userRating: Int = 0,
    var addCount: Int = 0,
    var isNewSub: Boolean = false
) : Serializable


data class UserCommonJson(
    @SerializedName("api_key") var api_key: String? = "",
    @SerializedName("user_id") var user_id: String? = "",
    @SerializedName("email") var email: String? = "",
    @SerializedName("otp") var otp: String? = "",
    @SerializedName("id") var id: String? = "",
    @SerializedName("bank_id") var bank_id: String? = "",
    @SerializedName("vehicle_id") var vehicle_id: String? = "",
    @SerializedName("status") var status: String? = "",
    @SerializedName("hublist_id") var hublist_id: String? = "",
    @SerializedName("page_name") var page_name: String? = ""
)


data class MockListMainRes(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("mock_list") var mockList: ArrayList<MockList> = ArrayList()

)

data class MockList(

    @SerializedName("test_id") var id: String? = null,
    @SerializedName("product_id") var productId: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("category_name") var category_name: String? = null,
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("sub_category_id") var subCategoryId: String? = null,
    @SerializedName("our_price") var ourPrice: String? = null,
    @SerializedName("available_for") var availableFor: String? = null,
    @SerializedName("is_popular") var isPopular: String? = null,
    @SerializedName("test_type") var testType: String? = null,
    @SerializedName("short_descriptions") var short_descriptions: String? = null,
    @SerializedName("max_members_list") var max_members_list: Int = 0,
    @SerializedName("questions") var questions: String? = null,
    @SerializedName("marks") var marks: String? = null,
    @SerializedName("p_duration") var pDuration: String? = null

)


data class ContactUsMain(

    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<ContactUs> = arrayListOf()

)

data class ContactUs(

    @SerializedName("address") var address: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("phone_2") var phone2: String? = null,
    @SerializedName("email") var email: String? = null

)

data class TestDetailsResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("mock_data") var mockData: TestDetailsData? = null
)

data class TestDetailsData(
    @SerializedName("id") var id: String? = null,
    @SerializedName("product_id") var productId: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("category_id") var categoryId: String? = null,
    @SerializedName("p_date") var pDate: String? = null,
    @SerializedName("p_duration") var pDuration: String? = null,
    @SerializedName("p_time") var pTime: String? = null,
    @SerializedName("questions") var questions: String? = null,
    @SerializedName("marks") var marks: String? = null,
    @SerializedName("short_descriptions") var shortDescriptions: String? = null,
    @SerializedName("descriptions") var descriptions: String? = null,
    @SerializedName("available_for") var availableFor: String? = null,
    @SerializedName("is_popular") var isPopular: String? = null,
    @SerializedName("max_members_list") var maxMembersList: String? = null,
    @SerializedName("rewards") var rewards: String? = null,
    @SerializedName("test_type") var testType: String? = null,
    @SerializedName("test_instructions") var test_instructions: String? = null,
    @SerializedName("is_enrolled") var isEnrolled: Int? = null,
    @SerializedName("subjects") var subjects: ArrayList<TestSubject> = arrayListOf()
)

data class MyExamsResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<MyExamItem> = arrayListOf()
)

data class MyExamItem(

    @SerializedName("exam_id")
    var examId: String? = null,

    @SerializedName("user_exam_id")
    var userExamId: String? = null,

    @SerializedName("exam_name")
    var examName: String? = null,

    @SerializedName("product_id")
    var productId: String? = null,

    @SerializedName("user_id")
    var userId: String? = null,

    @SerializedName("total_questions")
    var totalQuestions: String? = null,

    @SerializedName("attempted_questions")
    var attemptedQuestions: Int? = null,

    @SerializedName("correct_answers")
    var correctAnswers: String? = null,

    @SerializedName("wrong_answers")
    var wrongAnswers: Int? = null,

    @SerializedName("un_attempted_questions")
    var unAttemptedQuestions: Int? = null,

    @SerializedName("score_percentage")
    var scorePercentage: String? = null,

    @SerializedName("user_rank")
    var userRank: Int? = null,

    @SerializedName("attempted_date")
    var attemptedDate: String? = null,

    @SerializedName("questions")
    var questions: ArrayList<QuestionItem> = arrayListOf()
)

data class QuestionItem(

    @SerializedName("question_id")
    var questionId: String? = null,

    @SerializedName("question_text")
    var questionText: String? = null,

    @SerializedName("selected_option")
    var selectedOption: String? = null,

    @SerializedName("correct_option")
    var correctOption: String? = null,

    @SerializedName("is_answered")
    var isAnswered: Int? = null,

    @SerializedName("is_correct")
    var isCorrect: Int? = null,

    @SerializedName("explanation")
    var explanation: String? = null
)

data class TestSubject(
    @SerializedName("subject_name") var subjectName: String? = null,
    @SerializedName("question_count") var question_count: Int? = null
)


data class NotificationResponse(
    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("notifications")
    var notifications: ArrayList<NotificationItem> = arrayListOf()
)

data class NotificationItem(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("user_id")
    var userId: Int? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("reference_id")
    var referenceId: String? = null,

    @SerializedName("is_read")
    var isRead: Int? = null,

    @SerializedName("created_at")
    var createdAt: String? = null
)

data class CreateTicketResponse(
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null
)

data class TicketsResponse(
    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("tickets")
    var tickets: ArrayList<TicketsItem> = arrayListOf()
)

data class TicketsItem(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("subject")
    var subject: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,
)

data class TicketDetailsResponse(

    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("ticket")
    var ticket: TicketDetailsItem? = null,

    @SerializedName("replies")
    var replies: ArrayList<TicketReplyItem> = arrayListOf()
)

data class TicketDetailsItem(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("user_id")
    var userId: String? = null,

    @SerializedName("subject")
    var subject: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null
)

data class TicketReplyItem(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("ticket_id")
    var ticketId: String? = null,

    @SerializedName("user_type")
    var userType: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null
)

data class MyTestResultResponse(

    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("summary")
    var summary: TestSummary? = null,

    @SerializedName("results")
    var results: ArrayList<TestResultItem> = arrayListOf()
)

data class TestSummary(

    @SerializedName("total_tests")
    var totalTests: Int? = null,

    @SerializedName("tests_attempted")
    var testsAttempted: Int? = null,

    @SerializedName("average_score")
    var averageScore: String? = null,

    @SerializedName("best_score")
    var bestScore: String? = null
)

data class TestResultItem(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("attempted_date")
    var attemptedDate: String? = null,

    @SerializedName("p_duration")
    var duration: String? = null,

    @SerializedName("score_percentage")
    var scorePercentage: Double? = null,

    @SerializedName("score")
    var score: String? = null,

    @SerializedName("image")
    var image: String? = null
)

data class LeaderBoardResponse(

    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("leaderboard")
    val leaderboard: List<LeaderBoardItem> = emptyList()
)

data class LeaderBoardItem(

    @SerializedName("rank")
    var rank: Int? = null,

    @SerializedName("user_id")
    var userId: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("profile_image")
    var profileImage: String? = null,

    @SerializedName("score")
    var score: Int? = null
)

data class HomeSubCategoryResponse(

    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("subcategories")
    var homeSubCategoriesItems: ArrayList<HomeSubcategoriesItem> = arrayListOf()
)

data class HomeSubcategoriesItem(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("name")
    var name: String? = null
)