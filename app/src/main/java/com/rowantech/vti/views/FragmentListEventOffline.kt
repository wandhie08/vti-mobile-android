package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.GetEventByTypeRequest
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.data.model.response.GetEventByTypeResponse
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.data.model.response.MessageResponse
import com.rowantech.vti.databinding.FragmentHomeBinding
import com.rowantech.vti.databinding.FragmentListEventBinding
import com.rowantech.vti.databinding.FragmentNotificationBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterEvent
import javax.inject.Inject

class FragmentListEventOffline : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentListEventBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private var adapterEvent by autoCleared<ListAdapterEvent>()

    var getEventByTypeRequest = GetEventByTypeRequest()
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var isUpcoming = false
    var ongoing = false
    var register = false
    var closed = false
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListEventBinding.inflate(inflater, container, false)
        context ?: return binding.root

        if(arguments?.getBoolean("isUpcoming") == true){
            isUpcoming =true
            binding.btnAkanDatang.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnAkanDatang.setTextColor(Color.parseColor("#FFFFFFFF"))
        }

        if(arguments?.getBoolean("register") == true){
            register =true

            binding.btnPendaftaran.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnPendaftaran.setTextColor(Color.parseColor("#FFFFFFFF"))
        }

        if(arguments?.getBoolean("closed") == true){
            closed =true
            binding.btnSelesai.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnSelesai.setTextColor(Color.parseColor("#FFFFFFFF"))
        }

        if(arguments?.getBoolean("ongoing") == true){
            ongoing =true
            binding.btnBerlangsung.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnBerlangsung.setTextColor(Color.parseColor("#FFFFFFFF"))
        }

        getEventByType(binding,"UPCOMING|REGISTRATION|EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT|ANNOUNCEMENT|CLOSED")

        binding.btnAkanDatang.setOnClickListener {
            if (!isUpcoming){
                isUpcoming=true
                binding.btnAkanDatang.setBackgroundResource(R.drawable.btn_shape_blue)
                binding.btnAkanDatang.setTextColor(Color.parseColor("#FFFFFFFF"))
            }else{
                isUpcoming=false

                binding.btnAkanDatang.setBackgroundResource(R.drawable.btn_shape_blue_white)
                binding.btnAkanDatang.setTextColor(Color.parseColor("#FF343F4B"))

            }
            getEventByType(binding,"UPCOMING")
        }

        binding.btnBerlangsung.setOnClickListener {
            if (!ongoing){
                ongoing=true
                binding.btnBerlangsung.setBackgroundResource(R.drawable.btn_shape_blue)
                binding.btnBerlangsung.setTextColor(Color.parseColor("#FFFFFFFF"))
            }else{
                ongoing=false
                binding.btnBerlangsung.setBackgroundResource(R.drawable.btn_shape_blue_white)
                binding.btnBerlangsung.setTextColor(Color.parseColor("#FF343F4B"))

            }
            getEventByType(binding,"EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT")
        }

        binding.btnPendaftaran.setOnClickListener {
            if (!register){
                register=true
                binding.btnPendaftaran.setBackgroundResource(R.drawable.btn_shape_blue)
                binding.btnPendaftaran.setTextColor(Color.parseColor("#FFFFFFFF"))
            }else{
                register=false
                binding.btnPendaftaran.setBackgroundResource(R.drawable.btn_shape_blue_white)
                binding.btnPendaftaran.setTextColor(Color.parseColor("#FF343F4B"))

            }
            getEventByType(binding,"REGISTRATION")

        }

        binding.btnSelesai.setOnClickListener {
            if (!closed){
                closed=true
                binding.btnSelesai.setBackgroundResource(R.drawable.btn_shape_blue)
                binding.btnSelesai.setTextColor(Color.parseColor("#FFFFFFFF"))
            }else{
                closed=false
                binding.btnSelesai.setBackgroundResource(R.drawable.btn_shape_blue_white)
                binding.btnSelesai.setTextColor(Color.parseColor("#FF343F4B"))

            }
            getEventByType(binding,"ANNOUNCEMENT|CLOSED")

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun getEventByType(binding: FragmentListEventBinding, typeEvent: String) {
        var evenSelected: String = ""

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
        getEventByTypeRequest.typeAccount = "Customer"
        binding.recycleViewNotifications.adapter = adapterEvent
        binding.recycleViewNotifications.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        postponeEnterTransition()

        binding.recycleViewNotifications.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }


        if (isUpcoming){
            if (evenSelected !=""){
                evenSelected =evenSelected+"|"
            }
            evenSelected =evenSelected+"UPCOMING|DRAFT"
        }

        if (register){
            if (evenSelected !=""){
                evenSelected =evenSelected+"|"
            }
            evenSelected =evenSelected+"REGISTRATION"
        }

        if (closed){
            if (evenSelected !=""){
                evenSelected =evenSelected+"|"
            }
            evenSelected =evenSelected+"CLOSED|ANNOUNCEMENT|EVALUATION EVENT"
        }

        if (ongoing){
            if (evenSelected !=""){
                evenSelected =evenSelected+"|"
            }
            evenSelected =evenSelected+"ONGOING|SUBMISSION|EVALUATION REGISTRATION"
        }
        getEventByTypeRequest.typeLocation = "offline"
        getEventByTypeRequest.typeEvent = evenSelected
        getEventByTypeRequest.page = 0
        getEventByTypeRequest.size = 100
        mainViewModel.paramWithBody(
            "",
            Constant.EVENT_BYTYPE,
            Gson().toJson(getEventByTypeRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

            if (result.status == Status.SUCCESS) {
                val response =
                    Gson().fromJson(result.data, GetEventByTypeResponse::class.java)
                adapterEvent.submitList(response.events)
                if (response.events!!.size > 0) {

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

    private fun onClickDataEvent(binding: FragmentListEventBinding, partItem: EventsItem) {
        val bundle = Bundle()
        bundle.putString("data",Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentDescEvent,bundle)
    }

}