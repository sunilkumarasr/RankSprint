package com.example.quiztech.model

import com.google.gson.annotations.SerializedName

data class SubscriptionMainRes(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("is_active_plan") var isActivePlan: Boolean? = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : Any? = null
)

data class SubscriptionDetailRes(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : SubscriptionDetail? = null
)

data class SubscriptionDetail(
    @SerializedName("plan_name"     ) var planName     : String? = null,
    @SerializedName("description"   ) var description   : String? = null,
    @SerializedName("duration_days" ) var durationDays : String? = null,
    @SerializedName("price"         ) var price        : String? = null,
    @SerializedName("status"        ) var status       : String? = null,
    @SerializedName("renewal_date"  ) var renewalDate  : String? = null,
    @SerializedName("included"      ) var included     : ArrayList<String> = arrayListOf()
)

data class ActivePlan(
    @SerializedName("id") var id: String? = null,
    @SerializedName("plan_name") var planName: String? = null,
    @SerializedName("plan_desc") var planDesc: String? = null,
    @SerializedName("activated_date") var activatedDate: String? = null,
    @SerializedName("expiry_date") var expiryDate: String? = null,
    @SerializedName("expiry_days") var expiryDays: Int? = null,
    @SerializedName("price") var price: String? = null
)

data class SubscriptionPlan(

    @SerializedName("id"            ) var id           : String? = null,
@SerializedName("plan_title"    ) var planTitle    : String? = null,
@SerializedName("plan_desc"     ) var planDesc     : String? = null,
@SerializedName("duration_days" ) var durationDays : String? = null,
@SerializedName("price"         ) var price        : String? = null,
@SerializedName("paid_qnty"     ) var paidQnty     : String? = null,
@SerializedName("free_qnty"     ) var freeQnty     : String? = null
)
