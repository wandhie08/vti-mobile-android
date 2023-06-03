package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.request.GetEventByTypeRequest
import com.rowantech.vti.databinding.FragmentDetailEventBinding
import com.rowantech.vti.databinding.FragmentListEventBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterEvent
import javax.inject.Inject

class FragmentDetailEvent : BaseFragment(), Injectable {

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
        val binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        context ?: return binding.root



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}