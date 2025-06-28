package com.sgvas21.ddadi21.messengerapp.ui.components

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.sgvas21.ddadi21.messengerapp.databinding.DialogErrorPopupBinding

object ErrorPopup {
    fun show(activity: Activity, message: String) {
        val binding = DialogErrorPopupBinding.inflate(LayoutInflater.from(activity))

        val dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.tvErrorMessage.text = message
        binding.btnClose.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}