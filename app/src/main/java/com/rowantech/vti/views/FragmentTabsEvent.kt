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
import com.google.android.material.tabs.TabLayout
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.databinding.FragmentTabsEventBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.autoCleared
import java.util.ArrayList
import javax.inject.Inject

class FragmentTabsEvent : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentTabsEventBinding>()

    internal var adapter: ViewPagerAdapter? = null
    internal lateinit var fragmentListEventOffline: FragmentDetailEvent
    internal lateinit var fragmentListEventOnline: FragmentDetailEvent
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTabsEventBinding.inflate(inflater, container, false)
        context ?: return binding.root

        adapter = ViewPagerAdapter(childFragmentManager)

        adapter!!.addFragment(seArgsForFragment(FragmentDetailEvent()), "Description")
        adapter!!.addFragment(seArgsForFragment(FragmentDetailEvent()), "Offline")


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
        fragmentListEventOnline = adapter!!.getItem(0) as FragmentDetailEvent
        fragmentListEventOffline = adapter!!.getItem(1) as FragmentDetailEvent

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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