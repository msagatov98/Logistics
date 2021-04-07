package kz.logistics

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
import kz.logistics.Util.DESTINATION_CITY
import kz.logistics.Util.ORIGIN_CITY
import kz.logistics.Util.viewBinding
import kz.logistics.databinding.TruckPageBinding
import kz.logistics.databinding.TruckViewHolderBinding


class TruckFragment : Fragment(R.layout.truck_page) {

    private val binding by viewBinding(TruckPageBinding::bind)

    private val trucksList = mutableListOf(
        Truck(1, "Almaty", "Nur-Sultan", "100.000", "Шоколад", 2.0, 1.3, "21.01.2019"),
        Truck(1, "Almaty", "Shymkent", "100.000", "Шоколад", 2.0, 1.3, "21.01.2019"),
        Truck(1, "Almaty", "Taldyqorgan", "100.000", "Шоколад", 2.0, 1.3, "21.01.2019"),
        Truck(1, "Almaty", "Taraz", "100.000", "Шоколад", 2.0, 1.3, "21.01.2019"),
        Truck(1, "Almaty", "Aqtobe", "100.000", "Шоколад", 2.0, 1.3, "21.01.2019")
    )

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

        binding.run {
            trucks.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TruckAdapter(listener).apply {
                    submitList(trucksList)
                }
            }
        }
    }
}

class TruckAdapter(val listener: OnTruckClickListener) : ListAdapter<Truck, TruckAdapter.TruckViewHolder>(
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

        fun bind(truck: Truck) {
            binding.run {
                addressFromText.text = truck.addressFrom
                addressToText.text = truck.addressTo
                loadingDateText.text = "Погрузка: ${truck.createAt}"
                priceText.text = "Цена: ${truck.price}"
                typeText.text = truck.type
                weightText.text = truck.weight.toString() + " t."
                areaText.text = truck.area.toString() + " m3"

                root.setOnClickListener {
                    listener.onTruckClick(truck.addressFrom, truck.addressTo)
                }
            }
        }
    }
}

class TruckItemCallback : DiffUtil.ItemCallback<Truck>() {
    override fun areItemsTheSame(oldItem: Truck, newItem: Truck) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Truck, newItem: Truck) =
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