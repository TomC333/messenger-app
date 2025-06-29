package com.sgvas21.ddadi21.messengerapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentMainBinding
import com.sgvas21.ddadi21.messengerapp.ui.auth.SigninFragment
import com.sgvas21.ddadi21.messengerapp.ui.auth.SignupFragment
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSignOut.setOnClickListener {
            SessionManager.saveSignedIn(requireContext(), false)
            SessionManager.clear(requireContext())
            requireActivity().recreate()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SigninFragment())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}