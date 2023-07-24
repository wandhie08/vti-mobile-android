package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.GetDiscussionRequest
import com.rowantech.vti.data.model.request.GetEventByTypeRequest
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentHomeBinding
import com.rowantech.vti.databinding.FragmentProductPaymentBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterProduct
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
    val listProductEvent: MutableList<ProductsItem> = mutableListOf()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapterProduct by autoCleared<ListAdapterProduct>()
    var pageRequest = GetDiscussionRequest()
    internal lateinit var data: EventsItem
    internal lateinit var getProductEventResponse: GetProductEventResponse
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductPaymentBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        pageRequest.eventId =data.eventId
        binding.btnRegistrasi.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("dataEvent", Gson().toJson(data))
            bundle.putString("address", Gson().toJson(data))
            bundle.putString("courier", Gson().toJson(data))
            bundle.putString("data", Gson().toJson(getProductEventResponse))
            findNavController().navigate(R.id.fragmentCreateInvoice,bundle)
        }
        getAllProduct(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun getAllProduct(binding: FragmentProductPaymentBinding){
        val adapterProduct = ListAdapterProduct(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: ProductsItem -> onClickDataBrands(binding, partItem) },
            { partItem: ProductsItem -> onClickDataAdd(binding, partItem) },
            { partItem: ProductsItem ->
                onClickDataDelete(
                    binding,
                    partItem
                )
            }) { contributor, imageView ->
        }

        this.adapterProduct = adapterProduct

        binding.recycleViewBanner.adapter = adapterProduct
        binding.recycleViewBanner.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        postponeEnterTransition()
        binding.recycleViewBanner.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        pageRequest.page =0
        pageRequest.size =100
        mainViewModel.paramWithBody(
            "",
            Constant.PRODUCT,
            Gson().toJson(pageRequest)
        )

        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                getProductEventResponse =
                    Gson().fromJson(result.data, GetProductEventResponse::class.java)
                adapterProduct.submitList(getProductEventResponse.products)
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

    private fun onClickDataBrands(binding: FragmentProductPaymentBinding, partItem: ProductsItem) {

    }
    private fun onClickDataAdd(binding: FragmentProductPaymentBinding, partItem: ProductsItem) {
        for (i in 0 until getProductEventResponse.products!!.size) {
            if(getProductEventResponse.products!!.get(i)!!.productId == partItem.productId){
                getProductEventResponse.products!!.get(i)?.stock = partItem.stock?.plus(1)
            }
        }

        adapterProduct.notifyDataSetChanged()
        totalTagihan(binding)
    }

    private fun onClickDataDelete(binding: FragmentProductPaymentBinding, partItem: ProductsItem) {
        for (i in 0 until getProductEventResponse.products!!.size) {
            if(getProductEventResponse.products!!.get(i)!!.productId == partItem.productId){
                if (getProductEventResponse.products?.get(i)?.stock!! > 0){
                    getProductEventResponse.products?.get(i)?.stock = partItem.stock?.minus(1)

                }
            }
        }
        adapterProduct.notifyDataSetChanged()
        totalTagihan(binding)
    }

    fun totalTagihan(binding: FragmentProductPaymentBinding) {
        var totalTagihan: Int? = 0
        for (layanan in getProductEventResponse.products!!) {
            val totalHarga = layanan?.price?.times(layanan.stock!!)
            if (totalTagihan != null) {
                totalTagihan = totalTagihan + totalHarga!!
            }
        }
        binding.totalValue.text = NumberUtil.moneyFormat(totalTagihan.toString())
    }
}