package com.sgvas21.ddadi21.messengerapp.ui.mainScreens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.sgvas21.ddadi21.messengerapp.MainActivity
import com.sgvas21.ddadi21.messengerapp.R
import com.sgvas21.ddadi21.messengerapp.data.model.User
import com.sgvas21.ddadi21.messengerapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    private var currentUser: User? = null
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.profileImage.setImageURI(uri)

            // Upload the image to Firebase Storage
            currentUser?.let { user ->
                viewModel.uploadProfileImage(uri, user)
            }
        }
    }

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
        setupSignOutButton()
        setupUpdateButton()
        binding.profileImage.setOnClickListener {
            openGallery()
        }

        observeViewModel()
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun setupSignOutButton() {
        binding.btnSignOut.setOnClickListener {
            (requireActivity() as? MainActivity)?.handleSignOut()
        }
    }

    private fun setupUpdateButton() {
        binding.btnUpdate.setOnClickListener {
            currentUser?.let { user ->
                showUpdateDialog(user)
            }
        }
    }

    private fun showUpdateDialog(user: User) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Profile")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val usernameEdit = EditText(requireContext())
        usernameEdit.hint = "Username"
        usernameEdit.setText(user.username)
        // Enable username editing
        usernameEdit.isEnabled = true
        layout.addView(usernameEdit)

        val professionEdit = EditText(requireContext())
        professionEdit.hint = "Profession"
        professionEdit.setText(user.profession)
        layout.addView(professionEdit)

        builder.setView(layout)

        builder.setPositiveButton("Update") { _, _ ->
            val newUsername = usernameEdit.text.toString().trim()
            val newProfession = professionEdit.text.toString().trim()

            if (newUsername.isNotEmpty() && newProfession.isNotEmpty()) {
                val updatedUser = user.copy(
                    username = newUsername,
                    profession = newProfession
                )
                viewModel.updateUserInfo(updatedUser)
            } else {
                Toast.makeText(requireContext(), "Username and profession cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userState.collect { user ->
                user?.let {
                    currentUser = it
                    updateUI(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.btnUpdate.isEnabled = !isLoading
                binding.btnSignOut.isEnabled = !isLoading
                // Also disable profile image selection during loading
                binding.profileImage.isEnabled = !isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateSuccess.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    viewModel.clearUpdateSuccess()
                }
            }
        }
    }

    private fun updateUI(user: User) {
        binding.profileName.text = user.username
        binding.profileRole.text = user.profession

        binding.profileImage.load(user.profileImageUrl) {
            placeholder(R.drawable.avatar_image_placeholder)
            error(R.drawable.avatar_image_placeholder)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
