package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
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
import com.rowantech.vti.data.model.request.*
import com.rowantech.vti.data.model.response.GetByCustomerResponse
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.data.model.response.MessageResponse
import com.rowantech.vti.databinding.DialogNotificationBinding
import com.rowantech.vti.databinding.FragmentEditProfileBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class FragmentEditProfile : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors
    internal lateinit var data: LoginResponse


    var updateAccountRequest = UpdateAccountRequest()

    var updatePasswordRequest = UpdatePasswordRequest()

    var updateAddressRequest = UpdateAddressRequest()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    var getByCustomerRequest = GetByCustomerRequest()

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)
        println("data.customer!!.customerId :"+data.customer!!.customerId)
        getByCustomerRequest.customerId = data.customer!!.customerId
        getProfile(binding)

        binding.btnSimpan.setOnClickListener {
            if (TextUtils.isEmpty(binding.formName.getText().toString())) {
                Snackbar.make(binding.root, "Nama tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            }else if (TextUtils.isEmpty(binding.formPhone.getText().toString())) {
                Snackbar.make(binding.root, "No Handphone tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            }else if (TextUtils.isEmpty(binding.formEmail.getText().toString())) {
                Snackbar.make(binding.root, "Email tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else {


                progressShow()
                updateAccountRequest.customerId = data.customer!!.customerId
                updateAccountRequest.email = binding.formEmail.text.toString()
                updateAccountRequest.nameCustomer = binding.formName.text.toString()
                updateAccountRequest.phoneNumber = binding.formPhone.text.toString()

                mainViewModel.paramWithBody(
                    "",
                    Constant.UPDATE_ACCOUNT,
                    Gson().toJson(updateAccountRequest)
                )
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                    progressDismis()

                    if (result.status == Status.SUCCESS) {
                        Snackbar.make(
                            binding!!.root,
                            "Data berhasil diubah",
                            Snackbar.LENGTH_LONG
                        ).show()

                        findNavController().navigate(R.id.fragmentHome)
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

        binding.btnSimpanPassword.setOnClickListener {
            if (TextUtils.isEmpty(binding.formPasswordOld.getText().toString())) {
                Snackbar.make(binding.root, "Password lama tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formPasswordNew.getText().toString())) {
                Snackbar.make(binding.root, "Password baru tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else if (TextUtils.isEmpty(binding.formPasswordUlangi.getText().toString())) {
                Snackbar.make(binding.root, "Ulangi password baru tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (binding.formPasswordNew.getText().toString()!=binding.formPasswordUlangi.getText().toString()) {
                Snackbar.make(binding.root, "Password baru dan ulangi password baru tidak sama", Snackbar.LENGTH_LONG)
                    .show()
            } else {


                progressShow()
                updatePasswordRequest.customerId = data.customer!!.customerId
                updatePasswordRequest.oldPassword = encryptThisString(binding.formPasswordOld.getText().toString())
                updatePasswordRequest.newPassword = encryptThisString(binding.formPasswordNew.getText().toString())

                mainViewModel.paramWithBody(
                    "",
                    Constant.UPDATE_PASSWORD,
                    Gson().toJson(updatePasswordRequest)
                )
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                    progressDismis()

                    if (result.status == Status.SUCCESS) {
                        Snackbar.make(
                            binding!!.root,
                            "Password berhasil diubah",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.fragmentHome)

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

        binding.btnSimpanAddress.setOnClickListener {
            if (TextUtils.isEmpty(binding.formNamaPenerima.getText().toString())) {
                Snackbar.make(binding.root, "Nama penerima tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formNoHandphonePenerima.getText().toString())) {
                Snackbar.make(binding.root, "No handphone penerima  tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formKodePos.getText().toString())) {
                Snackbar.make(binding.root, "Kode POS tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else if (TextUtils.isEmpty(binding.formProvinsi.getText().toString())) {
                Snackbar.make(binding.root, "Provinsi tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formCity.getText().toString())) {
                Snackbar.make(binding.root, "Kota/Kabupaten tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formKec.getText().toString())) {
                Snackbar.make(binding.root, "Kecamatan/Keluarahan tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.formAddress.getText().toString())) {
                Snackbar.make(binding.root, "Alamat tidak boleh kosong", Snackbar.LENGTH_LONG)
                    .show()

            } else {


                progressShow()
                updateAddressRequest.customerId = data.customer!!.customerId
                updateAddressRequest.name = binding.formNamaPenerima.getText().toString()
                updateAddressRequest.phoneNumber = binding.formNoHandphonePenerima.getText().toString()
                updateAddressRequest.address = binding.formAddress.getText().toString()
                updateAddressRequest.province = binding.formProvinsi.getText().toString()
                updateAddressRequest.district = binding.formKec.getText().toString()
                updateAddressRequest.postalCode = binding.formKodePos.getText().toString()
                updateAddressRequest.city = binding.formCity.getText().toString()

                mainViewModel.paramWithBody(
                    "",
                    Constant.UPDATE_ADDRESS,
                    Gson().toJson(updateAddressRequest)
                )
                mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                    progressDismis()

                    if (result.status == Status.SUCCESS) {
                        Snackbar.make(
                            binding!!.root,
                            "Data berhasil diubah",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.fragmentHome)

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

    fun getProfile(binding: FragmentEditProfileBinding) {

        println("getByCustomerRequest :"+getByCustomerRequest.customerId)
        mainViewModel.paramWithBody(
            "",
            Constant.CUSTOMER_BYID,
            Gson().toJson(getByCustomerRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

            if (result.status == Status.SUCCESS) {
                val response =
                    Gson().fromJson(result.data, GetByCustomerResponse::class.java)

                if (response.customer !=null){
                    binding.formName.setText(response.customer.nameCustomer)
                    binding.formPhone.setText(response.customer.phoneNumber)
                    binding.formEmail.setText(response.customer.email)
                }

                if (response.customerAddress !=null){
                    binding.formNamaPenerima.setText(response.customerAddress.name)
                    binding.formNoHandphonePenerima.setText(response.customerAddress.phoneNumber)
                    binding.formCity.setText(response.customerAddress.city)
                    binding.formProvinsi.setText(response.customerAddress.province)
                    binding.formKodePos.setText(response.customerAddress.postalCode)
                    binding.formKec.setText(response.customerAddress.district)
                    binding.formAddress.setText(response.customerAddress.address)

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