package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.CancelOrderRequest
import com.rowantech.vti.data.model.request.CreateOrderRequest
import com.rowantech.vti.data.model.response.CreateOrderResponse
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.data.model.response.MessageResponse
import com.rowantech.vti.databinding.FragmentProductPaymentBinding
import com.rowantech.vti.databinding.FragmentQrisBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject


class FragmentQRIS : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentProductPaymentBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    var cancelOrderRequest = CancelOrderRequest()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    internal lateinit var data: EventsItem
    internal lateinit var createOrderResponse: CreateOrderResponse
    lateinit var bitmap: Bitmap

    // on below line we are creating
    // a variable for qr encoder.
    lateinit var qrEncoder: QRGEncoder
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQrisBinding.inflate(inflater, container, false)
        context ?: return binding.root
        createOrderResponse = Gson().fromJson(arguments?.getString("data"), CreateOrderResponse::class.java)
        // on below line we are getting service for window manager
        val windowManager: WindowManager = requireContext().getSystemService(WINDOW_SERVICE) as WindowManager

        // on below line we are initializing a
        // variable for our default display
        val display: Display = windowManager.defaultDisplay

        // on below line we are creating a variable
        // for point which is use to display in qr code
        val point: Point = Point()
        display.getSize(point)

        // on below line we are getting
        // height and width of our point
        val width = point.x
        val height = point.y

        // on below line we are generating
        // dimensions for width and height
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        // on below line we are initializing our qr encoder
        qrEncoder = QRGEncoder(createOrderResponse.data!!.qrisResponse!!.rawqr, null, QRGContents.Type.TEXT, dimen)

        object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.timer.setText(
                    (f.format(min)).toString() + " menit " + f.format(
                        sec
                    ) + " detik "
                )
            }

            // When the task is over it will print 00:00:00 there
            override fun onFinish() {
                binding.timer.setText("00 menit 00 detik")
                cancelOrderRequest.orderId = createOrderResponse.data!!.id
                mainViewModel.paramWithBody(
                    "",
                    Constant.CANCEL_ORDER,
                    Gson().toJson(cancelOrderRequest)
                )
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

                    if (result.status == Status.SUCCESS) {
                            findNavController().navigate(R.id.fragmentHome)

                    } else {
                        findNavController().navigate(R.id.fragmentHome)
                    }
                })
            }
        }.start()
        // on below line we are running a try
        // and catch block for initializing our bitmap
        try {
            // on below line we are
            // initializing our bitmap
            bitmap = qrEncoder.bitmap

            // on below line we are setting
            // this bitmap to our image view
            binding.qris.setImageBitmap(bitmap)
        } catch (e: Exception) {
            // on below line we
            // are handling exception
            e.printStackTrace()
        }
        binding.totalValueText.text = NumberUtil.moneyFormat(createOrderResponse.data!!.transaction!!.totalValue.toString())
        binding.btnSend.setOnClickListener {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }}