package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent

import com.rowantech.vti.data.model.request.CreateCommentRequest
import com.rowantech.vti.data.model.request.GetDiscussionRequest
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentDescEventBinding
import com.rowantech.vti.databinding.FragmentDetailDiscussionBinding
import com.rowantech.vti.databinding.FragmentLoginBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterDiscussion
import com.rowantech.vti.views.adapter.ListAdapterProductPaid
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FragmentListDiscussion : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentDetailDiscussionBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    internal lateinit var dataCustomer: LoginResponse

    var createCommentRequest = CreateCommentRequest()
    internal lateinit var data: EventsItem
    internal lateinit var dataDiscussion: DiscussionsItem
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailDiscussionBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("dataEvent"), EventsItem::class.java)
        dataDiscussion = Gson().fromJson(arguments?.getString("data"), DiscussionsItem::class.java)
        dataCustomer = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)
        getAllDiscussion(binding,data)
        binding.btnSend.setOnClickListener({
            createCommentRequest.discussionId = dataDiscussion.discussionId
            createCommentRequest.isAdmin = "N"
            createCommentRequest.customerId = dataCustomer.customer!!.customerId
            createCommentRequest.title = binding.formMessage.text.toString()
            createCommentRequest.message = binding.formMessage.text.toString()
            createCommentRequest.type = dataDiscussion.type

            mainViewModel.paramWithBody(
                "",
                Constant.CREATE_COMMENT,
                Gson().toJson(createCommentRequest)
            )

            mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                if (result.status == Status.SUCCESS) {
                    Snackbar.make(
                        binding!!.root,
                        "Komentar berhasil ditambahkan",
                        Snackbar.LENGTH_LONG
                    ).show()

                    findNavController().navigate(R.id.fragmentHome)
                }
            })
        })
        return binding.root
    }

    private var adapterDiscussion by autoCleared<ListAdapterDiscussion>()
    var pageRequest = GetDiscussionRequest()
    internal lateinit var getDiscussionResponse: GetDiscussionResponse
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private fun onClickDataDiscussion(
        binding: FragmentDetailDiscussionBinding,
        partItem: DiscussionsItem
    ) {
    }
    fun getAllDiscussion(binding: FragmentDetailDiscussionBinding, eventsItem: EventsItem) {
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

        binding.recycleViewProduk.adapter = adapterDiscussion
        binding.recycleViewProduk.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postponeEnterTransition()
        binding.recycleViewProduk.getViewTreeObserver()
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
                    binding.recycleViewProduk.visibility =View.VISIBLE
                }
                adapterDiscussion.submitList(getDiscussionResponse.discussions)
            }
        })


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}