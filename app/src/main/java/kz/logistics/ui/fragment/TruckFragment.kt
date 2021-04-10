package kz.logistics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kz.logistics.App
import kz.logistics.R
import kz.logistics.util.Util.DESTINATION_CITY
import kz.logistics.util.Util.ORIGIN_CITY
import kz.logistics.util.Util.viewBinding
import kz.logistics.databinding.TruckPageBinding
import kz.logistics.databinding.TruckViewHolderBinding
import kz.logistics.model.Load
import kz.logistics.util.Util.showToast


class TruckFragment : Fragment(R.layout.truck_page) {

    private val app by lazy(::initApp)

    private val binding by viewBinding(TruckPageBinding::bind)

    private val trucksList = mutableListOf<Load>()

    private lateinit var loadAdapter: TruckAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)

        val listener = object : OnTruckClickListener {
            override fun onTruckClick(origin: String, dest: String) {
                val bundle = bundleOf(
                    ORIGIN_CITY to origin,
                    DESTINATION_CITY to dest
                )
                navController.navigate(R.id.nav_map, bundle)
            }
        }

        loadAdapter = TruckAdapter(listener)

        binding.run {
            trucks.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = loadAdapter
            }

            addTruckButton.setOnClickListener {
                navController.navigate(R.id.nav_add_truck)
            }
        }

        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        app.mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trucksList.clear()
                snapshot.children.forEach {
                    it.getValue(Load::class.java)?.let { it1 -> trucksList.add(it1) }
                }
                loadAdapter.submitList(trucksList)
            }

            override fun onCancelled(error: DatabaseError) = Unit

        })
    }

    private fun initApp() : App = requireActivity().application as App
}

class TruckAdapter(val listener: OnTruckClickListener) : ListAdapter<Load, TruckAdapter.TruckViewHolder>(
    TruckItemCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val binding =
            TruckViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TruckViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TruckViewHolder(private val binding: TruckViewHolderBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(truck: Load) {
            binding.run {
                addressFromText.text = truck.originAddress
                addressToText.text = truck.destAddress
                loadingDateText.text = "Погрузка: ${truck.loadingDate}"
                priceText.text = "Цена: ${truck.price}"
                typeText.text = truck.good
                weightText.text = truck.weight.toString() + " t."
                areaText.text = truck.area.toString() + " m3"

                root.setOnClickListener {
                    listener.onTruckClick(truck.originAddress, truck.destAddress)
                }


            }
        }
    }
}

class TruckItemCallback : DiffUtil.ItemCallback<Load>() {
    override fun areItemsTheSame(oldItem: Load, newItem: Load) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Load, newItem: Load) =
        oldItem == newItem
}

data class Truck(
    val id: Int,
    val addressFrom: String,
    val addressTo: String,
    val price: String,
    val type: String,
    val weight: Double,
    val area: Double,
    val createAt: String
)

interface OnTruckClickListener {
    fun onTruckClick(origin: String, dest: String)
}