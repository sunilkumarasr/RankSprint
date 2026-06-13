package com.example.ranksprint.services



import com.example.quiztech.model.BannerResponse
import com.example.quiztech.model.CategoryResponse
import com.example.quiztech.model.ContactUsMain
import com.example.quiztech.model.EnrollResponse
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.model.LoginResponse
import com.example.quiztech.model.MainResponse
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.model.ExamQuestionsResponse
import com.example.quiztech.model.MyExamsResponse
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.SubmitExamRequest
import com.example.quiztech.model.SubmitExamResponse
import com.example.quiztech.model.PageResponse
import com.example.quiztech.model.RegistrationMainRes
import com.example.quiztech.model.ResendOTPResponse
import com.example.quiztech.model.SubCategoryMainRes
import com.example.quiztech.model.SubscriptionDetailRes
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.model.TestDetailsResponse
import com.example.quiztech.model.TopicMainRes
import com.example.quiztech.services.ServiceManager.Companion.ROOT_URL_SUB
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface APIInterface {


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "register")
    fun registerUser(
        @Field("full_name") fullName: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("city") city: String,
        @Field("pincode") pincode: String,
        @Field("address") address: String
    ): Call<RegistrationMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "verify_otp")
    fun verifyOtp(
        @Field("user_id") user_id: String,
        @Field("otp") otp: String
    ): Call<OTPVerifyResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "login")
    fun loginUser(
        @Field("email") email: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "resend_otp")
    fun resendOtp(
        @Field("user_id") user_id: String
    ): Call<ResendOTPResponse>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "update_profile")
    fun updateProfile(
        @Header("Authorization") authHeader: String,
        @Field("user_id") user_id: String,
        @Field("full_name") full_name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("gender") gender: String,
        @Field("city") city: String,
        @Field("pincode") pincode: String,
    ): Call<OTPVerifyResponse>


    @POST(ROOT_URL_SUB + "get_categories")
    fun getCategories(
        @Header("Authorization") authHeader: String,
    ): Call<CategoryResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_sub_categories")
    fun getSubCategories(
        @Header("Authorization") authHeader: String,
        @Field("cat_id") cat_id: String,
    ): Call<SubCategoryMainRes>

    @POST(ROOT_URL_SUB + "get_topics")
    fun getTopicBySib(
        @Header("Authorization") authHeader: String,
        @Field("cat_id") cat_id: String
    ): Call<TopicMainRes>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_sub_topics")
    fun getSubTopicBySib(
        @Header("Authorization") authHeader: String,
        @Field("topic_id") topic_id: String,
    ): Call<MainResponse>


    @POST(ROOT_URL_SUB + "get_popular_mock_list")
    fun getPopularMockTests(  @Header("Authorization") authHeader: String): Call<MockListMainRes>



    //Test Apis
    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_category")
    fun getMockTestByCategory(  @Header("Authorization") authHeader: String,
                                @Field("category_id") category_ic: String,
                                @Field("user_id") user_id: String,
                                ): Call<MockListMainRes>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_sub_category")
    fun getMockTestBySubCategory(  @Header("Authorization") authHeader: String,
        @Field("sub_category_id") sub_category_ic: String): Call<MockListMainRes>



    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_topic")
    fun getMockTestByTopic(  @Header("Authorization") authHeader: String,
                                   @Field("topic_id") topic_ic: String): Call<MockListMainRes>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_sub_category")
    fun getMockTestBySubTopic(  @Header("Authorization") authHeader: String,
                                @Field("sub_topic_id") sub_topic_id: String): Call<MockListMainRes>






    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_test_details")
    fun getTestDetails(  @Header("Authorization") authHeader: String,
                         @Field("test_id") test_id: String,
                         @Field("user_id") user_id: String): Call<TestDetailsResponse>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_exam_questions")
    fun getExamQuestions(  @Header("Authorization") authHeader: String,
                         @Field("test_id") test_id: String): Call<ExamQuestionsResponse>


    @POST(ROOT_URL_SUB + "submit_exam")
    fun submitExam(
        @Header("Authorization") authHeader: String,
        @Body request: SubmitExamRequest
    ): Call<SubmitExamResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "user_exams")
    fun getMyExams( @Header("Authorization") authHeader: String,
                    @Field("api_key") apiKey: String,
                    @Field("user_id") user_id: String,
                    @Field("type") type: String // 1 for completed, 0 for new? Or similar
    ): Call<MyExamsResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_scorecard")
    fun getScoreCard( @Header("Authorization") authHeader: String,
                      @Field("api_key") apiKey: String,
                      @Field("user_id") user_id: String,
                      @Field("product_id") product_id: String
    ): Call<SubmitExamResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "my_quiz")
    fun myQuiz( @Header("Authorization") authHeader: String,
                @Field("api_key") apiKey: String,
                @Field("user_id") user_id: String,
                @Field("completed") type: String
    ): Call<Any>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "wallet_transactions_list")
    fun transactionLIst( @Header("Authorization") authHeader: String,
                         @Field("api_key") apiKey: String,
                         @Field("user_id") user_id: String

    ): Call<Any>  @FormUrlEncoded
    @POST(ROOT_URL_SUB + "subscribe_package")
    fun subscribe_package( @Header("Authorization") authHeader: String,
                         @Field("api_key") apiKey: String,
                         @Field("user_id") user_id: String,
                         @Field("product_id") product_id: String,
                         @Field("razorpay_payment_id") paymentId: String,

                           @Field("razorpay_order_id") payOrderId: String,

    ): Call<EnrollResponse>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "wallet_transactions_list")
    fun withDrawAmount( @Header("Authorization") authHeader: String,
                        @Field("api_key") apiKey: String,
                        @Field("user_id") user_id: String,
                        @Field("amount") amount: String,
                        @Field("bank_name") bank_name: String,
                        @Field("account_number") account_number: String,
                        @Field("ifsc_code") ifsc_code: String,
                        @Field("upi_id") upi_id: String,

    ): Call<Any>
