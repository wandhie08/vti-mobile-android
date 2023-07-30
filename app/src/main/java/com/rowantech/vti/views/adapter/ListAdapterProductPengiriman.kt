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
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.request.ItemOrder
import com.rowantech.vti.data.model.response.BannersItem
import com.rowantech.vti.databinding.ListProductPaymentBinding
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterProductPengiriman (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListener: (ItemOrder) -> Unit,
    val clickListenerAddItem: (ItemOrder) -> Unit,
    val clickListenerRemoveItem: (ItemOrder) -> Unit,
    private val callback: ((BannersItem, ImageView) -> Unit)?

) : DataBoundListAdapter<ItemOrder, ListProductPaymentBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ItemOrder>() {
        override fun areItemsTheSame(
            oldItem: ItemOrder,
            newItem: ItemOrder
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ItemOrder,
            newItem: ItemOrder
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListProductPaymentBinding {
        val binding = DataBindingUtil
            .inflate<ListProductPaymentBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_product_payment,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListProductPaymentBinding, item: ItemOrder) {
        binding.nameProduct.text = item.name
        binding.priceProduk.text = NumberUtil.moneyFormat(item.totalValue.toString())
        binding.heightProduk.text = item.qty.toString() +" Barang"
        item.photo?.let { binding.iconBrand.loadUrl(it) }


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