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
    internal lateinit var dataLogin: LoginResponse
    val listProductsItemOrder: MutableList<ItemOrder> = mutableListOf()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductPaymentBinding.inflate(inflater, container, false)
        context ?: return binding.root
        dataLogin = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)


        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        pageRequest.eventId = data.eventId
        binding.btnRegistrasi.setOnClickListener {
            createOrderRequest.courier ="sicepat"
            createOrderRequest.courierService ="SIUNT"
            createOrderRequest.orderNumber ="00001"
            createOrderRequest.isCod =false
            createOrderRequest.isSendCompany =false
            createOrderRequest.deliveryType ="pickup"
            createOrderRequest.deliveryTime ="06/05/2022 04:52:00 PM +00:00"
            createOrderRequest.originNote = "Rumah Deket SD"
            createOrderRequest.originContactName ="Wandhie Dimyati"
            createOrderRequest.originContactPhone ="087730188988"
            createOrderRequest.originContactEmail ="wandhie.dimyati@gmail.com"
            createOrderRequest.originCityCode = "32.04"
            createOrderRequest.originCityName = "KAB. BANDUNG"
            createOrderRequest.originProvinceCode = "32"
            createOrderRequest.originProvinceName = "JAWA BARAT"
            createOrderRequest.originSubdistrictCode = "32.04.35"
            createOrderRequest.originSubdistrictName = "PASEH"
            createOrderRequest.originContactAddress = "Kapung Cihaneut Desa Drawati Kec Paseh"
            createOrderRequest.originPostalCode = "40383"
            println("dataLogin.customer!!.customerId :"+dataLogin.customer!!.customerId)
            transaction.customerId =dataLogin.customer!!.customerId
            transaction.eventId =data.eventId
            transaction.uniqueCode =1
            transaction.shippingCharge =0
            transaction.feeInsurance =0
            transaction.isInsuranced =false
            transaction.discount =0
            transaction.totalCod =0
            transaction.packageCategory ="NORMAL"
            transaction.packageContent =getProductEventResponse.products!!.get(0)!!.title


            transaction.subtotal =1012
            transaction.totalValue =1012
            transaction.weight =10
            transaction.width =10
            transaction.height =10
            transaction.length =10
            transaction.coolie =10

            createOrderRequest.transaction = transaction
            for (i in 0 until getProductEventResponse.products!!.size) {
                val itemOrder = ItemOrder()
                itemOrder.productId = getProductEventResponse.products!!.get(i)!!.productId
                itemOrder.name = getProductEventResponse.products!!.get(i)!!.title
                itemOrder.description = getProductEventResponse.products!!.get(i)!!.description
                itemOrder.weight = getProductEventResponse.products!!.get(i)!!.weight
                itemOrder.weightUom = "kg"
                itemOrder.qty = getProductEventResponse.products!!.get(i)!!.stock
                itemOrder.value = getProductEventResponse.products!!.get(i)!!.price
                itemOrder.width = getProductEventResponse.products!!.get(i)!!.volumeWidth
                itemOrder.height = getProductEventResponse.products!!.get(i)!!.volumeHeight
                itemOrder.dimensionUom = "cm"
                itemOrder.length = getProductEventResponse.products!!.get(i)!!.volumeLength
                itemOrder.totalValue = getProductEventResponse.products!!.get(i)!!.stock?.times(
                    getProductEventResponse.products!!.get(i)!!.price!!
                )
                listProductsItemOrder.add(itemOrder)
            }
            createOrderRequest.items =listProductsItemOrder
            if (binding.accountValue.text == "Transfer") {
                transaction.methodPayment ="transfer"
            }else{
                transaction.methodPayment ="qris"

            }
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

    fun methodPayment(binding: FragmentProductPaymentBinding) {
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

    fun methodCourier(binding: FragmentProductPaymentBinding) {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_jasa_pengiriman)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val btnSave = dialogView.findViewById<AppCompatButton>(R.id.btnRegistrasi)
        btnSave.setOnClickListener({
            dialogView.dismiss()
        })
        dialogView.show()
    }

    var getPostalCodeResponse = GetPostalCodeResponse()
    var transaction = Transaction()

    var createOrderRequest = CreateOrderRequest()
    fun methodAddress(binding: FragmentProductPaymentBinding) {
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
                createOrderRequest.destinationNote =formKurir.getText().toString()
                createOrderRequest.destinationContactName =formNamaPenerima.getText().toString()
                createOrderRequest.destinationContactPhone =formNoHandphonePenerima.getText().toString()
                createOrderRequest.destinationContactEmail =dataLogin.customer!!.email
                createOrderRequest.destinationCityCode = getPostalCodeResponse.data?.get(0)!!.cityCode
                createOrderRequest.destinationCityName = getPostalCodeResponse.data?.get(0)!!.cityName
                createOrderRequest.destinationProvinceCode = getPostalCodeResponse.data?.get(0)!!.provinceCode
                createOrderRequest.destinationProvinceName = getPostalCodeResponse.data?.get(0)!!.provinceName
                createOrderRequest.destinationSubdistrictCode = getPostalCodeResponse.data?.get(0)!!.subdistrictCode
                createOrderRequest.destinationSubdistrictName = getPostalCodeResponse.data?.get(0)!!.subdistrictName
                createOrderRequest.destinationContactAddress = formAddress.getText().toString()
                createOrderRequest.destinationPostalCode = formKodePos.getText().toString()

                dialogView.dismiss()

            }
        })
        dialogView.show()
    }

    fun getAllProduct(binding: FragmentProductPaymentBinding) {
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
            if (getProductEventResponse.products!!.get(i)!!.productId == partItem.productId) {
                getProductEventResponse.products!!.get(i)?.stock = partItem.stock?.plus(1)
            }
        }

        adapterProduct.notifyDataSetChanged()
        totalTagihan(binding)
    }

    private fun onClickDataDelete(binding: FragmentProductPaymentBinding, partItem: ProductsItem) {
        for (i in 0 until getProductEventResponse.products!!.size) {
            if (getProductEventResponse.products!!.get(i)!!.productId == partItem.productId) {
                if (getProductEventResponse.products?.get(i)?.stock!! > 0) {
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