package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
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
import com.google.gson.reflect.TypeToken
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.*
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentCreateDiscussionBinding
import com.rowantech.vti.databinding.FragmentListDiscussionTypeBinding
import com.rowantech.vti.databinding.FragmentLoginBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterFromPendaftaran
import com.rowantech.vti.views.adapter.ListAdapterFromUpload
import com.rowantech.vti.views.adapter.ListAdapterPrize
import com.rowantech.vti.views.adapter.ListAdapterProductPaid
import javax.inject.Inject

class FragmentCreateDiscussion : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    internal lateinit var getProductEventResponse: GetProductEventResponse
    internal lateinit var getParameterResponse: GetParameterResponse
    var getEventTerkaitResponse = GetFormTemplateResponse()
    var getEventTerkaitRequest = GetEventTerkaitRequest()
    private var adapterPendaftaran by autoCleared<ListAdapterFromPendaftaran>()
    private var adapterUpload by autoCleared<ListAdapterFromUpload>()
    @Inject
    lateinit var appExecutors: AppExecutors
    var getDateEventResponse = GetDateEventResponse()

    var binding by autoCleared<FragmentCreateDiscussionBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    internal lateinit var dataCustomer: LoginResponse
    var loginRequest = LoginRequest()
    var pageRequest = GetDiscussionRequest()
    private var adapterProduct by autoCleared<ListAdapterProductPaid>()
    private var adapterPrize by autoCleared<ListAdapterPrize>()

    internal lateinit var data: EventsItem
    var createDiscussionRequest = CreateDiscussionRequest()
    var discussionType : String="JADWAL"
    @SuppressLint("HardwareIds", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateDiscussionBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        dataCustomer = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)
        binding.btnSend.setOnClickListener({
            createDiscussionRequest.eventId = data.eventId
            createDiscussionRequest.isRead = "N"
            createDiscussionRequest.customerId = dataCustomer.customer!!.customerId
            createDiscussionRequest.title = binding.formMessage.text.toString()
            createDiscussionRequest.content =binding.formMessage.text.toString()
            createDiscussionRequest.type = discussionType

            mainViewModel.paramWithBody(
                "",
                Constant.CREATE_DISCUSSION,
                Gson().toJson(createDiscussionRequest)
            )

            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                if (result.status == Status.SUCCESS) {
                    Snackbar.make(
                        binding!!.root,
                        "Diskusi berhasil ditambahkan",
                        Snackbar.LENGTH_LONG
                    ).show()

                    findNavController().navigate(R.id.fragmentHome)
                }
            })
        })
        if (data.winningPrize == "N") {
            //binding.btnHadiah.visibility = View.GONE
            binding.btnPemenang.visibility = View.GONE
        } else if (data.winningPrize == "Y") {
            //binding.btnHadiah.visibility = View.VISIBLE
            binding.btnPemenang.visibility = View.VISIBLE
        }

        if (data.submission == "N") {
            binding.btnPengumpulanData.visibility = View.GONE

        } else if (data.submission == "Y") {
            binding.btnPengumpulanData.visibility = View.VISIBLE
        }

        if (data.eventType == "free") {
            binding.btnItemProduct.visibility = View.GONE

        } else if (data.eventType == "paid") {
            binding.btnItemProduct.visibility = View.VISIBLE
        }
        getDateEventResponse =
            Gson().fromJson(data.freeData2, GetDateEventResponse::class.java)

        binding.btnjadwal.setBackgroundResource(R.drawable.btn_shape_blue)
        binding.btnjadwal.setTextColor(Color.parseColor("#FFFFFFFF"))
        binding.textPendaftaranValue.text =getDateEventResponse.regP
        binding.textAcaraValue.text =getDateEventResponse.evtP
        binding.textEvaluasiValue.text =getDateEventResponse.val2P
        binding.textPengumpulanDataValue.text =getDateEventResponse.subP
        binding.textPengumumanValue.text =getDateEventResponse.winP

        binding.btnjadwal.setOnClickListener({
            resetButton(binding)
            discussionType= "JADWAL"
            println("data.freeData2 :"+data.freeData2)
            binding.btnjadwal.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnjadwal.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.jadwalLayout.visibility =View.VISIBLE

            binding.textPendaftaranValue.text =getDateEventResponse.regP
            binding.textAcaraValue.text =getDateEventResponse.evtP
            binding.textEvaluasiValue.text =getDateEventResponse.val2P
            binding.textPengumpulanDataValue.text =getDateEventResponse.subP
            binding.textPengumumanValue.text =getDateEventResponse.winP
        })

        binding.btnHadiah.setOnClickListener({
            resetButton(binding)
            discussionType= "PRIZE"

            binding.btnHadiah.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnHadiah.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.recycleViewHadiah.visibility =View.VISIBLE
            val recycleViewProduk = binding.recycleViewHadiah

            val adapterPrize = ListAdapterPrize(
                dataBindingComponent,
                requireContext(),
                appExecutors,
            ) { contributor, imageView ->
            }

            this.adapterPrize = adapterPrize

            recycleViewProduk.adapter = adapterPrize
            recycleViewProduk.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            postponeEnterTransition()
            recycleViewProduk.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            pageRequest.eventId = data.eventId
            pageRequest.page = 0
            pageRequest.size = 100
            mainViewModel.paramWithBody(
                "",
                Constant.PRIZE,
                Gson().toJson(pageRequest)
            )

            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                if (result.status == Status.SUCCESS) {
                    getParameterResponse =
                        Gson().fromJson(result.data, GetParameterResponse::class.java)
                    adapterPrize.submitList(getParameterResponse.parameters)
                }
            })
        })
        binding.btnPendaftaran.setOnClickListener({
            resetButton(binding)
            binding.btnPendaftaran.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnPendaftaran.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.recycleViewPendaftaran.visibility =View.VISIBLE
            discussionType= "FORM_REGISTRATION"

            val recycleViewProduk = binding.recycleViewPendaftaran
            val adapterPendaftaran = ListAdapterFromPendaftaran(
                dataBindingComponent,
                requireContext(),
                appExecutors,
            ) { contributor, imageView ->
            }

            this.adapterPendaftaran = adapterPendaftaran

            recycleViewProduk.adapter = adapterPendaftaran
            recycleViewProduk.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            postponeEnterTransition()
            recycleViewProduk.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            getEventTerkaitRequest.eventId = data.eventId
            getEventTerkaitRequest.page = 0
            getEventTerkaitRequest.size = 100
            mainViewModel.paramWithBody(
                "",
                Constant.FORM_TEMPLATE,
                Gson().toJson(getEventTerkaitRequest)
            )
            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

                if (result.status == Status.SUCCESS) {
                    getEventTerkaitResponse =
                        Gson().fromJson(result.data, GetFormTemplateResponse::class.java)

                    adapterPendaftaran.submitList(getEventTerkaitResponse.templates)
                }
            })
        })

        binding.btnPengumpulanData.setOnClickListener({
            resetButton(binding)
            discussionType= "FORM_SUBMISSION"
            binding.btnPengumpulanData.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnPengumpulanData.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.recycleViewUpload.visibility =View.VISIBLE
            val recycleViewProduk = binding.recycleViewUpload
            val adapterUpload = ListAdapterFromUpload(
                dataBindingComponent,
                requireContext(),
                appExecutors,
            ) { contributor, imageView ->
            }

            this.adapterUpload = adapterUpload

            recycleViewProduk.adapter = adapterUpload
            recycleViewProduk.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            postponeEnterTransition()
            recycleViewProduk.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            getEventTerkaitRequest.eventId = data.eventId
            getEventTerkaitRequest.page = 0
            getEventTerkaitRequest.size = 100
            mainViewModel.paramWithBody(
                "",
                Constant.SUBMISSION,
                Gson().toJson(getEventTerkaitRequest)
            )
            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

                if (result.status == Status.SUCCESS) {
                    getEventTerkaitResponse =
                        Gson().fromJson(result.data, GetFormTemplateResponse::class.java)

                    adapterUpload.submitList(getEventTerkaitResponse.templates)
                }
            })
        })

        binding.btnItemProduct.setOnClickListener({
            resetButton(binding)
            discussionType= "PRODUCT"

            binding.btnItemProduct.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnItemProduct.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.recycleViewProduk.visibility =View.VISIBLE
            val recycleViewProduk = binding.recycleViewProduk
            val adapterProduct = ListAdapterProductPaid(
                dataBindingComponent,
                requireContext(),
                appExecutors,
            ) { contributor, imageView ->
            }

            this.adapterProduct = adapterProduct

            recycleViewProduk.adapter = adapterProduct
            recycleViewProduk.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            postponeEnterTransition()
            recycleViewProduk.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            pageRequest.eventId = data.eventId
            pageRequest.page = 0
            pageRequest.size = 100
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

                }
            })
        })

        binding.btnPemenang.setOnClickListener({
            resetButton(binding)
            discussionType= "PRIZE"

            binding.btnPemenang.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnPemenang.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.recycleViewHadiah.visibility =View.VISIBLE
            val recycleViewProduk = binding.recycleViewHadiah

            val adapterPrize = ListAdapterPrize(
                dataBindingComponent,
                requireContext(),
                appExecutors,
            ) { contributor, imageView ->
            }

            this.adapterPrize = adapterPrize

            recycleViewProduk.adapter = adapterPrize
            recycleViewProduk.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            postponeEnterTransition()
            recycleViewProduk.getViewTreeObserver()
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            pageRequest.eventId = data.eventId
            pageRequest.page = 0
            pageRequest.size = 100
            mainViewModel.paramWithBody(
                "",
                Constant.PRIZE,
                Gson().toJson(pageRequest)
            )

            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                if (result.status == Status.SUCCESS) {
                    getParameterResponse =
                        Gson().fromJson(result.data, GetParameterResponse::class.java)
                    adapterPrize.submitList(getParameterResponse.parameters)
                }
            })
        })

        binding.btnPeraturan.setOnClickListener({
            resetButton(binding)
            discussionType= "PERATURAN"

            binding.btnPeraturan.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnPeraturan.setTextColor(Color.parseColor("#FFFFFFFF"))
        })

        binding.btnLainnya.setOnClickListener({
            resetButton(binding)
            discussionType= "LAINNYA"

            binding.btnLainnya.setBackgroundResource(R.drawable.btn_shape_blue)
            binding.btnLainnya.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.textLainnya.visibility =View.VISIBLE

            if (data.freeData3!!.length>0){
                val typeToken = object : TypeToken<List<FreeDataResponseItem>>() {}.type
                val freeData3 = Gson().fromJson<List<FreeDataResponseItem>>(data.freeData3!!, typeToken)
                if (!freeData3.isEmpty()){
                    binding.textLainnya.text = freeData3[0].content
                }
            }
        })
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    fun resetButton(binding: FragmentCreateDiscussionBinding){
        binding.btnHadiah.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnHadiah.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnjadwal.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnjadwal.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnPendaftaran.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnPendaftaran.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnPengumpulanData.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnPengumpulanData.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnItemProduct.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnItemProduct.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnPemenang.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnPemenang.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnPeraturan.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnPeraturan.setTextColor(Color.parseColor("#FF343F4B"))

        binding.btnLainnya.setBackgroundResource(R.drawable.btn_shape_blue_white)
        binding.btnLainnya.setTextColor(Color.parseColor("#FF343F4B"))

        binding.jadwalLayout.visibility =View.GONE
        binding.recycleViewProduk.visibility =View.GONE
        binding.recycleViewPendaftaran.visibility =View.GONE
        binding.recycleViewUpload.visibility =View.GONE
        binding.recycleViewHadiah.visibility =View.GONE
        binding.textLainnya.visibility =View.GONE
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}