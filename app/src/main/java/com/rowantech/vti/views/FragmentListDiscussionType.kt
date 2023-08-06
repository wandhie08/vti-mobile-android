package com.rowantech.vti.views

import android.annotation.SuppressLint
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
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.binding.FragmentDataBindingComponent
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.GetDiscussionRequest
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.response.DiscussionsItem
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.data.model.response.GetDiscussionResponse
import com.rowantech.vti.databinding.FragmentDetailDiscussionBinding
import com.rowantech.vti.databinding.FragmentListDiscussionTypeBinding
import com.rowantech.vti.databinding.FragmentLoginBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterDiscussion
import javax.inject.Inject

class FragmentListDiscussionType : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentListDiscussionTypeBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    var loginRequest = LoginRequest()
    internal lateinit var data: EventsItem

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListDiscussionTypeBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        getAllDiscussion(binding,data)
        binding.btnAddComment.setOnClickListener({
            val bundle = Bundle()
            bundle.putString("data", Gson().toJson(data))
            findNavController().navigate(
                R.id.fragmentCreateDiscussion,
                bundle
            )
        })
        return binding.root
    }

    private var adapterDiscussion by autoCleared<ListAdapterDiscussion>()
    var pageRequest = GetDiscussionRequest()
    internal lateinit var getDiscussionResponse: GetDiscussionResponse
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private fun onClickDataDiscussion(
        binding: FragmentListDiscussionTypeBinding,
        partItem: DiscussionsItem
    ) {
    }
    fun getAllDiscussion(binding: FragmentListDiscussionTypeBinding, eventsItem: EventsItem) {
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