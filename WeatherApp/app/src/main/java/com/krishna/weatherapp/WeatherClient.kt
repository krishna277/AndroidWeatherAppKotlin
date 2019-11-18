package com.krishna.weatherapp

import android.content.Context

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject

object WeatherClient {

    val API_ID = "Replace with your own appid" // Replace with your own APPID got from openweathermap.org site.
    val BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q=Singapore,SG&mode=json&appid=$API_ID&units=metric"
    val UNITS = "metric"
    private val TIMEOUT_REQUEST = 10000 // In millis
    private var queue: RequestQueue? = null


    fun getJsonData(context: Context, responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener) {

        val request = JsonObjectRequest(Request.Method.GET, BASE_URL, null, responseListener, errorListener)
        request.retryPolicy = DefaultRetryPolicy(TIMEOUT_REQUEST, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        if (queue == null) {
            queue = Volley.newRequestQueue(context)
        }
        queue!!.add(request)
    }

}
