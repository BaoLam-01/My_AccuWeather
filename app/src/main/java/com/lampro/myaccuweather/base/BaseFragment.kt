package com.lampro.myaccuweather.base

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lampro.myaccuweather.R


abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding!!

    private lateinit var dialog: AlertDialog


    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(layoutInflater,container)

        val builder = AlertDialog.Builder(context)
        val getView: View = layoutInflater.inflate(R.layout.loading_dialog,null)
        builder.setView(getView)
        dialog = builder.create()
        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(0))


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun showLoadingDialog() {
        if (!dialog.isShowing) {
            dialog.apply {
                show()
                setCanceledOnTouchOutside(false)
                setCancelable(false)
            }

        }
    }
    fun hideLoadingDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

}