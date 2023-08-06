package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.*
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentDescEventBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.*
import javax.inject.Inject


class FragmentDescEvent : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors


    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    internal lateinit var data: EventsItem
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapterEvent by autoCleared<ListAdapterEvent>()
    private var adapterBanner by autoCleared<ListAdapterBanner>()
    private var adapterDiscussion by autoCleared<ListAdapterDiscussion>()
    var getEventTerkaitRequest = GetEventTerkaitRequest()
    internal lateinit var dataLogin: LoginResponse
    var registerEventRequest = RegisterEventRequest()
    var getStatusTemplateRequest = GetStatusTemplateRequest()
    private var adapterPendaftaran by autoCleared<ListAdapterFromPendaftaran>()
    private var adapterUpload by autoCleared<ListAdapterFromUpload>()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDescEventBinding.inflate(inflater, container, false)
        context ?: return binding.root

        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)

        binding.nameBrand.text = data.title
        binding.companyBrand.text = data.companyName
        binding.descEvent.text = data.shortDescription
        getEventTerkaitRequest.companyId = data.companyId
        getEventTerkaitRequest.eventId = data.eventId



        binding.textDateEvent.text = data.periodeDate

        if (data.location == "online") {
            binding.textLocationType.text = "Online"
        } else if (data.location == "offline") {
            binding.textLocationType.text = "Offline"

        }

        if (data.checkIn == "N") {
            binding.btnCheckIn.visibility = View.GONE
        } else if (data.checkIn == "Y") {
            binding.btnCheckIn.visibility = View.VISIBLE

        }

        if (data.winningPrize == "N") {
            binding.btnHadiah.visibility = View.GONE
        } else if (data.winningPrize == "Y") {
            binding.btnHadiah.visibility = View.VISIBLE
            binding.btnHadiah.setOnClickListener {
                dialogHadiah()
            }
        }

        if (data.eventType == "free") {
            binding.textBerbayar.text = "Gratis"
        } else if (data.eventType == "paid") {
            binding.textBerbayar.text = "Berbayar"
        }

        if (data.eventType == "paid") {
            binding.btnBerbayar.setOnClickListener {
                dialogProduct()
            }
        }
        if (data.formRegistration == "N") {
            binding.btnPendaftaran.visibility = View.GONE
        } else if (data.formRegistration == "Y") {
            binding.btnPendaftaran.visibility = View.VISIBLE
            binding.btnPendaftaran.setOnClickListener({
                dialogFormPendaftaran()
            })
        }

        binding.btnCheckIn.setOnClickListener({
            dialogCheckIn()
        })
        if (data.formValidation == "N") {
            binding.btnVerifikasi.visibility = View.GONE
        } else if (data.formValidation == "Y") {
            binding.btnVerifikasi.visibility = View.VISIBLE

        }

        if (data.submission == "N") {
            binding.btnUpload.visibility = View.GONE
        } else if (data.submission == "Y") {
            binding.btnUpload.visibility = View.VISIBLE

        }

        binding.btnUpload.setOnClickListener({
            dialogFormUpload()
        })
        binding.btnDateEvent.setOnClickListener({
            dialogDateEvent()
        })

        binding.btnComment.setOnClickListener({
            val bundle = Bundle()
            bundle.putString("data", Gson().toJson(data))
            findNavController().navigate(
                R.id.fragmentListDiscussionType,
                bundle
            )
        })
        getAllDiscussion(binding, data)
        // binding.textDateEvent.text = data.
        getAllBanner(binding, data)
        //Glide.with(this).load(data.avatar).into(binding.iconBrand)
        data.avatar?.let { binding.iconBrand.loadUrl(it) }
        if (!TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
            dataLogin = Gson().fromJson(
                MainApplication().getStringPref(context, "dataLogin"),
                LoginResponse::class.java
            )

            if (dataLogin.customer != null) {
                registerEventRequest.eventId = data.eventId
                registerEventRequest.customerId = dataLogin.customer!!.customerId

                getStatusTemplateRequest.eventId = data.eventId
                getStatusTemplateRequest.customerId = dataLogin.customer!!.customerId
            }

            subscribeEvent(binding, Constant.STATUS_EVENT)
        }
        getEventByType(
            binding,
            "REGISTRATION|EVALUATION REGISTRATION|SUBMISSION|ONGOING|EVALUATION EVENT"
        )

        binding.recycleViewDiscussion.visibility = View.GONE
        binding.layoutBrand.visibility = View.GONE

        if (data.type == "CLOSED") {
            binding.btnRegistrasi.visibility = View.GONE
        } else if (data.formValidation == "Y") {
            binding.btnVerifikasi.visibility = View.VISIBLE

        }
        binding.btnRegistrasi.setBackgroundResource(R.drawable.button_blue)
        println("data.type :" + data.type)
        if (data.type == "CLOSED") {
            println("data.type SELESAI:" + data.type)
            binding.btnRegistrasi.visibility = View.VISIBLE
            binding.btnRegistrasi.setBackgroundResource(R.drawable.button_grey)
            binding.btnRegistrasi.setText("SELESAI")
            //binding.btnRegistrasi.isEnabled =false
        } else if (data.type == "ONGOING|REGISTRATION") {
            binding.btnRegistrasi.visibility = View.VISIBLE
            binding.btnRegistrasi.setBackgroundResource(R.drawable.button_blue)
        }

        binding.btnRegistrasi.setOnClickListener {
            if (TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
                findNavController().navigate(R.id.fragmentLogin)
            } else {
                if (binding.btnRegistrasi.text.toString() == "TERDAFTAR") {
                    val bundle = Bundle()
                    bundle.putString("data", Gson().toJson(data))
                    if (data.formRegistration == "Y") {
                        progressShow()

                        mainViewModel.paramWithBody(
                            "",
                            Constant.GET_STATUS_TEMPLATE,
                            Gson().toJson(getStatusTemplateRequest)
                        )
                        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                            progressDismis()

                            if (result.status == Status.SUCCESS) {
                                val response =
                                    Gson().fromJson(
                                        result.data,
                                        GetStatusRegisterEventResponse::class.java
                                    )
                                if (response.status == "Y") {

                                    if (data.eventType == "paid") {
                                        val bundle = Bundle()
                                        bundle.putString("data", Gson().toJson(data))
                                        findNavController().navigate(
                                            R.id.fragmentProductPayment,
                                            bundle
                                        )
                                    } else if (data.eventType == "free") {
                                        val bundle = Bundle()
                                        bundle.putString("data", Gson().toJson(data))
                                        findNavController().navigate(R.id.fragmentHome, bundle)

                                    }

                                } else {
                                    findNavController().navigate(
                                        R.id.fragmentCreateTemplate,
                                        bundle
                                    )
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

                    } else {
                        if (data.eventType == "paid") {
                            findNavController().navigate(R.id.fragmentCreateTemplate, bundle)
                        }
                    }
                } else {
                    dataLogin = Gson().fromJson(
                        MainApplication().getStringPref(context, "dataLogin"),
                        LoginResponse::class.java
                    )

                    if (dataLogin.customer != null) {
                        registerEventRequest.eventId = data.eventId
                        registerEventRequest.customerId = dataLogin.customer!!.customerId


                    }

                    subscribeEvent(binding, Constant.SUBSCRIBE_EVENT)
                }
            }
        }

        binding.header.findViewById<AppCompatImageView>(R.id.iconMenu).setOnClickListener {
            if (TextUtils.isEmpty(MainApplication().getStringPref(context, "dataLogin"))) {
                findNavController().navigate(R.id.fragmentLogin)
            } else {
                findNavController().navigate(R.id.fragmentDialogNotifications)
            }
        }

        binding.layoutTentangEvent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("data", Gson().toJson(data))
            findNavController().navigate(R.id.fragmentTabsEvent, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun getAllBanner(binding: FragmentDescEventBinding, eventsItem: EventsItem) {
        val adapterBanner = ListAdapterBanner(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: BannersItem ->
                onClickDataBanner(
                    binding,
                    partItem
                )
            }) { contributor, imageView ->
        }
        this.adapterBanner = adapterBanner

        binding.recycleViewBanner.adapter = adapterBanner
        binding.recycleViewBanner.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()
        binding.recycleViewBanner.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }


        adapterBanner.submitList(eventsItem.banners)


    }

    fun getAllDiscussion(binding: FragmentDescEventBinding, eventsItem: EventsItem) {
        val adapterDiscussion = ListAdapterDiscussion(
            dataBindingComponent,
            requireContext(),
            appExecutors,
            { partItem: DiscussionsItem ->
                onClickDataDiscussion(
                    binding,
                    partItem
                )
            })

        this.adapterDiscussion = adapterDiscussion

        binding.recycleViewDiscussion.adapter = adapterDiscussion
        binding.recycleViewDiscussion.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()
        binding.recycleViewDiscussion.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        pageRequest.eventId = data.eventId
        pageRequest.page = 0
        pageRequest.size = 100
        mainViewModel.paramWithBody(
            "",
            Constant.LIST_DISCUSSION,
            Gson().toJson(pageRequest)
        )

        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS) {
                getDiscussionResponse =
                    Gson().fromJson(result.data, GetDiscussionResponse::class.java)
                if (getDiscussionResponse.discussions!!.size>0){
                    binding.recycleViewDiscussion.visibility =View.VISIBLE
                }
                adapterDiscussion.submitList(getDiscussionResponse.discussions)
            }
        })


    }

    fun getEventByType(binding: FragmentDescEventBinding, typeEvent: String) {

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


        binding.recycleViewEventTerkait.adapter = adapterEvent
        binding.recycleViewEventTerkait.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()

        binding.recycleViewEventTerkait.getViewTreeObserver()
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        getEventTerkaitRequest.page = 0
        getEventTerkaitRequest.size = 100
        mainViewModel.paramWithBody(
            "",
            Constant.EVENT_TERKAIT,
            Gson().toJson(getEventTerkaitRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

            if (result.status == Status.SUCCESS) {
                val response =
                    Gson().fromJson(result.data, GetEventByTypeResponse::class.java)
                adapterEvent.submitList(response.events)
                if (response.events!!.size > 0) {


                    binding.layoutBrand.visibility = View.VISIBLE

                }
            } else {

            }
        })
    }

    private fun onClickDataEvent(binding: FragmentDescEventBinding, partItem: EventsItem) {
        val bundle = Bundle()
        bundle.putString("data", Gson().toJson(partItem))
        findNavController().navigate(R.id.fragmentDescEvent, bundle)
    }

    private fun dialogDateEvent() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_tanggal)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    private fun dialogLocation() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_location)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    var getEventTerkaitResponse = GetFormTemplateResponse()

    private fun dialogFormPendaftaran() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_pendaftaran)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        val recycleViewProduk = dialogView.findViewById<RecyclerView>(R.id.recycleViewProduk)
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
        dialogView.show()
    }

    private fun dialogFormUpload() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_upload)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        val recycleViewProduk = dialogView.findViewById<RecyclerView>(R.id.recycleViewProduk)
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
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    private fun dialogHadiah() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_hadiah)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        val recycleViewProduk = dialogView.findViewById<RecyclerView>(R.id.recycleViewProduk)
        val textItemRight = dialogView.findViewById<TextView>(R.id.textItemRight)
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
                textItemRight.text = getParameterResponse.parameters!!.size.toString()+" kategori"
            }
        })
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    internal lateinit var getProductEventResponse: GetProductEventResponse
    internal lateinit var getParameterResponse: GetParameterResponse

    private var adapterProduct by autoCleared<ListAdapterProductPaid>()
    private var adapterPrize by autoCleared<ListAdapterPrize>()

    var pageRequest = GetDiscussionRequest()
    internal lateinit var getDiscussionResponse: GetDiscussionResponse

    private fun dialogProduct() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_berbayar)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        val recycleViewProduk = dialogView.findViewById<RecyclerView>(R.id.recycleViewProduk)
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
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    private fun dialogCheckIn() {
        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_checkin)
        val qrisLogo = dialogView.findViewById<ImageView>(R.id.qrisLogo)
        data.locationCheckIn?.let { qrisLogo.loadUrl(it) }
        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    private fun onClickDataDiscussion(
        binding: FragmentDescEventBinding,
        partItem: DiscussionsItem
    ) {
        val bundle = Bundle()
        bundle.putString("data", Gson().toJson(partItem))
        bundle.putString("dataEvent", Gson().toJson(data))
        findNavController().navigate(R.id.fragmentListDiscussion, bundle)

    }

    private fun onClickDataBanner(binding: FragmentDescEventBinding, partItem: BannersItem) {
//        val bundle = Bundle()
//        bundle.putString("data", Gson().toJson(partItem))
//        findNavController().navigate(R.id.fragmentDetailBanner, bundle)

        val dialogView =
            Dialog(requireContext(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog)

        dialogView.requestWindowFeature(
            Window.FEATURE_NO_TITLE
        )
        dialogView.setContentView(R.layout.dialog_image)

        dialogView.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        val btnClose = dialogView.findViewById<AppCompatButton>(R.id.btnClose)
        val imageBanner = dialogView.findViewById<ZoomImageView>(R.id.imageBanner)

        //Glide.with(this).load(partItem.banner).into(imageBanner)
        Glide.with(this)
            .asBitmap()
            .load(partItem.banner)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap?>?) {
                    val w = bitmap.width
                    val h = bitmap.height
                    imageBanner.setImageBitmap(bitmap)

                    if (w > h) {

                    } else {

                    }
                }

            })
        // partItem.banner?.let { imageBanner.loadUrl(it) }
        btnClose.setOnClickListener({
            dialogView.dismiss()
        })
        imageBanner.swipeToDismissEnabled = true
        imageBanner.onDismiss = {
            dialogView.show()
        }
        dialogView.getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogView.show()
    }

    fun subscribeEvent(binding: FragmentDescEventBinding, typeSubscribe: String) {
        progressShow()


        mainViewModel.paramWithBody(
            "",
            typeSubscribe,
            Gson().toJson(registerEventRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
            progressDismis()

            if (result.status == Status.SUCCESS) {


                if (typeSubscribe == Constant.STATUS_EVENT) {
                    val response =
                        Gson().fromJson(result.data, GetStatusRegisterEventResponse::class.java)
                    if (data.type != "CLOSED") {
                        if (response.registrationStatus == "Y") {
                            binding.btnRegistrasi.setText("TERDAFTAR")
                            binding.btnRegistrasi.setBackgroundResource(R.drawable.button_grey)
                        } else {
                            binding.btnRegistrasi.setText("DAFTAR")
                            binding.btnRegistrasi.setBackgroundResource(R.drawable.button_blue)
                        }
                    }
                } else if (typeSubscribe == Constant.SUBSCRIBE_EVENT) {
                    Snackbar.make(
                        binding!!.root,
                        "Anda berhasil daftar event",
                        Snackbar.LENGTH_LONG
                    ).show()

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