package kz.logistics.ui.truck_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kz.logistics.model.Load

class TruckListViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
    ) : ViewModel() {

    val trucks = MutableStateFlow(emptyList<Load>())
    val trucksEvent = MutableSharedFlow<TrucksEvent>()

    init {
        loadTrucks()
    }

    fun onTruckClicked(origin: String, dest: String) = viewModelScope.launch {
        trucksEvent.emit(TrucksEvent.OnTruckClicked(origin, dest))
    }

    fun onDeleteClicked(load: Load) {
        firebaseDatabase.child(firebaseAuth.uid.orEmpty()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (appleSnapshot in snapshot.children) {
                    if (appleSnapshot.getValue(Load::class.java) == load) {
                        appleSnapshot.ref.removeValue()
                        return
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit

        })
    }

    private fun loadTrucks() {
        firebaseDatabase.child(firebaseAuth.uid.orEmpty()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trucks.value = snapshot.children.mapNotNull { it.getValue(Load::class.java) }
            }

            override fun onCancelled(error: DatabaseError) = Unit

        })
    }

}

sealed interface TrucksEvent {
    class OnTruckClicked(val origin: String, val dest: String) : TrucksEvent
}