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
import com.rowantech.vti.databinding.FragmentHomeBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterBrand
import com.rowantech.vti.views.adapter.ListAdapterEvent
import javax.inject.Inject

class FragmentBrands : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentBrandsBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    internal lateinit var data: BrandsItem
    internal lateinit var dataLogin: LoginResponse

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapterBrand by autoCleared<ListAdapterBrand>()
    private var adapterEvent by autoCleared<ListAdapterEvent>()

    var getEventByBrandRequest = GetEventByBrandRequest()
    var getBrandsRequest = GetBrandsTerkaitRequest()

    var subscribeBrandRequest = SubscribeBrandRequest()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBrandsBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), BrandsItem::class.java)
        //Glide.with(this).load(data.avatar).into(binding.iconBrand)
        data.avatar?.let { binding.iconBrand.loadUrl(it) }
        binding.descBrand.text = data.description
        binding.nameBrand.text = data.brandName
        binding.detailBrand.text = data.tagline
        binding.companyBrand.text = data.companyName
        getEventByBrandRequest.brandId = data.brandId
        getBrandsRequest.companyId = data.companyId

        getBrandsRequest.brandId = data.brandId
        if (!TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
            dataLogin = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)

            if (dataLogin.customer!=null){
                subscribeBrandRequest.brandId = data.brandId
                subscribeBrandRequest.userId = dataLogin.customer!!.customerId
            }

            subscribeBrand(binding,Constant.STATUS_SUBSCRIBE)
        }

        binding.btnSubscribe.setOnClickListener {
            if (TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
                findNavController().navigate(R.id.fragmentLogin)
            } else {
                if (binding.btnSubscribe.text.toString() =="SUBSCRIBE"){
                    subscribeBrand(binding,Constant.SUBSCRIBE_BRAND)
                }else{
                    subscribeBrand(binding,Constant.UNSUBSCRIBE_BRAND)
                }

            }
        }
        getEventByType(binding,"REGISTRATION")
        getAllBrand(binding)
        return binding.root
    }

    fun getEventByType(binding: FragmentBrandsBinding, typeEvent: String) {

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


        binding.recycleViewEventSelesai.adapter = adapterEvent
        binding.recycleViewEventSelesai.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()

        binding.recycleViewEventSelesai.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        getEventByBrandRequest.typeEvent = typeEvent
        getEventByBrandRequest.page = 0
        getEventByBrandRequest.size = 100
        mainViewModel.paramWithBody(
            "",
            Constant.EVENT_BYBRAND,
            Gson().toJson(getEventByBrandRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

            if (result.status == Status.SUCCESS) {
                val response =
                    Gson().fromJson(result.data, GetEventByTypeResponse::class.java)
                adapterEvent.submitList(response.events)
                if (response.events!!.size > 0) {


                    binding.layoutSelesai.visibility = View.VISIBLE

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

    fun getAllBrand(binding: FragmentBrandsBinding){
        val adapterBrand = ListAdapterBrand(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: BrandsItem ->
                onClickDataBrands(
                    binding,
                    partItem
                )
            }) { contributor, imageView ->
        }
        this.adapterBrand = adapterBrand

        binding.recycleViewBrand.adapter = adapterBrand
        binding.recycleViewBrand.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()
        binding.recycleViewBrand.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        getBrandsRequest.page =0
        getBrandsRequest.size =100
        mainViewModel.paramWithBody(
            "",
            Constant.BRAND_TERKAIT,
            Gson().toJson(getBrandsRequest)
        )

        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                val response =
                    Gson().fromJson(result.data, GetBrandsResponse::class.java)
                adapterBrand.submitList(response.brands)
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

    private fun onClickDataEvent(binding: FragmentBrandsBinding, partItem: EventsItem) {
        val bundle = Bundle()
        bundle.putString("data",Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentDescEvent,bundle)
    }

    private fun onClickDataBrands(binding: FragmentBrandsBinding, partItem: BrandsItem) {
        val bundle = Bundle()
        bundle.putString("data",Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentBrands,bundle)
    }

    fun subscribeBrand(binding: FragmentBrandsBinding,typeSubscribe:String){
        progressShow()


        mainViewModel.paramWithBody(
            "",
            typeSubscribe,
            Gson().toJson(subscribeBrandRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            progressDismis()

            if (result.status == Status.SUCCESS) {

                val response =
                    Gson().fromJson(result.data, SubscribeBrandResponse::class.java)

                if (typeSubscribe==Constant.STATUS_SUBSCRIBE){
                    if (response.status =="Y"){
                        binding.btnSubscribe.setText("SUBSCRIBED")
                        binding.btnSubscribe.setBackgroundResource(R.drawable.button_grey)
                    }else{
                        binding.btnSubscribe.setText("SUBSCRIBE")
                        binding.btnSubscribe.setBackgroundResource(R.drawable.button_blue)
                    }
                }else   if (typeSubscribe==Constant.SUBSCRIBE_BRAND){
                    Snackbar.make(
                        binding!!.root,
                        "Anda berhasil subscribe brand",
                        Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.fragmentHome)
                }else  if (typeSubscribe==Constant.UNSUBSCRIBE_BRAND){
                    Snackbar.make(
                        binding!!.root,
                        "Anda berhasil unsubscribe brand",
                        Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.fragmentHome)
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