package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.request.GetDiscussionRequest
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.data.model.response.GetProductEventResponse
import com.rowantech.vti.data.model.response.ProductsItem
import com.rowantech.vti.databinding.FragmentProductPaymentBinding
import com.rowantech.vti.databinding.FragmentQrisBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterProduct
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

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    internal lateinit var data: EventsItem
    internal lateinit var getProductEventResponse: GetProductEventResponse
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQrisBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        binding.btnSend.setOnClickListener {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }}