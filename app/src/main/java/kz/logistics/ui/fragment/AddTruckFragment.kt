package kz.logistics.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.logistics.App
import kz.logistics.R
import kz.logistics.databinding.AddTruckPageBinding
import kz.logistics.model.Load
import kz.logistics.util.Util.hideKeyboard
import kz.logistics.util.Util.showToast
import kz.logistics.util.Util.viewBinding
import java.util.*

const val DATE_PATTERN = "[00].[00].[0000]"

class AddTruckFragment : Fragment(R.layout.add_truck_page) {

    private val app by lazy(::initApp)

    private val binding by viewBinding(AddTruckPageBinding::bind)

    private lateinit var originAddress: String
    private lateinit var destAddress: String

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                Unit

            override fun afterTextChanged(s: Editable?) {
                binding.run {
                    val loadingDate = loadingDateInput.text.toString().trim().isNotEmpty()
                    val price = priceInput.text.toString().trim().isNotEmpty()
                    val load = loadInput.text.toString().trim().isNotEmpty()
                    val weight = weightInput.text.toString().trim().isNotEmpty()
                    val area = areaInput.text.toString().trim().isNotEmpty()

                    addLoadButton.isEnabled = loadingDate && price && load && weight && area
                }
            }
        }

        val arrayAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.cities, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val onOriginSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                originAddress = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                originAddress = parent?.getItemAtPosition(0).toString()
            }
        }

        val onDestSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                destAddress = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                destAddress = parent?.getItemAtPosition(0).toString()
            }
        }

        binding.run {
            MaskedTextChangedListener.installOn(loadingDateInput, DATE_PATTERN)

            loadingDateInput.addTextChangedListener(textWatcher)
            priceInput.addTextChangedListener(textWatcher)
            loadInput.addTextChangedListener(textWatcher)
            weightInput.addTextChangedListener(textWatcher)
            areaInput.addTextChangedListener(textWatcher)

            originAddressSpinner.adapter = arrayAdapter
            destinationAddressSpinner.adapter = arrayAdapter

            originAddressSpinner.onItemSelectedListener = onOriginSelected
            destinationAddressSpinner.onItemSelectedListener = onDestSelected

            addLoadButton.setOnClickListener {
                requireActivity().hideKeyboard()
                addLoadButton.isEnabled = false

                progressBar.visibility = View.VISIBLE

                val loadingDate = loadingDateInput.text.toString().trim()
                val price = priceInput.text.toString().trim()
                val good = loadInput.text.toString().trim()
                val weight = weightInput.text.toString().trim()
                val area = areaInput.text.toString().trim()

                val load = Load(originAddress, destAddress, loadingDate, price, good, weight, area)

                progressBar.visibility = View.GONE
                saveLoadInFirebaseDatabase(load)
            }
        }
    }

    private fun saveLoadInFirebaseDatabase(load: Load) {
        val random = Random().nextInt(999999).toString()
        app.mDatabase.child(random).setValue(load).addOnCompleteListener {
            binding.progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                resetInputs()
                AlertDialog.Builder(requireContext())
                    .setTitle("Успех")
                    .setMessage("Груз добавлен")
                    .setPositiveButton("Добавить еще груз") { dialog, _ ->  dialog.dismiss()}
                    .setNegativeButton("Выйти") { _, _ -> navController.popBackStack(R.id.nav_truck, false)}
                    .create()
                    .show()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Провал")
                    .setMessage("Груз не добавлен")
                    .create()
                    .show()
            }
        }
    }

    private fun resetInputs() {
        binding.apply {
            loadingDateInput.setText("")
            priceInput.setText("")
            loadInput.setText("")
            weightInput.setText("")
            areaInput.setText("")
        }
    }

    private fun initApp() : App = activity?.application as App

}