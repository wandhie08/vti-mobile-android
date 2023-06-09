package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.databinding.FragmentCreateInvoiceBinding
import com.rowantech.vti.databinding.FragmentSuccessInvoiceBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import javax.inject.Inject

class FragmentSuccessInvoice : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentSuccessInvoiceBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }


    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSuccessInvoiceBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.btnRegistrasi.setOnClickListener {
            findNavController().navigate(R.id.fragmentHome)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}