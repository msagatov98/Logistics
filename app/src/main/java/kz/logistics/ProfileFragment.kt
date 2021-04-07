package kz.logistics

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kz.logistics.Util.viewBinding
import kz.logistics.databinding.ProfilePageBinding

class ProfileFragment : Fragment(R.layout.profile_page) {

    private val binding by viewBinding(ProfilePageBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = activity?.application as App

        binding.run {
            profileEmailText.text = app.mAuth.currentUser?.email
            exitButton.setOnClickListener {
                app.mAuth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}