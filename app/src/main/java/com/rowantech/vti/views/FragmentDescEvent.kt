package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.*
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentBrandsBinding
import com.rowantech.vti.databinding.FragmentDescEventBinding
import com.rowantech.vti.databinding.FragmentHomeBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterBanner
import com.rowantech.vti.views.adapter.ListAdapterBrand
import com.rowantech.vti.views.adapter.ListAdapterEvent
import javax.inject.Inject

class FragmentDescEvent : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors


    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    internal lateinit var data: EventsItem
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapterEvent by autoCleared<ListAdapterEvent>()
    private var adapterBanner by autoCleared<ListAdapterBanner>()
    var getEventTerkaitRequest = GetEventTerkaitRequest()
    internal lateinit var dataLogin: LoginResponse
    var registerEventRequest = RegisterEventRequest()
    var getStatusTemplateRequest = GetStatusTemplateRequest()
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDescEventBinding.inflate(inflater, container, false)
        context ?: return binding.root

        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)

        binding.nameBrand.text = data.title
        binding.companyBrand.text = data.companyName
        binding.descEvent.text = data.shortDescription
        getEventTerkaitRequest.companyId = data.companyId
        getEventTerkaitRequest.eventId = data.eventId



        binding.textDateEvent.text = data.periodeDate

        if (data.location == "online") {
            binding.textLocationType.text = "Online"
        } else if (data.location == "offline") {
            binding.textLocationType.text = "Offline"

        }

        if (data.checkIn == "N") {
            binding.btnCheckIn.visibility = View.GONE
        } else if (data.checkIn == "Y") {
            binding.btnCheckIn.visibility = View.VISIBLE

        }

        if (data.winningPrize == "N") {
            binding.btnHadiah.visibility = View.GONE
        } else if (data.winningPrize == "Y") {
            binding.btnHadiah.visibility = View.VISIBLE

        }

        if (data.eventType == "free") {
            binding.textBerbayar.text = "Gratis"
        } else if (data.eventType == "paid") {
            binding.textBerbayar.text = "Berbayar"

        }

        if (data.eventType == "paid") {
            binding.btnBerbayar.setOnClickListener{

            }
        }
        if (data.formRegistration == "N") {
            binding.btnPendaftaran.visibility = View.GONE
        } else if (data.formRegistration == "Y") {
            binding.btnPendaftaran.visibility = View.VISIBLE

        }

        if (data.formValidation == "N") {
            binding.btnVerifikasi.visibility = View.GONE
        } else if (data.formValidation == "Y") {
            binding.btnVerifikasi.visibility = View.VISIBLE

        }
        // binding.textDateEvent.text = data.
        getAllBanner(binding, data)
        //Glide.with(this).load(data.avatar).into(binding.iconBrand)
        data.avatar?.let { binding.iconBrand.loadUrl(it) }
        if (!TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
            dataLogin = Gson().fromJson(
                MainApplication().getStringPref(context, "dataLogin"),
                LoginResponse::class.java
            )

            if (dataLogin.customer != null) {
                registerEventRequest.eventId = data.eventId
                registerEventRequest.customerId = dataLogin.customer!!.customerId

                getStatusTemplateRequest.eventId = data.eventId
                getStatusTemplateRequest.customerId = dataLogin.customer!!.customerId
            }

            subscribeEvent(binding, Constant.STATUS_EVENT)
        }
        getEventByType(
            binding,
            "REGISTRATION|EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT"
        )

        binding.recycleViewDiscussion.visibility = View.GONE
        binding.layoutBrand.visibility = View.GONE

        if (data.type == "CLOSED") {
            binding.btnRegistrasi.visibility = View.GONE
        } else if (data.formValidation == "Y") {
            binding.btnVerifikasi.visibility = View.VISIBLE

        }
        binding.btnRegistrasi.setBackgroundResource(R.drawable.button_blue)
        if (data.type == "CLOSED") {
            binding.btnRegistrasi.visibility = View.INVISIBLE
        } else  if (data.type == "ONGOING|REGISTRATION"){
            binding.btnRegistrasi.visibility = View.VISIBLE
            binding.btnRegistrasi.setBackgroundResource(R.drawable.button_blue)
        }

        binding.btnRegistrasi.setOnClickListener {
            if (TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
                findNavController().navigate(R.id.fragmentLogin)
            } else {
                if (binding.btnRegistrasi.text.toString() == "TERDAFTAR") {
                    val bundle = Bundle()
                    bundle.putString("data", Gson().toJson(data))
                    if (data.formRegistration == "Y") {
                        progressShow()

                        mainViewModel.paramWithBody(
                            "",
                            Constant.GET_STATUS_TEMPLATE,
                            Gson().toJson(getStatusTemplateRequest)
                        )
                        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                            progressDismis()

                            if (result.status == Status.SUCCESS) {
                                val response =
                                    Gson().fromJson(result.data, GetStatusRegisterEventResponse::class.java)
                                if (response.status == "Y") {

                                    if (data.eventType == "paid") {
                                        val bundle = Bundle()
                                        bundle.putString("data", Gson().toJson(data))
                                        findNavController().navigate(
                                            R.id.fragmentProductPayment,
                                            bundle
                                        )
                                    } else if (data.eventType == "free") {
                                        val bundle = Bundle()
                                        bundle.putString("data", Gson().toJson(data))
                                        findNavController().navigate(R.id.fragmentHome, bundle)

                                    }

                                }else{
                                    findNavController().navigate(R.id.fragmentCreateTemplate, bundle)
                                }
                            } else {
                                if (!TextUtils.isEmpty(result.data)) {
                                    val response =
                                        Gson().fromJson(result.data, MessageResponse::class.java)
                                    response.error?.let {
                                        Snackbar.make(
                                            binding!!.root,
                                            it,
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        })

                    } else {
                        if (data.eventType == "paid") {
                            findNavController().navigate(R.id.fragmentCreateTemplate, bundle)
                        }
                    }
                } else {
                    dataLogin = Gson().fromJson(
                        MainApplication().getStringPref(context, "dataLogin"),
                        LoginResponse::class.java
                    )

                    if (dataLogin.customer != null) {
                        registerEventRequest.eventId = data.eventId
                        registerEventRequest.customerId = dataLogin.customer!!.customerId


                    }

                    subscribeEvent(binding, Constant.SUBSCRIBE_EVENT)
                }
            }
        }

        binding.iconMenu.setOnClickListener {
            if (TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
                findNavController().navigate(R.id.fragmentLogin)
            } else {
                findNavController().navigate(R.id.fragmentDialogNotifications)
            }
        }

        binding.layoutTentangEvent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("data", Gson().toJson(data))
            findNavController().navigate(R.id.fragmentTabsEvent, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun getAllBanner(binding: FragmentDescEventBinding, eventsItem: EventsItem) {
        val adapterBanner = ListAdapterBanner(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: BannersItem ->
                onClickDataBanner(
                    binding,
                    partItem
                )
            }) { contributor, imageView ->
        }
        this.adapterBanner = adapterBanner

        binding.recycleViewBanner.adapter = adapterBanner
        binding.recycleViewBanner.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()
        binding.recycleViewBanner.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }


        adapterBanner.submitList(eventsItem.banners)


    }

    fun getEventByType(binding: FragmentDescEventBinding, typeEvent: String) {

        val adapterEvent = ListAdapterEvent(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: EventsItem ->
                onClickDataEvent(
                    binding,
                    partItem
                )
            }) { contributor, imageView ->
        }
        this.adapterEvent = adapterEvent


        binding.recycleViewEventTerkait.adapter = adapterEvent
        binding.recycleViewEventTerkait.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()

        binding.recycleViewEventTerkait.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        getEventTerkaitRequest.page = 0
        getEventTerkaitRequest.size = 100
        mainViewModel.paramWithBody(
            "",
            Constant.EVENT_TERKAIT,
            Gson().toJson(getEventTerkaitRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

            if (result.status == Status.SUCCESS) {
                val response =
                    Gson().fromJson(result.data, GetEventByTypeResponse::class.java)
                adapterEvent.submitList(response.events)
                if (response.events!!.size > 0) {


                    binding.layoutBrand.visibility = View.VISIBLE

                }
            } else {

            }
        })
    }

    private fun onClickDataEvent(binding: FragmentDescEventBinding, partItem: EventsItem) {
        val bundle = Bundle()
        bundle.putString("data", Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentDescEvent, bundle)
    }

    private fun onClickDataBanner(binding: FragmentDescEventBinding, partItem: BannersItem) {

    }

    fun subscribeEvent(binding: FragmentDescEventBinding, typeSubscribe: String) {
        progressShow()


        mainViewModel.paramWithBody(
            "",
            typeSubscribe,
            Gson().toJson(registerEventRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            progressDismis()

            if (result.status == Status.SUCCESS) {


                if (typeSubscribe == Constant.STATUS_EVENT) {
                    val response =
                        Gson().fromJson(result.data, GetStatusRegisterEventResponse::class.java)
                    if (response.registrationStatus == "Y") {
                        binding.btnRegistrasi.setText("TERDAFTAR")
                        binding.btnRegistrasi.setBackgroundResource(R.drawable.button_grey)
                    } else {
                        binding.btnRegistrasi.setText("DAFTAR")
                        binding.btnRegistrasi.setBackgroundResource(R.drawable.button_blue)
                    }
                } else if (typeSubscribe == Constant.SUBSCRIBE_EVENT) {
                    Snackbar.make(
                        binding!!.root,
                        "Anda berhasil daftar event",
                        Snackbar.LENGTH_LONG
                    ).show()

                }


            } else {
                if (!TextUtils.isEmpty(result.data)) {
                    val response =
                        Gson().fromJson(result.data, MessageResponse::class.java)
                    response.error?.let {
                        Snackbar.make(
                            binding!!.root,
                            it,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            //.placeholder(R.drawable.ic_copy)
            //.error(R.drawable.ic_copy)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }
}