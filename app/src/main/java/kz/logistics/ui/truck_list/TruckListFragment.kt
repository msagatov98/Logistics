package kz.logistics.ui.truck_list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import kz.logistics.R
import kz.logistics.databinding.TruckPageBinding
import kz.logistics.util.Util.DESTINATION_CITY
import kz.logistics.util.Util.ORIGIN_CITY
import kz.logistics.util.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class TruckFragment : Fragment(R.layout.truck_page) {

    private val binding by viewBinding(TruckPageBinding::bind)
    private val viewModel by viewModel<TruckListViewModel>()

    private val loadAdapter = TruckAdapter(
        onTruckClicked = { origin, dest ->
            viewModel.onTruckClicked(origin, dest)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectViewModel()
        setupView()
    }

    private fun collectViewModel() = with(binding) {
        val navController = Navigation.findNavController(root)
        collect(viewModel.trucks, loadAdapter::submitList)
        collect(viewModel.trucksEvent) {
            when (it) {
                is TrucksEvent.OnTruckClicked -> {
                    val bundle = bundleOf(
                        ORIGIN_CITY to it.origin,
                        DESTINATION_CITY to it.dest
                    )
                    navController.navigate(R.id.nav_map, bundle)
                }
            }
        }
    }

    private fun setupView() = with(binding) {
        val navController = Navigation.findNavController(root)
        trucks.adapter = loadAdapter
        addTruckButton.setOnClickListener {
            navController.navigate(R.id.nav_add_truck)
        }
    }
}