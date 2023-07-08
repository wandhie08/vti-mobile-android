package com.rowantech.vti.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.BrandsItem
import com.rowantech.vti.databinding.ListBrandBinding
import com.rowantech.vti.utilities.Utils
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterBrand (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListener: (BrandsItem) -> Unit,
    private val callback: ((BrandsItem, ImageView) -> Unit)?
) : DataBoundListAdapter<BrandsItem, ListBrandBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<BrandsItem>() {
        override fun areItemsTheSame(
            oldItem: BrandsItem,
            newItem: BrandsItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BrandsItem,
            newItem: BrandsItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListBrandBinding {
        val binding = DataBindingUtil
            .inflate<ListBrandBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_brand,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListBrandBinding, item: BrandsItem) {
        binding.layoutBrand.setOnClickListener { clickListener(item) }
        //if (item.avatar!!.contains(".svg")){
            //Utils.fetchSvg(context, item.avatar, binding.iconBrand)
        item.avatar?.let { binding.iconBrand.loadUrl(it) }
//        }else{
//            c(context).load(item.avatar).into(binding.iconBrand)
//
//        }
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