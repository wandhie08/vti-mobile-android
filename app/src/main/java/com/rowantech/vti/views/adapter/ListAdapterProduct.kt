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
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.BannersItem
import com.rowantech.vti.data.model.response.ProductsItem
import com.rowantech.vti.databinding.ListAddProductBinding
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterProduct (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListener: (ProductsItem) -> Unit,
    val clickListenerAddItem: (ProductsItem) -> Unit,
    val clickListenerRemoveItem: (ProductsItem) -> Unit,
    private val callback: ((BannersItem, ImageView) -> Unit)?

) : DataBoundListAdapter<ProductsItem, ListAddProductBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ProductsItem>() {
        override fun areItemsTheSame(
            oldItem: ProductsItem,
            newItem: ProductsItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductsItem,
            newItem: ProductsItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListAddProductBinding {
        val binding = DataBindingUtil
            .inflate<ListAddProductBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_add_product,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListAddProductBinding, item: ProductsItem) {
        binding.nameProduct.text = item.title
        binding.formStock.text = item.stock.toString()
        binding.priceProduk.text = NumberUtil.moneyFormat(item.price.toString())
        binding.btnAdd.setOnClickListener { clickListenerAddItem(item) }
        binding.btnMin.setOnClickListener { clickListenerRemoveItem(item) }
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