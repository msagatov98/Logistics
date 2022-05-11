package kz.logistics.ui.fragment

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kz.logistics.R
import kz.logistics.databinding.MapPageBinding
import kz.logistics.model.GoogleMapDTO
import kz.logistics.util.Util.DESTINATION_CITY
import kz.logistics.util.Util.ORIGIN_CITY
import kz.logistics.util.Util.viewBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

class MapFragment : Fragment(R.layout.map_page), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var firstPlace: MarkerOptions? = null
    private var secondPlace: MarkerOptions? = null
    private var currentPolyline: Polyline? = null

    private val binding by viewBinding(MapPageBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        val origin = arguments?.getString(ORIGIN_CITY)
        val dest = arguments?.getString(DESTINATION_CITY)

        Log.v("qwer", origin.orEmpty())
        Log.v("qwer", dest.orEmpty())

        firstPlace = when (origin) {
            "Алматы" ->             MarkerOptions().position(LatLng(43.22411, 76.85147))
            "Нур-Султан" ->         MarkerOptions().position(LatLng(51.15992, 71.45878))
            "Шымкент" ->            MarkerOptions().position(LatLng(42.34180, 69.58774))
            "Актобе" ->             MarkerOptions().position(LatLng(50.28405, 57.16461))
            "Караганда" ->          MarkerOptions().position(LatLng(49.801749777706256, 73.11793874364142))
            "Тараз" ->              MarkerOptions().position(LatLng(42.952208536236334, 71.40176961504426))
            "Павлодар" ->           MarkerOptions().position(LatLng(52.30265229441416, 77.00722041225545))
            "Усть-Каменогорск" ->   MarkerOptions().position(LatLng(50.142087218082416, 82.57483056414947))
            "Семей" ->              MarkerOptions().position(LatLng(50.59058968209038, 80.22375643448868))
            "Атырау" ->             MarkerOptions().position(LatLng(47.27217883969216, 51.94494794148447))
            "Костанай" ->           MarkerOptions().position(LatLng(53.33778864146027, 63.56848289374518))
            "Кызылорда" ->          MarkerOptions().position(LatLng(44.98853385006499, 65.58996738364523))
            "Уральск" ->            MarkerOptions().position(LatLng(51.227144778040135, 51.38657142384738))
            "Петропавловск" ->      MarkerOptions().position(LatLng(54.92271003538278, 69.13537051474118))
            "Актау" ->              MarkerOptions().position(LatLng(43.65921539254773, 51.19951377821448))
            "Темиртау" ->           MarkerOptions().position(LatLng(50.0506026573525, 72.9718936096262))
            "Туркестан" ->          MarkerOptions().position(LatLng(43.30426945310669, 68.23495380087446))
            "Кокшетау" ->           MarkerOptions().position(LatLng(53.343962401682866, 69.38196142722234))
            "Талдыкорган" ->        MarkerOptions().position(LatLng(45.0809011791341, 78.4572886893096))
            "Экибастуз" ->          MarkerOptions().position(LatLng(51.72526539240014, 75.31737318970674))
            "Рудный" ->             MarkerOptions().position(LatLng(52.97252439869143, 63.11211438211817))
            else -> null
        }

        secondPlace = when (dest) {
            "Алматы" ->             MarkerOptions().position(LatLng(43.22411, 76.85147))
            "Нур-Султан" ->         MarkerOptions().position(LatLng(51.15992, 71.45878))
            "Шымкент" ->            MarkerOptions().position(LatLng(42.34180, 69.58774))
            "Актобе" ->             MarkerOptions().position(LatLng(50.28405, 57.16461))
            "Караганда" ->          MarkerOptions().position(LatLng(49.801749777706256, 73.11793874364142))
            "Тараз" ->              MarkerOptions().position(LatLng(42.952208536236334, 71.40176961504426))
            "Павлодар" ->           MarkerOptions().position(LatLng(52.30265229441416, 77.00722041225545))
            "Усть-Каменогорск" ->   MarkerOptions().position(LatLng(50.142087218082416, 82.57483056414947))
            "Семей" ->              MarkerOptions().position(LatLng(50.59058968209038, 80.22375643448868))
            "Атырау" ->             MarkerOptions().position(LatLng(47.27217883969216, 51.94494794148447))
            "Костанай" ->           MarkerOptions().position(LatLng(53.33778864146027, 63.56848289374518))
            "Кызылорда" ->          MarkerOptions().position(LatLng(44.98853385006499, 65.58996738364523))
            "Уральск" ->            MarkerOptions().position(LatLng(51.227144778040135, 51.38657142384738))
            "Петропавловск" ->      MarkerOptions().position(LatLng(54.92271003538278, 69.13537051474118))
            "Актау" ->              MarkerOptions().position(LatLng(43.65921539254773, 51.19951377821448))
            "Темиртау" ->           MarkerOptions().position(LatLng(50.0506026573525, 72.9718936096262))
            "Туркестан" ->          MarkerOptions().position(LatLng(43.30426945310669, 68.23495380087446))
            "Кокшетау" ->           MarkerOptions().position(LatLng(53.343962401682866, 69.38196142722234))
            "Талдыкорган" ->        MarkerOptions().position(LatLng(45.0809011791341, 78.4572886893096))
            "Экибастуз" ->          MarkerOptions().position(LatLng(51.72526539240014, 75.31737318970674))
            "Рудный" ->             MarkerOptions().position(LatLng(52.97252439869143, 63.11211438211817))
            else -> null
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val almaty = LatLng(43.23, 76.85)

        if (firstPlace != null && secondPlace != null) {
            mMap.addMarker(firstPlace)
            mMap.addMarker(secondPlace)

            currentPolyline?.remove()

            currentPolyline = mMap.addPolyline(
                PolylineOptions()
                    .add(firstPlace!!.position, secondPlace!!.position)
                    .width(5F)
                    .color(ContextCompat.getColor(requireContext(), R.color.dark_blue))
            )

            val url = getDestinationUrl(firstPlace!!.position, secondPlace!!.position)
            GetDirection(url, mMap).execute()

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(almaty))
    }

    private fun getDestinationUrl(origin: LatLng, dest: LatLng) =
        "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&mode=driving"

}

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