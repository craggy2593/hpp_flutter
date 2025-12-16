package com.example.hpp_flutter

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.realexpayments.hpp.HPPManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

/** HppFlutterPlugin */
class HppFlutterPlugin :
    FlutterPlugin,
    MethodCallHandler {
    // The MethodChannel that will the communication between Flutter and native Android
    //
    // This local reference serves to register the plugin with the Flutter Engine and unregister it
    // when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var activity: Activity
    private lateinit var context: Context
    private lateinit var result: Result

    companion object {
        const val PAYMENT_REQUEST_CODE = 1337
        const val RESULT_ERROR = 2
        const val RESULT_FIELD_SUCCESS = "success"
        const val RESULT_FIELD_CODE = "code"
        const val RESULT_FIELD_RESULT = "result"
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "hpp_flutter")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(
        call: MethodCall,
        result: Result
    ) {
        this.result = result

        if (call.method == "showPaymentWindow") {
           val arguments: ArrayList<Any>? = call.arguments<ArrayList<Any>?>()
                val data = arguments.asPayloadData()

                // validate
                if (data == null
                        || data.hppURL.isEmpty()) {
                    result.success(mutableMapOf(RESULT_FIELD_SUCCESS to false, RESULT_FIELD_RESULT to "No or invalid configuration provided", RESULT_FIELD_CODE to 9999))
                    return
                }

                val intent = Intent(context, PaymentActivity::class.java)
                val bundle = Bundle()
                bundle.putString(HPPManager.HPPURL, data.hppURL)

                intent.putExtra("hppData", bundle)

                activity.startActivityForResult(intent, PAYMENT_REQUEST_CODE)
        } else if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return when (requestCode) {
            PAYMENT_REQUEST_CODE -> handleResult(resultCode, data)
            else -> return false
        }
    }

    private fun handleResult(resultCode: Int, data: Intent?): Boolean {
        when (resultCode) {
            RESULT_OK -> result.success(mutableMapOf(RESULT_FIELD_SUCCESS to true, RESULT_FIELD_RESULT to "successful transaction", RESULT_FIELD_CODE to 200))
            RESULT_CANCELED -> result.success(mutableMapOf(RESULT_FIELD_SUCCESS to false, RESULT_FIELD_RESULT to "Cancelled", RESULT_FIELD_CODE to 9998))
            RESULT_ERROR -> result.success(mutableMapOf(RESULT_FIELD_SUCCESS to false, RESULT_FIELD_RESULT to data?.extras?.get("error"), RESULT_FIELD_CODE to 9999))
        }
        return true
    }
}

data class PayloadData(@SerializedName("HPPURL") val hppURL: String
)

fun ArrayList<Any>?.asPayloadData(): PayloadData? {
    val data: HashMap<String, Any> = this[0] as HashMap<String, Any>
    return data?.toDataClass<PayloadData>()
}

val gson = Gson()

//convert a data class to a map
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

//convert a map to a data class
inline fun <reified T> Map<String, Any>.toDataClass(): T {
    return convert()
}

//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}