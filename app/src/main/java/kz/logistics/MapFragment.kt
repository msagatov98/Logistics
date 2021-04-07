package kz.logistics

import android.graphics.Color
import android.os.Bundle
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
import kz.logistics.Util.DESTINATION_CITY
import kz.logistics.Util.ORIGIN_CITY
import kz.logistics.Util.showToast
import kz.logistics.Util.viewBinding
import kz.logistics.databinding.MapPageBinding


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

        firstPlace = when (origin) {
            "Almaty" -> MarkerOptions().position(LatLng(43.45044, 76.73848))
            else -> null
        }

        secondPlace = when (dest) {
            "Nur-Sultan" -> MarkerOptions().position(LatLng(51.30796, 71.61213))
            "Taldyqorgan" -> MarkerOptions().position(LatLng(45.11741, 78.31902))
            "Shymkent" -> MarkerOptions().position(LatLng(42.45589, 69.68556))
            "Taraz" -> MarkerOptions().position(LatLng(43.97886, 71.35369))
            "Aqtobe" -> MarkerOptions().position(LatLng(50.40362, 57.08065))
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
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(almaty))
    }
}