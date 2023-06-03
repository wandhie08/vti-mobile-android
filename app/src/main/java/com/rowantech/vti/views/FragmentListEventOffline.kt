package com.rowantech.vti.views

import android.annotation.SuppressLint
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


    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListEventBinding.inflate(inflater, container, false)
        context ?: return binding.root

        getEventByType(binding,"UPCOMING|REGISTRATION|EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT|ANNOUNCEMENT|CLOSED")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun getEventByType(binding: FragmentListEventBinding, typeEvent: String) {
        var evenSelected: String = typeEvent

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
                    Snackbar.make(
                        binding.root,
                        "Incorrect email or password or level, Please check again before login!",
                        Snackbar.LENGTH_LONG
                    ).show()
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