package com.krishna.weatherapp

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley

class DataAdapter(private val context: Context, private val weatherModelList: List<WeatherModel>) : RecyclerView.Adapter<DataAdapter.WeatherDataHolder>() {

    //private RequestQueue mRequestQueue;
    private val mImageLoader: ImageLoader

    init {
        val mRequestQueue = Volley.newRequestQueue(context)
        mImageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
            private val mCache = LruCache<String, Bitmap>(10)

            override fun putBitmap(url: String, bitmap: Bitmap) {
                mCache.put(url, bitmap)
            }

            override fun getBitmap(url: String): Bitmap? {
                return mCache.get(url)
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return WeatherDataHolder(view)
    }


    override fun onBindViewHolder(holder: WeatherDataHolder, position: Int) {
        val rd = weatherModelList[position]

        val url = "http://openweathermap.org/img/wn/" + rd.icon + "@2x.png"
        holder.nvIcon.setImageUrl(url, mImageLoader)

        holder.tvDate.text = rd.dateTxt
        // holder.tvDesc.setText("Weather:" + rd.getDescription());
        holder.tvDesc.text = rd.description
        val tempStr = rd.temp.toString() + " \u2103"
        holder.tvTemp.text = tempStr
        val humidStr = "Humidity:" + rd.humid + " %"
        holder.tvHumid.text = humidStr
        val windStr = "Wind Speed:" + rd.windSpeed + " m/s"
        holder.tvWindSpeed.text = windStr
    }

    override fun getItemCount(): Int {
        return weatherModelList.size
    }

    inner class WeatherDataHolder internal constructor( itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var nvIcon: NetworkImageView = itemView.findViewById(R.id.icon_id)
        internal var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        internal var tvTemp: TextView = itemView.findViewById(R.id.tv_temp)
        internal var tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        internal var tvHumid: TextView = itemView.findViewById(R.id.tv_humid)
        internal var tvWindSpeed: TextView = itemView.findViewById(R.id.tv_wind_speed)

    }

}
