package com.example.quiztech.services


import com.example.quiztech.model.BannerResponse
import com.example.quiztech.model.CategoryResponse
import com.example.quiztech.model.ContactUsMain
import com.example.quiztech.model.EnrollResponse
import com.example.quiztech.model.ExamQuestionsResponse
import com.example.quiztech.model.SubmitExamRequest
import com.example.quiztech.model.SubmitExamResponse
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.model.HomeSubCategoryResponse
import com.example.quiztech.model.LoginResponse
import com.example.quiztech.model.MainResponse
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.PageResponse
import com.example.quiztech.model.ResendOTPResponse
import com.example.quiztech.model.MyExamsResponse
import com.example.quiztech.model.NotificationResponse
import com.example.quiztech.model.RegistrationMainRes
import com.example.quiztech.model.SubCategoryMainRes
import com.example.quiztech.model.SubscriptionDetailRes
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.model.TestDetailsResponse
import com.example.quiztech.model.TopicMainRes

import com.google.gson.GsonBuilder
import com.example.ranksprint.common.Utils

import com.example.ranksprint.services.APIInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class ServiceManager {
    val ROOT_URL = " https://ranksprint.org/"

    companion object {
        private var dataManager: ServiceManager? = null
        const val  ROOT_URL_SUB = "api/"
        private val apiKey="the_quiz_company_7s736V2J2iB549214s40i3Lz77I0297L"
        const val SUB_ROOT_URL = ""
        @JvmStatic
        fun getDataManager(): ServiceManager {
            if (dataManager == null) {
                dataManager = ServiceManager()
            }
            return dataManager as ServiceManager
        }
    }

    private val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.callTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)
        httpClient.addInterceptor(logging)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder().baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }


    fun loginUser(cb: Callback<LoginResponse>, email:String, device_id:String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.loginUser(email, device_id)
        call.enqueue(cb)
    }

    fun verifyOTP(cb: Callback<OTPVerifyResponse>, user_id:String, otp:String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.verifyOtp(user_id,otp)
        call.enqueue(cb)
    }
    fun resendOTP(cb: Callback<ResendOTPResponse>, user_id:String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.resendOtp(user_id)
        call.enqueue(cb)
    }

    fun registerUser(cb: Callback<RegistrationMainRes>, fullName: String, email: String, phone: String, password: String, city: String, pincode: String, address: String) {
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.registerUser(fullName, email, phone, password, city, pincode, address)
        call.enqueue(cb)
    }
    fun updateProfile(cb: Callback<OTPVerifyResponse>,user_id:String,fullname:String, address:String, phone:String, gender:String, city:String, pincode:String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.updateProfile("Bearer $token", user_id,fullname,address,phone,gender, city, pincode)
        call.enqueue(cb)
    }

    fun getCategories(cb: Callback<CategoryResponse>){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getCategories("Bearer $token")
        call.enqueue(cb)
    }
    fun getSubCategories(cb: Callback<SubCategoryMainRes>, cat_id: String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubCategories("Bearer $token",cat_id)
        call.enqueue(cb,)
    }

    fun getHomeSubCategories(cb: Callback<HomeSubCategoryResponse>, cat_id: String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getHomeSubCategories("Bearer $token",cat_id)
        call.enqueue(cb,)
    }

    fun getMockItemsTestByCategory(cb: Callback<MockListMainRes>,sub_category_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockItemsTestByCategory("Bearer $token",sub_category_id)
        call.enqueue(cb,)
    }

    fun getTopic(cb: Callback<TopicMainRes>,cat_id: String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getTopicBySib("Bearer $token",cat_id)
        call.enqueue(cb,)
    }

    fun getSubTopic(cb: Callback<MainResponse>,topic_id: String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubTopicBySib("Bearer $token",topic_id)
        call.enqueue(cb,)
    }

    fun getPopularMockTests(cb: Callback<MockListMainRes>){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getPopularMockTests("Bearer $token")
        call.enqueue(cb,)
    }
    fun getMockTestByCategory(cb: Callback<MockListMainRes>,cat_id:String,user_id:String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestByCategory("Bearer $token",cat_id,user_id)
        call.enqueue(cb,)
    }

    fun getMockTestBySubCategory(cb: Callback<MockListMainRes>,sub_cat_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestBySubCategory("Bearer $token",sub_cat_id)
        call.enqueue(cb,)
    }
    fun getMockTestByTopic(cb: Callback<MockListMainRes>,topic_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestByTopic("Bearer $token",topic_id)
        call.enqueue(cb,)
    }
    fun getMockTestBySubTopic(cb: Callback<MockListMainRes>,sub_topic_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestBySubTopic("Bearer $token",sub_topic_id)
        call.enqueue(cb,)
    }
    fun getTestDetails(cb: Callback<TestDetailsResponse>, test_id:String, user_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getTestDetails("Bearer $token",test_id,user_id)
        call.enqueue(cb,)
    }

   /* fun getSubscriptionPlans(cb: Callback<SubscriptionMainRes>){
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubscriptionPlans(apiKey)
        call.enqueue(cb)
    }*/
    fun getUserActiveSubscriptions(cb: Callback<SubscriptionMainRes>,user_id: String){
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getUserActiveSubscriptions("Bearer $token", apiKey, user_id)
        call.enqueue(cb)
    }

    fun getSubscriptionPlans(cb: Callback<SubscriptionMainRes>,user_id:String) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubscriptionPlans("Bearer $token", apiKey,user_id)
        call.enqueue(cb)
    }
   /* fun subscribePlan(cb: Callback<MainResponse>,user_id: String,plan_id: String){
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.subscribePlan("Bearer $token", apiKey,user_id, plan_id  )
        call.enqueue(cb)
    }*/
    fun enrollProduct(cb: Callback<EnrollResponse>, user_id: String, product_id: String, paymentId: String){
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.subscribe_package("Bearer $token", apiKey,user_id, product_id ,paymentId,"" )
        call.enqueue(cb)
    }

    fun getExamQuestions(cb: Callback<ExamQuestionsResponse>, test_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getExamQuestions("Bearer $token",test_id)
        call.enqueue(cb,)
    }

    fun submitExam(cb: Callback<SubmitExamResponse>, request: SubmitExamRequest) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.submitExam("Bearer $token", request)
        call.enqueue(cb)
    }

    fun getScoreCard(cb: Callback<SubmitExamResponse>, user_id: String, product_id: String) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getScoreCard("Bearer $token", apiKey, user_id, product_id)
        call.enqueue(cb)
    }
    fun getMyExams(cb: Callback<MyExamsResponse>, user_id: String, type: String) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMyExams("Bearer $token", apiKey, user_id, type)
        call.enqueue(cb)
    }

    fun getSubscriptionPlanDetail(cb: Callback<SubscriptionDetailRes>, planId: String) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubscriptionPlanDetail("Bearer $token", apiKey, planId)
        call.enqueue(cb)
    }

    fun enquiryForm(cb: Callback<FAQsMainResponse>,name:String,phone:String
                            ,subject:String,message:String,user_id:String,email:String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.enquireForm("Bearer $token",apiKey,name,phone,subject,message,user_id,email)
        call.enqueue(cb,)
    }


    fun faqs(cb: Callback<FAQsMainResponse>){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.faqs("Bearer $token",apiKey)
        call.enqueue(cb,)
    }
    fun contactUs(cb: Callback<ContactUsMain>){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.contactDetails("Bearer $token",apiKey)
        call.enqueue(cb,)
    }

    fun getPageList(cb: Callback<PageResponse>, pageName: String) {
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getPageList(apiKey, pageName)
        call.enqueue(cb)
    }

    fun getBanners(cb: Callback<BannerResponse>) {
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getBanners()
        call.enqueue(cb)
    }

    fun uploadProfileImage(cb: Callback<OTPVerifyResponse>, userId: String, imageFile: File) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)

        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageFile.toImageRequestBody("image")

        val call = apiService.uploadProfileImage("Bearer $token", userIdPart, imagePart)
        call.enqueue(cb)
    }

    fun getProfile(cb: Callback<OTPVerifyResponse>, userId: String) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getProfile("Bearer $token", userId)
        call.enqueue(cb)
    }

    fun getNotifications(cb: Callback<NotificationResponse>, userId: String) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getNotifications("Bearer $token", userId)
        call.enqueue(cb)
    }


    fun File.toImageRequestBody(partName: String): MultipartBody.Part {
        val mimeType = when (this.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            else -> "image/*"
        }

        val requestFile = this.asRequestBody(mimeType.toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(partName, this.name, requestFile)
    }

}
