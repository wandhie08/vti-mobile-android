package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.request.RegisterRequest
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.databinding.FragmentRegisterBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FragmentRegister : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentRegisterBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    var registerRequest = RegisterRequest()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        context ?: return binding.root
        val sdfTime = SimpleDateFormat("HH:mm:ss")
        val currentTime = sdfTime.format(Date())

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.fragmentLogin)
        }

        binding.btnRegistrasi.setOnClickListener {
            if (TextUtils.isEmpty(binding.formName.getText().toString())) {
                Snackbar.make(binding.root, "Nama tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formHandphone.getText().toString())) {
                Snackbar.make(binding.root, "No Handphone tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formUsername.getText().toString())) {
                Snackbar.make(binding.root, "Email tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formPassword.getText().toString())) {
                Snackbar.make(binding.root, "Password tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else if (TextUtils.isEmpty(binding.formConfirmPassword.getText().toString())) {
                Snackbar.make(
                    binding.root,
                    "Konfirmasi Password tidak boleh kosong",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (binding.formPassword.getText()
                    .toString() != binding.formConfirmPassword.getText().toString()
            ) {
                Snackbar.make(
                    binding.root,
                    "Password dan konfirmasi password tidak sama",
                    Snackbar.LENGTH_LONG
                )
                    .show()

            } else {


                progressShow()
                registerRequest.email = binding.formUsername.text.toString()
                registerRequest.username = binding.formUsername.text.toString()
                registerRequest.nameCustomer = binding.formName.text.toString()
                registerRequest.phoneNumber = binding.formHandphone.text.toString()
                registerRequest.password = encryptThisString(binding.formPassword.text.toString())
                registerRequest.postalCode = ""
                registerRequest.identityType = ""
                registerRequest.district = ""
                registerRequest.province = ""
                registerRequest.nik = ""
                registerRequest.avatar = ""
                registerRequest.city = ""
                registerRequest.gender = ""
                registerRequest.address = ""

                mainViewModel.paramWithBody(
                    "",
                    Constant.REGISTER,
                    Gson().toJson(registerRequest)
                )
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                    progressDismis()

                    if (result.status == Status.SUCCESS) {


                        Snackbar.make(
                            binding!!.root,
                            "Anda berhasil registrasi, silahkan login",
                            Snackbar.LENGTH_LONG
                        ).show()

                        findNavController().navigate(R.id.fragmentLogin)

                    } else {
                        if (!TextUtils.isEmpty(result.data)) {
                            Snackbar.make(
                                binding!!.root,
                                "Incorrect email or password or level, Please check again before login!",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                })
            }
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