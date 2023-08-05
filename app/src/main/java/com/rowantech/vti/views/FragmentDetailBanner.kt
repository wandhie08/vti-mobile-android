package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.CancelOrderRequest
import com.rowantech.vti.data.model.response.BannersItem
import com.rowantech.vti.data.model.response.CreateOrderResponse
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.databinding.DialogImageBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

class FragmentDetailBanner : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<DialogImageBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)


    // on below line we are creating
    // a variable for qr encoder.
    lateinit var qrEncoder: QRGEncoder
    internal lateinit var data: BannersItem
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogImageBinding.inflate(inflater, container, false)
        context ?: return binding.root

        data = Gson().fromJson(arguments?.getString("data"), BannersItem::class.java)
        Glide.with(this).load(data.banner).into(binding.imageBanner)


        binding.imageBanner.swipeToDismissEnabled = true
        binding.imageBanner.onDismiss = {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}