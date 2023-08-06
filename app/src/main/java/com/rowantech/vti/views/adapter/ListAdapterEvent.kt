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
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.EventsItem
import com.rowantech.vti.data.model.response.GetDateEventResponse
import com.rowantech.vti.data.model.response.GetTotalDateEventResponse
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
    var getDateEventResponse = GetDateEventResponse()
    var getTotalDateEventResponse = GetTotalDateEventResponse()

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListEventBinding, item: EventsItem) {
        binding.layoutEvent.setOnClickListener { clickListener(item) }
        binding.titleEvent.text = item.title
        getDateEventResponse =
            Gson().fromJson(item.freeData2, GetDateEventResponse::class.java)
        getTotalDateEventResponse =
            Gson().fromJson(item.freeData1, GetTotalDateEventResponse::class.java)
        //Glide.with(context).load(item.avatar).into(binding.brand)
        item.avatar?.let { binding.brand.loadUrl(it) }
        if (item.type == "CLOSED") {
            binding.valueStatus.text = "selesai"
            binding.iconStatus.setBackgroundResource(R.drawable.circle)
            binding.tanggalEvent.text = item.closingDate
        } else if (item.type == "ANNOUNCEMENT") {
            binding.valueStatus.text = "pengumuman pemenang "
            binding.iconStatus.setBackgroundResource(R.drawable.circle_ungu)
            binding.tanggalEvent.text = getDateEventResponse.winP
        } else if (item.type == "EVALUATION EVENT") {
            binding.valueStatus.text = "evaluasi"
            binding.iconStatus.setBackgroundResource(R.drawable.circle_yellow)
            binding.tanggalEvent.text = getDateEventResponse.val2P
        } else if (item.type == "ONGOING") {
            binding.valueStatus.text = "berlangsung"
            binding.iconStatus.setBackgroundResource(R.drawable.circle_blue)
            binding.tanggalEvent.text = getDateEventResponse.evtP
        } else if (item.type == "SUBMISSION") {
            binding.valueStatus.text = "pengumpulan data"
            binding.iconStatus.setBackgroundResource(R.drawable.circle_blue)
            binding.tanggalEvent.text = getDateEventResponse.subP
        } else if (item.type == "REGISTRATION") {
            binding.valueStatus.text = "pendaftaran"
            binding.iconStatus.setBackgroundResource(R.drawable.circle_green)
            binding.tanggalEvent.text = getDateEventResponse.regP
        } else if (item.type == "EVALUATION REGISTRATION") {
            binding.valueStatus.text = "pendaftaran ditutup "
            binding.iconStatus.setBackgroundResource(R.drawable.circle_green)
            binding.tanggalEvent.text = item.registrationDate
            binding.tanggalEvent.text = getDateEventResponse.regP
        } else if (item.type == "UPCOMING") {
            binding.valueStatus.text = "dijadwalkan"
            binding.iconStatus.setBackgroundResource(R.drawable.circle_green)
            binding.tanggalEvent.text = item.registrationDate
            binding.tanggalEvent.text = getDateEventResponse.regP
        }
        if (item.banners!!.size>0){
            //Glide.with(context).load(item.banners?.get(0)?.banner).into(binding.banner)
            item.banners?.get(0)?.banner?.let { binding.banner.loadUrl(it) }
            System.out.println("item.avatar :"+item.avatar)
            System.out.println("item.banners?.get(0)?.banner :"+item.banners?.get(0)?.banner)
        }

    }

    fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            //.placeholder(R.drawable.ic_copy)
            //.error(R.drawable.ic_copy)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }
}