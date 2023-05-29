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
import com.bumptech.glide.Glide
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.BrandsItem
import com.rowantech.vti.databinding.ListBrandBinding
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
        Glide.with(context).load(item.avatar).into(binding.iconBrand)
    }

}