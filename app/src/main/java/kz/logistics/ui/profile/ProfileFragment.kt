package kz.logistics.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import kz.logistics.R
import kz.logistics.databinding.ProfilePageBinding
import kz.logistics.ui.login.LoginActivity
import kz.logistics.util.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.profile_page) {

    private val binding by viewBinding(ProfilePageBinding::bind)
    private val viewModel by viewModel<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect(viewModel.userEmail, binding.profileEmailText::setText)
        binding.exitButton.setOnClickListener {
            viewModel.onExitClicked()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}