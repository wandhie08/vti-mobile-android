package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.databinding.DialogNotificationBinding
import com.rowantech.vti.databinding.FragmentSearchEventBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import java.util.ArrayList
import javax.inject.Inject

class FragmentSearchEvent : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<DialogNotificationBinding>()

    internal var adapter: ViewPagerAdapter? = null
    internal lateinit var fragmentListEventOffline: FragmentListEventOffline
    internal lateinit var fragmentListEventOnline: FragmentListEventOnline
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchEventBinding.inflate(inflater, container, false)
        context ?: return binding.root

        adapter = ViewPagerAdapter(childFragmentManager)

        adapter!!.addFragment(seArgsForFragment(FragmentListEventOnline()), "Online")
        adapter!!.addFragment(seArgsForFragment(FragmentListEventOffline()), "Offline")
        println("arguments?.getBoolean(\"isUpcoming\") :"+arguments?.getBoolean("isUpcoming"))
        println("arguments?.getBoolean(\"register\") :"+arguments?.getBoolean("register"))
        println("arguments?.getBoolean(\"closed\") :"+arguments?.getBoolean("closed"))
        println("arguments?.getBoolean(\"ongoing\") :"+arguments?.getBoolean("ongoing"))


        binding.viewpager.setOffscreenPageLimit(2)
        binding.viewpager.setAdapter(adapter)
        binding.tabs.setupWithViewPager(binding.viewpager)
        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewpager.setCurrentItem(tab.position)
                binding.tabs.getTabAt(tab.position)?.getCustomView()?.setSelected(true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        fragmentListEventOnline = adapter!!.getItem(0) as FragmentListEventOnline
        fragmentListEventOffline = adapter!!.getItem(1) as FragmentListEventOffline

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun seArgsForFragment(fragment: Fragment): Fragment {
        val bundle = Bundle()

        arguments?.getBoolean("isUpcoming")?.let { bundle.putBoolean("isUpcoming", it) }
        arguments?.getBoolean("register")?.let { bundle.putBoolean("register", it) }
        arguments?.getBoolean("closed")?.let { bundle.putBoolean("closed", it) }
        arguments?.getBoolean("ongoing")?.let { bundle.putBoolean("ongoing", it) }
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