package com.sgvas21.ddadi21.messengerapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentSigninBinding
import com.sgvas21.ddadi21.messengerapp.ui.MainFragment
import com.sgvas21.ddadi21.messengerapp.ui.components.ErrorPopup
import com.sgvas21.ddadi21.messengerapp.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SigninFragment: Fragment() {
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SigninViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var currentUsername = ""

        binding.btnSignin.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            currentUsername = username

            viewModel.signin(username, password)
        }

        binding.btnGoToSignup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, SignupFragment())
                .commit()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signinState.collectLatest { result ->
                    result?.let {
                        if(it.isSuccess) {
                            SessionManager.saveSignedIn(requireContext(), true)
                            SessionManager.saveUsername(requireContext(), currentUsername)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view, MainFragment())
                                .commit()
                        }else {
                            ErrorPopup.show(requireActivity(), it.exceptionOrNull()?.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
