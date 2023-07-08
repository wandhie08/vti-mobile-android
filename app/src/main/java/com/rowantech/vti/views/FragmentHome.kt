package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.GetEventByTypeRequest
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.databinding.FragmentHomeBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.model.request.GetBrandsRequest
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.views.adapter.ListAdapterBrand
import com.rowantech.vti.views.adapter.ListAdapterEvent

import java.util.*
import javax.inject.Inject

class FragmentHome : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    internal lateinit var data: LoginResponse

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapterBrand by autoCleared<ListAdapterBrand>()
    private var adapterEvent by autoCleared<ListAdapterEvent>()

    var getEventByTypeRequest = GetEventByTypeRequest()
    var getBrandsRequest = GetBrandsRequest()


    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.layoutAkanDatang.visibility = View.GONE
        binding.layoutSelesai.visibility = View.GONE
        binding.layoutDaftarSekarang.visibility = View.GONE
        binding.layoutSedangBerlangsung.visibility = View.GONE

        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener { v, keyCode, event ->
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(homeIntent)
            }
            true
        }
        getEventByType(binding,"REGISTRATION")
        getEventByType(binding,"UPCOMING")
        getEventByType(binding,"ONGOING")
        getEventByType(binding,"CLOSED")

        getAllBrand(binding)
        binding.iconMenu.setOnClickListener {
            if(TextUtils.isEmpty(MainApplication().getStringPref(context,"dataLogin"))){
                findNavController().navigate(R.id.fragmentLogin)
            }else{
                findNavController().navigate(R.id.fragmentDialogNotifications)
            }
        }

        binding.iconScan.setOnClickListener {

        }

        binding.btnAkanDatang.setOnClickListener {
            findNavController().navigate(R.id.fragmentSearchEvent)
        }

        binding.btnSelesai.setOnClickListener {
            findNavController().navigate(R.id.fragmentSearchEvent)
        }

        binding.btnDaftarSekarang.setOnClickListener {
            findNavController().navigate(R.id.fragmentSearchEvent)
        }

        binding.btnSedangBerlangsung.setOnClickListener {
            findNavController().navigate(R.id.fragmentSearchEvent)
        }

        binding.iconSearch.setOnClickListener {

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun getEventByType(binding: FragmentHomeBinding,typeEvent:String){
        var evenSelected :String=typeEvent

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
        if (typeEvent=="CLOSED"){
            evenSelected="CLOSED|ANNOUNCEMENT"
            binding.recycleViewEventSelesai.adapter = adapterEvent
            binding.recycleViewEventSelesai.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            postponeEnterTransition()

            binding.recycleViewEventSelesai.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }else if (typeEvent=="ONGOING"){
            evenSelected="EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT"

            binding.recycleViewEventSedangBerlangsung.adapter = adapterEvent
            binding.recycleViewEventSedangBerlangsung.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            postponeEnterTransition()

            binding.recycleViewEventSedangBerlangsung.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }else if (typeEvent=="REGISTRATION"){
            evenSelected="REGISTRATION"

            if(!TextUtils.isEmpty(MainApplication().getStringPref(context,"dataLogin"))){
                data = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)
                if (data.customer!!.typeCustomer=="Publisher"){
                    evenSelected="DRAFT|REGISTRATION"
                    getEventByTypeRequest.typeAccount = "Publisher"
                }
            }
            binding.recycleViewEventDaftarSekarang.adapter = adapterEvent
            binding.recycleViewEventDaftarSekarang.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            postponeEnterTransition()

            binding.recycleViewEventDaftarSekarang.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }else if (typeEvent=="UPCOMING"){
            evenSelected="UPCOMING"

            binding.recycleViewEvantAkanDatang.adapter = adapterEvent
            binding.recycleViewEvantAkanDatang.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            postponeEnterTransition()

            binding.recycleViewEvantAkanDatang.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }



        getEventByTypeRequest.typeLocation = "all"
        getEventByTypeRequest.typeEvent = evenSelected
        getEventByTypeRequest.page =0
        getEventByTypeRequest.size =100
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
                if (response.events!!.size>0) {

                    if (typeEvent == "CLOSED") {
                        binding.layoutSelesai.visibility = View.VISIBLE
                    } else if (typeEvent == "ONGOING") {
                        binding.layoutSedangBerlangsung.visibility = View.VISIBLE

                    } else if (typeEvent == "REGISTRATION") {
                        binding.layoutDaftarSekarang.visibility = View.VISIBLE

                    } else if (typeEvent == "UPCOMING") {
                        binding.layoutAkanDatang.visibility = View.VISIBLE

                    }
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

    fun getAllBrand(binding: FragmentHomeBinding){
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
            Constant.BRAND,
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

    private fun onClickDataBrands(binding: FragmentHomeBinding, partItem: BrandsItem) {
        val bundle = Bundle()
        bundle.putString("data",Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentBrands,bundle)
    }

    private fun onClickDataEvent(binding: FragmentHomeBinding, partItem: EventsItem) {
        val bundle = Bundle()
        bundle.putString("data",Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentDescEvent,bundle)
    }

}