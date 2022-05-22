package kz.logistics.ui.add_truck

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kz.logistics.model.Deliver
import kz.logistics.model.Load
import java.util.*

class AddTruckViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : ViewModel() {

    private val date = MutableStateFlow("")
    private val area = MutableStateFlow("")
    private val good = MutableStateFlow("")
    private val price = MutableStateFlow("")
    private val weight = MutableStateFlow("")
    private val origin = MutableStateFlow("")
    private val destination = MutableStateFlow("")
    private val deliver = MutableStateFlow<Deliver?>(null)

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
        array.size == array.map { it.isEmpty() }.size
    }

    fun inputPrice(value: Editable?) {
        price.value = value.toString()
    }

    fun inputDate(value: Editable?) {
        date.value = value.toString()
    }

    fun inputWeight(value: Editable?) {
        weight.value = value.toString()
    }

    fun inputArea(value: Editable?) {
        area.value = value.toString()
    }

    fun inputLoad(value: Editable?) {
        good.value = value.toString()
    }

    fun selectOrigin(value: String) {
        origin.value = value
    }

    fun selectDestination(value: String) {
        destination.value = value
    }

    fun selectDeliver(value: Deliver) {
        deliver.value = value
    }

    fun onAddLoadClicked() {
        loading.value = true
        val load = Load(
            originAddress = origin.value,
            destAddress = destination.value,
            loadingDate = date.value,
            price = price.value,
            good = good.value,
            weight = weight.value,
            area = area.value,
            deliver = deliver.value
        )

        val random = Random().nextInt(9999999).toString()
        firebaseDatabase.child(firebaseAuth.uid.orEmpty()).child(random).setValue(load)
            .addOnCompleteListener {
                loading.value = false
                if (it.isSuccessful) {
                    viewModelScope.launch { event.emit(AddTruckAction.Success) }
                } else {
                    viewModelScope.launch { event.emit(AddTruckAction.Failure) }
                }
            }
    }
}

sealed interface AddTruckAction {
    object Success : AddTruckAction
    object Failure : AddTruckAction
}