//user_id,amount,bank_name,account_number,ifsc_code,upi_id

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_user_active_subscription")
    fun getUserActiveSubscriptions(  @Header("Authorization") authHeader: String,
                                     @Field("api_key") apiKey: String,
                                     @Field("user_id") user_id: String): Call<SubscriptionMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_subscription_plans")
    fun getSubscriptionPlans(
        @Header("Authorization") authHeader: String,
        @Field("api_key") apiKey: String,
        @Field("user_id") user_id: String
    ): Call<SubscriptionMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_subscription_plan_detail")
    fun getSubscriptionPlanDetail(
        @Header("Authorization") authHeader: String,
        @Field("api_key") apiKey: String,
        @Field("plan_id") planId: String
    ): Call<SubscriptionDetailRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "faqs_list")
    fun faqs(@Header("Authorization") authHeader: String,@Field("api_key") apiKey: String): Call<FAQsMainResponse>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "contact_details")
    fun contactDetails(@Header("Authorization") authHeader: String,@Field("api_key") apiKey: String): Call<ContactUsMain>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "enquiry_form")
    fun enquireForm(@Header("Authorization") authHeader: String,
        @Field("api_key") apiKey: String,
                    @Field("name") name: String,
                    @Field("phone") phone: String,
                    @Field("subject") subject: String,
                    @Field("message") message: String,
                    @Field("user_id") user_id: String,
                    @Field("email") email: String
    ): Call<FAQsMainResponse>



    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "pages_list")
    fun getPageList(
        @Field("api_key") apiKey: String,
        @Field("page_name") pageName: String
    ): Call<PageResponse>

    @POST(ROOT_URL_SUB + "get_banners")
    fun getBanners(): Call<BannerResponse>

    @Multipart
    @POST(ROOT_URL_SUB + "upload_profile_image")
    fun uploadProfileImage(
        @Header("Authorization") authHeader: String,
        @Part("user_id") userId: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<OTPVerifyResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_profile")
    fun getProfile(
        @Header("Authorization") authHeader: String,
        @Field("user_id") userId: String
    ): Call<OTPVerifyResponse>
}
