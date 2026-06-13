package com.example.ranksprint.common

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import okhttp3.internal.toLongOrDefault
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
   // val IMG_ROOT_URL = "https://sreezzyhomeservices.com/"

    var access_token=""
    var user_id: String? = ""
    val IS_REGISTERED="is_registered"

    fun hideKeyboard(view: View, context: Context) {
        view.post {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    }
    fun isNull( value: String?):String {

        if (value==null || value=="null"){
            return ""
        }
        return value
    }
    fun isStringToInt( value: String?):Int {

        if (value==null || value=="null"|| value.isEmpty()){
            return 0
        }
        return value.toInt()
    }
    fun isStringToLong( value: String?):Long {

        if (value==null || value=="null"|| value.isEmpty()){
            return 0
        }
        return value.toLongOrDefault(0)
    }
    fun isStringToDouble( value: String?):Double {

        if (value==null || value=="null"|| value.isEmpty()){
            return 0.0
        }
        return value.toDouble()
    }

    fun showAlertDialog(context: Context, title: String, msg: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Close the dialog
            }


        // Create and show the AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()


    }
    fun openDialog(context: Context): ProgressDialog {
        var dialog: ProgressDialog? = null
        dialog = ProgressDialog(context).apply {
            setMessage("Please wait...")
            setCancelable(false)
            show()
        }
        return dialog
    }

    fun isNetworkAvailable(context: Context): Boolean {
        var objConnectivityManager: ConnectivityManager?
        var isNetworkAvailable = false

        try {
            objConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (objConnectivityManager!!.activeNetworkInfo != null && objConnectivityManager!!.activeNetworkInfo!!
                    .isAvailable
                && objConnectivityManager!!.activeNetworkInfo!!.isConnected
            ) {
                return true.also { isNetworkAvailable = it }
            }
        } catch (e: java.lang.Exception) {
            e.message
        } finally {
            objConnectivityManager = null
        }
        return isNetworkAvailable
    }
     fun saveData(context: Context, key: String, value: Any?) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("PropertyZoneSP", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
        }
        editor.apply()
    }
    fun clearPref(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("PropertyZoneSP", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()


    }

    fun getData(context: Context, key: String, defaultValue: Any): Any? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("PropertyZoneSP", Context.MODE_PRIVATE)
        return when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue)
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            else -> null
        }
    }

    fun closeDialog(dialog: ProgressDialog) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }

    }
    fun dateYYYYMMMDD():String{
        val sdf = SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault())
        return  sdf.format(Date())
    }
}