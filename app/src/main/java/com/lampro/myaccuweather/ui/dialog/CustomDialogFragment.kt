package com.lampro.myaccuweather.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.lampro.myaccuweather.R

class CustomDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom,null)

        return AlertDialog.Builder(requireContext()).setView(dialogView).create()
    }
}