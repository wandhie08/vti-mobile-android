package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        getAllBanner(binding, data)
        Glide.with(this).load(data.avatar).into(binding.iconBrand)
        if (!TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
            dataLogin = Gson().fromJson(
                MainApplication().getStringPref(context, "dataLogin"),
                LoginResponse::class.java
            )

            if (dataLogin.customer != null) {
                registerEventRequest.eventId = data.eventId
                registerEventRequest.customerId = dataLogin.customer!!.customerId
            }

            subscribeEvent(binding, Constant.STATUS_EVENT)
        }
        getEventByType(
            binding,
            "REGISTRATION|EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT"
        )

        binding.recycleViewDiscussion.visibility = View.GONE
        binding.layoutBrand.visibility = View.GONE
        binding.btnRegistrasi.setOnClickListener {
            if (TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
                findNavController().navigate(R.id.fragmentLogin)
            } else {
                if (binding.btnRegistrasi.text.toString() == "TERDAFTAR") {

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
            findNavController().navigate(R.id.fragmentTabsEvent)
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
                    Snackbar.make(
                        binding!!.root,
                        "Incorrect email or password or level, Please check again before login!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

}