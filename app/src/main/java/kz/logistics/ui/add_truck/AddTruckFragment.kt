package kz.logistics.ui.add_truck

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.logistics.R
import kz.logistics.databinding.AddTruckPageBinding
import kz.logistics.util.Util.hideKeyboard
import kz.logistics.util.collect
import kz.logistics.util.doOnItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel

const val DATE_PATTERN = "[00].[00].[0000]"

class AddTruckFragment : Fragment(R.layout.add_truck_page) {

    private val binding by viewBinding(AddTruckPageBinding::bind)
    private val viewModel by viewModel<AddTruckViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectViewModel()
        setupView()
    }

    private fun setupView() = with(binding) {
        MaskedTextChangedListener.installOn(loadingDateInput, DATE_PATTERN)
        areaInput.doAfterTextChanged(viewModel::inputArea)
        loadInput.doAfterTextChanged(viewModel::inputLoad)
        priceInput.doAfterTextChanged(viewModel::inputPrice)
        weightInput.doAfterTextChanged(viewModel::inputWeight)
        loadingDateInput.doAfterTextChanged(viewModel::inputDate)
        originAddressSpinner.doOnItemSelected(viewModel::selectOrigin)
        destinationAddressSpinner.doOnItemSelected(viewModel::selectDestination)
        addLoadButton.setOnClickListener {
            requireActivity().hideKeyboard()
            viewModel.onAddLoadClicked()
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cities,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            originAddressSpinner.adapter = it
            destinationAddressSpinner.adapter = it
        }
    }

    private fun collectViewModel() = with(binding) {
        val navController = Navigation.findNavController(root)
        collect(viewModel.enabled, addLoadButton::setEnabled)
        collect(viewModel.loading, progressBar::isVisible::set)
        collect(viewModel.event) {
            when (it) {
                AddTruckAction.Success -> {
                    resetInputs()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Успех")
                        .setMessage("Груз добавлен")
                        .setPositiveButton("Добавить еще груз") { dialog, _ -> dialog.dismiss() }
                        .setNegativeButton("Выйти") { _, _ ->
                            navController.popBackStack(R.id.nav_truck, false)
                        }
                        .create()
                        .show()
                }
                AddTruckAction.Failure -> AlertDialog.Builder(requireContext())
                    .setTitle("Провал")
                    .setMessage("Груз не добавлен")
                    .create()
                    .show()
            }
        }
    }

    private fun resetInputs() = with(binding) {
        loadingDateInput.setText("")
        weightInput.setText("")
        priceInput.setText("")
        loadInput.setText("")
        areaInput.setText("")
    }

}