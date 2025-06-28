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
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentSignupBinding
import com.sgvas21.ddadi21.messengerapp.ui.components.ErrorPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSignup.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val profession = binding.profession.text.toString().trim()

            viewModel.signup(username, password, profession)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signupState.collectLatest { result ->
                    result?.let {
                        if(it.isSuccess) {
                            ErrorPopup.show(requireActivity(), "Successfully created account")
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