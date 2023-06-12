package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.databinding.FragmentProductPaymentBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import javax.inject.Inject

class FragmentProductPayment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentProductPaymentBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    internal lateinit var data: EventsItem
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductPaymentBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)

        binding.btnRegistrasi.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("data", Gson().toJson(data))
            findNavController().navigate(R.id.fragmentCreateInvoice,bundle)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}