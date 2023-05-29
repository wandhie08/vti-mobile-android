package com.rowantech.vti.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.rowantech.vti.R

open class BaseFragment : Fragment() {
    var dialogView : Dialog?=null
    fun progressShow(){
        dialogView = Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView!!.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView!!.setContentView(R.layout.dialog_loading)

        dialogView!!.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView!!.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView!!.show()
    }



    fun progressDismis(){
        dialogView!!.dismiss()
    }
}