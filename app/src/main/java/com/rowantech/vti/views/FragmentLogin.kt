package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
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
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.data.model.response.MessageResponse
import com.rowantech.vti.databinding.FragmentLoginBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FragmentLogin : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentLoginBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    var loginRequest = LoginRequest()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val sdfTime = SimpleDateFormat("HH:mm:ss")
        val currentTime = sdfTime.format(Date())

        binding.btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(binding.formUsername.getText().toString())) {
                Snackbar.make(binding.root, "Username tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formPassword.getText().toString())) {
                Snackbar.make(binding.root, "Password tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else {


                progressShow()
                loginRequest.userEmail = binding.formUsername.text.toString()
                loginRequest.password = encryptThisString(binding.formPassword.text.toString())

                mainViewModel.login(loginRequest)
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                    progressDismis()

                    if (result.status == Status.SUCCESS) {

                        val dataLoginResponse =
                            Gson().fromJson(result.data, LoginResponse::class.java)
                        if (dataLoginResponse.customer !=null) {
                            MainApplication().setPreference(context, "dataLogin", result!!.data!!)
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
        }

        binding.btnRegistrasi.setOnClickListener {
            findNavController().navigate(R.id.fragmentRegister)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun encryptThisString(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}
