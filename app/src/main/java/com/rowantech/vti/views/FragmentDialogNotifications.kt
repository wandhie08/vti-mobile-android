package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.GetByCustomerRequest
import com.rowantech.vti.data.model.request.GetEventByBrandRequest
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.DialogNotificationBinding
import com.rowantech.vti.databinding.FragmentBrandsBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import com.rowantech.vti.views.adapter.ListAdapterEvent
import java.util.ArrayList
import javax.inject.Inject

class FragmentDialogNotifications : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<DialogNotificationBinding>()

    internal var adapter: ViewPagerAdapter? = null
    internal lateinit var fragmentNotificationsUpdate: FragmentNotifications
    internal lateinit var fragmentNotificationsTransaksi: FragmentNotifications
    internal lateinit var fragmentNotificationsEvent: FragmentNotifications

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    var getByCustomerRequest = GetByCustomerRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, onBackPressedCallback
        )
    }
    internal lateinit var data: LoginResponse

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogNotificationBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)
        println("data.customer!!.customerId :"+data.customer!!.customerId)
        getByCustomerRequest.customerId = data.customer!!.customerId

        adapter = ViewPagerAdapter(childFragmentManager)

        adapter!!.addFragment(seArgsForFragment(FragmentNotifications()), "Update")
        adapter!!.addFragment(seArgsForFragment(FragmentNotifications()), "Transaksi")
        adapter!!.addFragment(seArgsForFragment(FragmentNotifications()), "Event")

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.fragmentEditProfile)
        }

        binding.viewpager.setOffscreenPageLimit(2)
        binding.viewpager.setAdapter(adapter)
        binding.tabs.setupWithViewPager(binding.viewpager)
        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewpager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        getProfile(binding)
        fragmentNotificationsUpdate = adapter!!.getItem(0) as FragmentNotifications
        fragmentNotificationsTransaksi = adapter!!.getItem(1) as FragmentNotifications
        fragmentNotificationsEvent = adapter!!.getItem(2) as FragmentNotifications
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun getProfile(binding: DialogNotificationBinding) {

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
                    binding.name.text = response.customer!!.nameCustomer
                    binding.phoneNumber.text = response.customer!!.phoneNumber
                    binding.email.text = response.customer!!.email
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

    private fun seArgsForFragment(fragment: Fragment): Fragment {
        val bundle = Bundle()

        //bundle.putBoolean("isDraft", arguments!!.getBoolean("isDraft"))
        fragment.setArguments(bundle)
        return fragment
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        override fun getCount(): Int {
            return mFragmentList.size
        }

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]

        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }
}