package kz.logistics.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kz.logistics.ui.add_truck.AddTruckViewModel
import kz.logistics.ui.login.LoginViewModel
import kz.logistics.ui.profile.ProfileViewModel
import kz.logistics.ui.registration.RegistrationViewModel
import kz.logistics.ui.truck_list.TruckListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val logisticsModule = module {

    single {
        FirebaseAuth.getInstance()
    }

    single {
        FirebaseDatabase.getInstance().getReference("load")
    }

    viewModel {
        LoginViewModel(firebaseAuth = get())
    }

    viewModel {
        ProfileViewModel(firebaseAuth = get())
    }

    viewModel {
        RegistrationViewModel(firebaseAuth = get())
    }

    viewModel {
        AddTruckViewModel(firebaseDatabase = get())
    }

    viewModel {
        TruckListViewModel(firebaseDatabase = get())
    }
    
}