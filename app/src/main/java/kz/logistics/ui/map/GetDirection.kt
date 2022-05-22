package kz.logistics.ui.map

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kz.logistics.model.GoogleMapDTO
import okhttp3.OkHttpClient
import okhttp3.Request

class GetDirection(private val url: String, private val mMap: GoogleMap) : AsyncTask<Void, Void, List<List<LatLng>>>() {

    override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val data = response.body.toString()
        val result = ArrayList<List<LatLng>>()
        try {
            val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)

            val path = ArrayList<LatLng>()

            for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].startLocation.lat.toDouble(),
                    respObj.routes[0].legs[0].steps[i].startLocation.lng.toDouble())

                val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].endLocation.lat.toDouble(),
                    respObj.routes[0].legs[0].steps[i].endLocation.lng.toDouble())

                path.add(startLatLng)
                path.add(endLatLng)
            }

            result.add(path)
        }catch (e : Exception) {
            e.printStackTrace()
        }
        return result
    }

    override fun onPostExecute(result: List<List<LatLng>>) {
        super.onPostExecute(result)

        val lineOption = PolylineOptions()
        for (i in result.indices) {

            lineOption.addAll(result[i])

            lineOption.width(10F)
            lineOption.color(Color.BLUE)
            lineOption.geodesic(true)
        }

        mMap.addPolyline(lineOption)
    }

}