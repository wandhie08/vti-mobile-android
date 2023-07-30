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
import com.rowantech.vti.data.model.response.DataCourier
import com.rowantech.vti.databinding.ListJasaPengirimanBinding
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterJasaPengiriman (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListener: (DataCourier) -> Unit,
    val clickListenerAddItem: (DataCourier) -> Unit,
    val clickListenerRemoveItem: (DataCourier) -> Unit,
    private val callback: ((BannersItem, ImageView) -> Unit)?

) : DataBoundListAdapter<DataCourier, ListJasaPengirimanBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<DataCourier>() {
        override fun areItemsTheSame(
            oldItem: DataCourier,
            newItem: DataCourier
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DataCourier,
            newItem: DataCourier
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListJasaPengirimanBinding {
        val binding = DataBindingUtil
            .inflate<ListJasaPengirimanBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_jasa_pengiriman,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListJasaPengirimanBinding, item: DataCourier) {

        binding.btnCourier.setOnClickListener { clickListenerAddItem(item) }
        if (item.courierCode == "jnt") {
            binding.iconBrand.setBackgroundResource(R.drawable.kurir_jnt)

        } else if (item.courierCode == "ninja") {
            binding.iconBrand.setBackgroundResource(R.drawable.kurir_ninja)
        } else if (item.courierCode == "anteraja") {
            binding.iconBrand.setBackgroundResource(R.drawable.kurir_anteraja)
        } else if (item.courierCode == "gosend") {
            binding.iconBrand.setBackgroundResource(R.drawable.kurir_gosend)
        } else if (item.courierCode == "sicepat") {
            binding.iconBrand.setBackgroundResource(R.drawable.kurir_sicepat)
        } else if (item.courierCode == "jne") {
            binding.iconBrand.setBackgroundResource(R.drawable.kurir_jne)

        }
        binding.estimation.text = item.estimation
        binding.price.text = NumberUtil.moneyFormat(item.price.toString())
        binding.nameProduct.text = item.service
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