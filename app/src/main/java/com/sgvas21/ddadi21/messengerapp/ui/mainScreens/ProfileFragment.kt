package com.sgvas21.ddadi21.messengerapp.ui.mainScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.core.RepoManager.clear
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentProfileBinding
import com.sgvas21.ddadi21.messengerapp.ui.auth.SigninFragment
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager.clear
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfile()
        setupSignOutButton()
    }

    private fun setupProfile() {
    }

    private fun setupSignOutButton() {
        binding.btnSignOut.setOnClickListener {
            SessionManager.saveSignedIn(requireContext(), false)
            SessionManager.clear(requireContext())
            requireActivity().recreate()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SigninFragment())
                .commit()
        }
        //არ მუშაობს
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
