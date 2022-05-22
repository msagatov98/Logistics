package kz.logistics.ui.add_truck

import android.text.Editable
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class AddTruckViewModel(
    private val firebaseDatabase: DatabaseReference
) : ViewModel() {

    val date = MutableStateFlow("")
    val area = MutableStateFlow("")
    val good = MutableStateFlow("")
    val price = MutableStateFlow("")
    val weight = MutableStateFlow("")
    val origin = MutableStateFlow("")
    val destination = MutableStateFlow("")

    val event = MutableSharedFlow<AddTruckAction>()

    val loading = MutableStateFlow(false)
    val enabled = combine(
        date,
        area,
        good,
        price,
        weight,
        origin,
        destination
    ) { array ->
        array.size == array.map { it.isNotEmpty() }.size
    }

    fun inputPrice(value: Editable?) {
        price.value = value.toString()
    }

    fun inputDate(value: Editable?) {
        price.value = value.toString()
    }

    fun inputWeight(value: Editable?) {
        price.value = value.toString()
    }

    fun inputArea(value: Editable?) {
        price.value = value.toString()
    }

    fun inputLoad(value: Editable?) {
        price.value = value.toString()
    }

    fun selectOrigin(value: String) {
        price.value = value
    }

    fun selectDestination(value: String) {
        price.value = value
    }

    fun onAddLoadClicked() {

    }
}

sealed interface AddTruckAction {
    object Success : AddTruckAction
    object Failure : AddTruckAction
}