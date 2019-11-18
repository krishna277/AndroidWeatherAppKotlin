package com.krishna.weatherapp

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.android.volley.Response

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val weatherList = ArrayList<WeatherModel>()
    private var dataAdapter: DataAdapter? = null

    internal lateinit var mydb: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mydb = DBHelper(this)

        val progressbar = findViewById<ProgressBar>(R.id.progress_bar)
        @Suppress("DEPRECATION") val color = resources.getColor(R.color.colorPrimary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            progressbar.indeterminateDrawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
        } else {
            @Suppress("DEPRECATION")
            progressbar.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshId)
        swipeRefreshLayout.setOnRefreshListener {
            loadDataFromUrl()
            swipeRefreshLayout.isRefreshing = false
        }

        val rvData = findViewById<RecyclerView>(R.id.rvData)
        rvData.addItemDecoration(DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL))

        dataAdapter = DataAdapter(this@MainActivity, weatherList)
        rvData.layoutManager = LinearLayoutManager(this@MainActivity)
        rvData.adapter = dataAdapter

        loadDataFromUrl()
    }

    fun showErrorDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Error!")
        builder.setMessage("Error getting weather data!")
        builder.setPositiveButton("RETRY") { _, _ -> loadDataFromUrl() }

        val dialog = builder.create()
        dialog.show()
    }

    fun loadDataFromSQLite() {
        val a = mydb.allData

        if (a.size == 0) {
            findViewById<View>(R.id.loadingPanel).visibility = View.GONE
            showErrorDialog()
            return
        }
        val str = a[0]

        try {
            val dbjson = JSONObject(str)
            if (dbjson.length() > 0) {
                val arr = dbjson.getJSONArray("list")

                for (i in 0 until arr.length()) {
                    val wm = WeatherModel()

                    val dateTxt = arr.getJSONObject(i).getString("dt_txt")
                    wm.dateTxt = DateHelper.getLocalTimeTxt(dateTxt)
                    val minTemp = arr.getJSONObject(i).getJSONObject("main").getInt("temp_min")
                    wm.minTemp = minTemp
                    val maxTemp = arr.getJSONObject(i).getJSONObject("main").getInt("temp_max")
                    wm.maxTemp = maxTemp
                    val humid = arr.getJSONObject(i).getJSONObject("main").getInt("humidity")
                    wm.humid = humid
                    val desc = arr.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")
                    wm.description = desc
                    val ic = arr.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
                    wm.icon = ic
                    val tmp = arr.getJSONObject(i).getJSONObject("main").getInt("temp")
                    wm.temp = tmp
                    val windSpeed = arr.getJSONObject(i).getJSONObject("wind").getDouble("speed")
                    wm.windSpeed = windSpeed.toInt()

                    weatherList.add(wm)
                }
                dataAdapter!!.notifyDataSetChanged()
                findViewById<View>(R.id.loadingPanel).visibility = View.GONE
            }
        } catch (e: JSONException) {
            findViewById<View>(R.id.loadingPanel).visibility = View.GONE
            showErrorDialog()
            e.printStackTrace()
        }
    }

    fun loadDataFromUrl() {
        findViewById<View>(R.id.loadingPanel).visibility = View.VISIBLE
        weatherList.clear()
        dataAdapter!!.notifyDataSetChanged()
        WeatherClient.getJsonData(this@MainActivity,
                Response.Listener<JSONObject> { response ->
                    try {
                        if (response.length() > 0) {
                            val arr = response.getJSONArray("list")

                            for (i in 0 until arr.length()) {
                                val wm = WeatherModel()

                                val dateTxt = arr.getJSONObject(i).getString("dt_txt")
                                wm.dateTxt = DateHelper.getLocalTimeTxt(dateTxt)
                                val minTemp = arr.getJSONObject(i).getJSONObject("main").getInt("temp_min")
                                wm.minTemp = minTemp
                                val maxTemp = arr.getJSONObject(i).getJSONObject("main").getInt("temp_max")
                                wm.maxTemp = maxTemp
                                val humid = arr.getJSONObject(i).getJSONObject("main").getInt("humidity")
                                wm.humid = humid
                                val desc = arr.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")
                                wm.description = desc
                                val ic = arr.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
                                wm.icon = ic
                                val tmp = arr.getJSONObject(i).getJSONObject("main").getInt("temp")
                                wm.temp = tmp
                                val windSpeed = arr.getJSONObject(i).getJSONObject("wind").getDouble("speed")
                                wm.windSpeed = windSpeed.toInt()

                                weatherList.add(wm)
                            }

                            val a = mydb.allData
                            if (a.size == 0) {
                                mydb.insertData(response.toString())
                            } else {
                                mydb.updateData(1, response.toString())
                            }
                            dataAdapter!!.notifyDataSetChanged()
                            findViewById<View>(R.id.loadingPanel).visibility = View.GONE

                        } else {
                            showErrorDialog()
                            findViewById<View>(R.id.loadingPanel).visibility = View.GONE
                            Toast.makeText(this@MainActivity, "Error getting response! Restart the App!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        showErrorDialog()
                        findViewById<View>(R.id.loadingPanel).visibility = View.GONE
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                // Load from SQLite Database
                loadDataFromSQLite()
                error.printStackTrace()
        })
    }
}
