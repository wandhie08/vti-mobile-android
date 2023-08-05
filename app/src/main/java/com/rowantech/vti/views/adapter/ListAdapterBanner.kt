package com.rowantech.vti.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
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
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.BannersItem
import com.rowantech.vti.databinding.ListBannerBinding
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterBanner (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListener: (BannersItem) -> Unit,
    private val callback: ((BannersItem, ImageView) -> Unit)?
) : DataBoundListAdapter<BannersItem, ListBannerBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<BannersItem>() {
        override fun areItemsTheSame(
            oldItem: BannersItem,
            newItem: BannersItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BannersItem,
            newItem: BannersItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListBannerBinding {
        val binding = DataBindingUtil
            .inflate<ListBannerBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_banner,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListBannerBinding, item: BannersItem) {

        Glide.with(context).load(item.banner).into(binding.iconBanner)
        item.banner?.let { binding.iconBanner.loadUrl(it) }

        binding.iconBanner.setOnClickListener { clickListener(item) }
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