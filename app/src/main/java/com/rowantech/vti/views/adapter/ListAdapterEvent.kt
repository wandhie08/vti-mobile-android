package com.rowantech.vti.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.databinding.ListEventBinding
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterEvent (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListener: (EventsItem) -> Unit,
    private val callback: ((EventsItem, ImageView) -> Unit)?
) : DataBoundListAdapter<EventsItem, ListEventBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<EventsItem>() {
        override fun areItemsTheSame(
            oldItem: EventsItem,
            newItem: EventsItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: EventsItem,
            newItem: EventsItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListEventBinding {
        val binding = DataBindingUtil
            .inflate<ListEventBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_event,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListEventBinding, item: EventsItem) {
        binding.layoutEvent.setOnClickListener { clickListener(item) }
        binding.titleEvent.text = item.title
        Glide.with(context).load(item.avatar).into(binding.brand)

        if (item.status == "CLOSED") {
            binding.textStatus.text = "pengumuman pemenang"
        } else if (item.status == "ONGOING") {
            binding.textStatus.text = "pengumpulan data"

        } else if (item.status == "REGISTRATION") {
            binding.textStatus.text = "pendaftaran"

        } else if (item.status == "UPCOMING") {
            binding.textStatus.text = "dijadwalkan"

        }
        if (item.banners!!.size>0){
            Glide.with(context).load(item.banners?.get(0)?.banner).into(binding.banner)
            System.out.println("item.avatar :"+item.avatar)
            System.out.println("item.banners?.get(0)?.banner :"+item.banners?.get(0)?.banner)
        }

    }

}