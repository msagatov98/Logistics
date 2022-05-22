package kz.logistics.ui.truck_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.logistics.databinding.TruckViewHolderBinding
import kz.logistics.model.Load

class TruckAdapter(
    private val onTruckClicked: (origin: String, dest: String) -> Unit,
    private val onDeleteClicked: (load: Load) -> Unit
) : ListAdapter<Load, TruckAdapter.ViewMolder>(TruckItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewMolder(
        TruckViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewMolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewMolder(
        private val binding: TruckViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(truck: Load) = with(binding) {
            addressFromText.text = truck.originAddress
            addressToText.text = truck.destAddress
            loadingDateText.text = "Погрузка: ${truck.loadingDate}"
            priceText.text = "Цена: ${truck.price}"
            typeText.text = truck.good
            weightText.text = truck.weight.toString() + " t."
            areaText.text = truck.area.toString() + " m3"
            deleteImageView.setOnClickListener {
                onDeleteClicked(truck)
            }

            root.setOnClickListener {
                onTruckClicked(truck.originAddress, truck.destAddress)
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