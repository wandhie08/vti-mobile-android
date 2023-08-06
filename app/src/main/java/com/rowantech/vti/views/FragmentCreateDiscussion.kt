package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.CreateCommentRequest
import com.rowantech.vti.data.model.request.CreateDiscussionRequest
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.databinding.FragmentCreateDiscussionBinding
import com.rowantech.vti.databinding.FragmentListDiscussionTypeBinding
import com.rowantech.vti.databinding.FragmentLoginBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import javax.inject.Inject

class FragmentCreateDiscussion : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentCreateDiscussionBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }
    internal lateinit var dataCustomer: LoginResponse
    var loginRequest = LoginRequest()
    internal lateinit var data: EventsItem
    var createDiscussionRequest = CreateDiscussionRequest()
    @SuppressLint("HardwareIds")
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
            createDiscussionRequest.type = "JADWAL"

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}