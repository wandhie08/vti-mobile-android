package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.GetDiscussionRequest
import com.rowantech.vti.data.model.request.GetParameterRequest
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentCreateInvoiceBinding
import com.rowantech.vti.databinding.FragmentProductPaymentBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterProduct
import javax.inject.Inject

class FragmentCreateInvoice  : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentCreateInvoiceBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    var pageRequest = GetParameterRequest()
    internal lateinit var data: EventsItem

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateInvoiceBinding.inflate(inflater, container, false)
        context ?: return binding.root


        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        binding.btnRegistrasi.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("data", Gson().toJson(data))
            findNavController().navigate(R.id.fragmentSuccessInvoice,bundle)
        }
        getUniqueCode(binding)
        return binding.root
    }

    internal lateinit var getParameterResponse: GetParameterResponse

    fun getUniqueCode(binding: FragmentCreateInvoiceBinding){
        pageRequest.page =1
        pageRequest.size =100
        pageRequest.type ="unique_code"

        mainViewModel.paramWithBody(
            "",
            Constant.UNIQUE_CODE,
            Gson().toJson(pageRequest)
        )

        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                getParameterResponse =
                    Gson().fromJson(result.data, GetParameterResponse::class.java)
                binding.textValueKodeUnik.setText(getParameterResponse.parameters?.get(0)?.parameterName?.let {
                    NumberUtil.moneyFormat(
                        it
                    )
                })
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}