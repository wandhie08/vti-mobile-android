package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.*
import com.rowantech.vti.data.model.request.Transaction
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentHomeBinding
import com.rowantech.vti.databinding.FragmentPengirimanBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterJasaPengiriman
import com.rowantech.vti.views.adapter.ListAdapterProduct
import com.rowantech.vti.views.adapter.ListAdapterProductPengiriman
import javax.inject.Inject

class FragmentPengiriman : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentPengirimanBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    val listProductEvent: MutableList<ItemOrder> = mutableListOf()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapterProduct by autoCleared<ListAdapterProductPengiriman>()
    private var adapterJasaPengiriman by autoCleared<ListAdapterJasaPengiriman>()

    var pageRequest = GetDiscussionRequest()
    internal lateinit var data: EventsItem
    internal lateinit var createOrderRequest: CreateOrderRequest
    var getPricingRequest = GetPricingRequest()
    internal lateinit var getProductEventResponse: GetProductEventResponse
    internal lateinit var getPricingResponse: GetPricingResponse

    internal lateinit var dataLogin: LoginResponse
    val listItemOrderOrder: MutableList<ItemOrder> = mutableListOf()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPengirimanBinding.inflate(inflater, container, false)
        context ?: return binding.root
        dataLogin = Gson().fromJson(
            MainApplication().getStringPref(context, "dataLogin"),
            LoginResponse::class.java
        )


        data = Gson().fromJson(arguments?.getString("dataEvent"), EventsItem::class.java)
        createOrderRequest =
            Gson().fromJson(arguments?.getString("data"), CreateOrderRequest::class.java)

        binding.alamatValue.text = arguments?.getString("address")
        binding.totalValue.text =
            NumberUtil.moneyFormat(createOrderRequest.transaction!!.totalValue.toString())
        pageRequest.eventId = data.eventId
        binding.btnRegistrasi.setOnClickListener {
            if (binding.alamatValue.text == "-") {
                Snackbar.make(
                    binding.root,
                    "Silahkan Pilih Alamat Pengiriman",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (binding.kurirValue.text == "-") {
                Snackbar.make(
                    binding.root,
                    "Silahkan Pilih Jasa Pengiriman",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (binding.accountValue.text == "-") {
                Snackbar.make(
                    binding.root,
                    "Silahkan Pilih Metode Pembayaran",
                    Snackbar.LENGTH_LONG
                )
                    .show()

            } else {
                createOrderRequest.transaction!!.uniqueCode = 1

                if (binding.accountValue.text == "Transfer") {
                    createOrderRequest.transaction!!.methodPayment = "transfer"
                } else {
                    createOrderRequest.transaction!!.methodPayment = "qris"

                }
                //createOrderRequest.transaction =transaction
                mainViewModel.paramWithBody(
                    "",
                    Constant.CREATE_ORDER,
                    Gson().toJson(createOrderRequest)
                )
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

                    if (result.status == Status.SUCCESS) {
                        if (binding.accountValue.text == "Transfer") {
                            val bundle = Bundle()
                            bundle.putString("dataEvent", Gson().toJson(data))
                            bundle.putString("address", Gson().toJson(data))
                            bundle.putString("courier", Gson().toJson(data))
                            bundle.putString("data", result.data)
                            findNavController().navigate(R.id.fragmentCreateInvoice, bundle)

                        } else {
                            val bundle = Bundle()
                            bundle.putString("dataEvent", Gson().toJson(data))
                            bundle.putString("address", Gson().toJson(data))
                            bundle.putString("courier", Gson().toJson(data))
                            bundle.putString("data", result.data)
                            findNavController().navigate(R.id.fragmentQRIS, bundle)

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
        }

        binding.btnHomeAddress.setOnClickListener({
            methodAddress(binding)
        })

        binding.btnKurir.setOnClickListener({
            methodCourier(binding)
        })

        binding.btnAccountBalance.setOnClickListener({
            methodPayment(binding)
        })

        getAllProduct(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun methodPayment(binding: FragmentPengirimanBinding) {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_method_payment)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        val btnTransfer = dialogView.findViewById<RadioButton>(R.id.transferBtn)
        val qrisBtn = dialogView.findViewById<RadioButton>(R.id.qrisBtn)

        btnTransfer.setOnClickListener({
            btnTransfer.isChecked = true
            qrisBtn.isChecked = false
        })

        qrisBtn.setOnClickListener({
            btnTransfer.isChecked = false
            qrisBtn.isChecked = true


        })

        val btnSave = dialogView.findViewById<AppCompatButton>(R.id.btnRegistrasi)
        btnSave.setOnClickListener({
            dialogView.dismiss()

            if (btnTransfer.isChecked) {
                binding.accountValue.text = "Transfer"
            } else {
                binding.accountValue.text = "QRIS"
            }
        })

        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    lateinit var dialogViewCourier: Dialog


    fun methodCourier(binding: FragmentPengirimanBinding) {
        dialogViewCourier =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogViewCourier.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogViewCourier.setContentView(R.layout.dialog_jasa_pengiriman)

        dialogViewCourier.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogViewCourier.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val btnSave = dialogViewCourier.findViewById<AppCompatButton>(R.id.btnRegistrasi)
        btnSave.setOnClickListener({
            dialogViewCourier.dismiss()
        })
        val recycleViewJasaPengiriman =
            dialogViewCourier.findViewById<RecyclerView>(R.id.recycleViewBanner)
        val adapterJasaPengiriman = ListAdapterJasaPengiriman(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: DataCourier -> onClickDataBrands(binding, partItem) },
            { partItem: DataCourier -> onClickDataAdd(binding, partItem) },
            { partItem: DataCourier ->
                onClickDataDelete(
                    binding,
                    partItem
                )
            }) { contributor, imageView ->
        }

        this.adapterJasaPengiriman = adapterJasaPengiriman

        recycleViewJasaPengiriman.adapter = adapterJasaPengiriman
        recycleViewJasaPengiriman.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        postponeEnterTransition()
        recycleViewJasaPengiriman.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        getPricingRequest.destinationLat = createOrderRequest.destinationLat
        getPricingRequest.destinationSubdistrictCode = createOrderRequest.destinationSubdistrictCode
        getPricingRequest.destinationPostalCode = createOrderRequest.destinationPostalCode?.toInt()
        getPricingRequest.originCityCode = createOrderRequest.originCityCode
        getPricingRequest.destinationProvinceName = createOrderRequest.destinationProvinceName
        getPricingRequest.originLong = createOrderRequest.originLong
        getPricingRequest.isCod = createOrderRequest.isCod
        getPricingRequest.originLat = createOrderRequest.originLat
        getPricingRequest.originProvinceCode = createOrderRequest.originProvinceCode
        getPricingRequest.originSubdistrictName = createOrderRequest.originSubdistrictName
        getPricingRequest.originCityName = createOrderRequest.originCityName
        getPricingRequest.destinationProvinceCode = createOrderRequest.destinationProvinceCode
        getPricingRequest.originSubdistrictCode = createOrderRequest.originSubdistrictCode
        getPricingRequest.originPostalCode = createOrderRequest.originPostalCode?.toInt()
        getPricingRequest.destinationLong = createOrderRequest.destinationLong
        getPricingRequest.originProvinceName = createOrderRequest.originProvinceName
        getPricingRequest.destinationCityCode = createOrderRequest.destinationCityCode
        getPricingRequest.destinationSubdistrictName = createOrderRequest.destinationSubdistrictName
        getPricingRequest.items = createOrderRequest.items
        getPricingRequest.destinationCityName = createOrderRequest.destinationCityName

        mainViewModel.paramWithBody(
            "",
            Constant.GET_PRICING,
            Gson().toJson(getPricingRequest)
        )

        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                getPricingResponse =
                    Gson().fromJson(result.data, GetPricingResponse::class.java)
                adapterJasaPengiriman.submitList(getPricingResponse.data)
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
        dialogViewCourier.show()
    }

    var getPostalCodeResponse = GetPostalCodeResponse()
    var transaction = Transaction()

    fun methodAddress(binding: FragmentPengirimanBinding) {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_data_penerima)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val formAddress = dialogView.findViewById<EditText>(R.id.formAddress)
        val formNamaPenerima = dialogView.findViewById<EditText>(R.id.formNamaPenerima)
        val formCity = dialogView.findViewById<EditText>(R.id.formCity)
        val formKec = dialogView.findViewById<EditText>(R.id.formKec)
        val formNoHandphonePenerima =
            dialogView.findViewById<EditText>(R.id.formNoHandphonePenerima)
        val formProvinsi = dialogView.findViewById<EditText>(R.id.formProvinsi)
        val formKodePos = dialogView.findViewById<EditText>(R.id.formKodePos)
        val formKurir = dialogView.findViewById<EditText>(R.id.formKurir)
        val getPostalCodeRequest = GetPostalCodeRequest()
        val btnKodePos = dialogView.findViewById<AppCompatButton>(R.id.btnKodePos)
        btnKodePos.setOnClickListener({
            getPostalCodeRequest.name = formKodePos.text.toString()

            mainViewModel.paramWithBody(
                "",
                Constant.GET_POSTAL_CODE,
                Gson().toJson(getPostalCodeRequest)
            )
            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

                if (result.status == Status.SUCCESS) {
                    getPostalCodeResponse =
                        Gson().fromJson(result.data, GetPostalCodeResponse::class.java)
                    if (getPostalCodeResponse.data!!.size > 0) {
                        formProvinsi.setText(getPostalCodeResponse.data!![0]!!.provinceName)
                        formCity.setText(getPostalCodeResponse.data!![0]!!.cityName)
                        formKec.setText(getPostalCodeResponse.data!![0]!!.subdistrictName)
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
        })
        val btnSave = dialogView.findViewById<AppCompatButton>(R.id.btnSimpanAddress)
        btnSave.setOnClickListener({
            if (TextUtils.isEmpty(formNamaPenerima.getText().toString())) {
                Snackbar.make(
                    binding.root,
                    "Nama penerima tidak boleh kosong",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (TextUtils.isEmpty(formNoHandphonePenerima.getText().toString())) {
                Snackbar.make(
                    binding.root,
                    "No handphone penerima  tidak boleh kosong",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (TextUtils.isEmpty(formKodePos.getText().toString())) {
                Snackbar.make(binding.root, "Kode Pos tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else if (TextUtils.isEmpty(formProvinsi.getText().toString())) {
                Snackbar.make(binding.root, "Provinsi tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(formCity.getText().toString())) {
                Snackbar.make(
                    binding.root,
                    "Kota/Kabupaten tidak boleh kosong",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (TextUtils.isEmpty(formKec.getText().toString())) {
                Snackbar.make(
                    binding.root,
                    "Kecamatan/Keluarahan tidak boleh kosong",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (TextUtils.isEmpty(formAddress.getText().toString())) {
                Snackbar.make(binding.root, "Alamat tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else {
                binding.alamatValue.text = formAddress.getText().toString()
                createOrderRequest.destinationNote = formKurir.getText().toString()
                createOrderRequest.destinationContactName = formNamaPenerima.getText().toString()
                createOrderRequest.destinationContactPhone =
                    formNoHandphonePenerima.getText().toString()
                createOrderRequest.destinationContactEmail = dataLogin.customer!!.email
                createOrderRequest.destinationCityCode =
                    getPostalCodeResponse.data?.get(0)!!.cityCode
                createOrderRequest.destinationCityName =
                    getPostalCodeResponse.data?.get(0)!!.cityName
                createOrderRequest.destinationProvinceCode =
                    getPostalCodeResponse.data?.get(0)!!.provinceCode
                createOrderRequest.destinationProvinceName =
                    getPostalCodeResponse.data?.get(0)!!.provinceName
                createOrderRequest.destinationSubdistrictCode =
                    getPostalCodeResponse.data?.get(0)!!.subdistrictCode
                createOrderRequest.destinationSubdistrictName =
                    getPostalCodeResponse.data?.get(0)!!.subdistrictName
                createOrderRequest.destinationContactAddress = formAddress.getText().toString()
                createOrderRequest.destinationPostalCode = formKodePos.getText().toString()

                dialogView.dismiss()

            }
        })
        dialogView.show()
    }

    fun getAllProduct(binding: FragmentPengirimanBinding) {
        val adapterProduct = ListAdapterProductPengiriman(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: ItemOrder -> onClickDataBrands(binding, partItem) },
            { partItem: ItemOrder -> onClickDataAdd(binding, partItem) },
            { partItem: ItemOrder ->
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
        adapterProduct.submitList(createOrderRequest.items)

    }

    private fun onClickDataBrands(binding: FragmentPengirimanBinding, partItem: ItemOrder) {

    }

    private fun onClickDataAdd(binding: FragmentPengirimanBinding, partItem: ItemOrder) {

    }

    private fun onClickDataDelete(binding: FragmentPengirimanBinding, partItem: ItemOrder) {

    }

    private fun onClickDataBrands(binding: FragmentPengirimanBinding, partItem: DataCourier) {

    }

    private fun onClickDataAdd(binding: FragmentPengirimanBinding, partItem: DataCourier) {
        binding.kurirValue.text = partItem.courier
        createOrderRequest.courier = partItem.courierCode
        createOrderRequest.courierService = partItem.service
        createOrderRequest.orderNumber = "00001"
        createOrderRequest.transaction!!.subtotal = createOrderRequest.transaction!!.totalValue?.plus(
            partItem.price!!
        )

        createOrderRequest.transaction!!.totalValue = createOrderRequest.transaction!!.totalValue?.plus(
            partItem.price!!
        )
        binding.totalValue.text =
            NumberUtil.moneyFormat(createOrderRequest.transaction!!.totalValue.toString())
        dialogViewCourier.dismiss()
    }

    private fun onClickDataDelete(binding: FragmentPengirimanBinding, partItem: DataCourier) {

    }

}