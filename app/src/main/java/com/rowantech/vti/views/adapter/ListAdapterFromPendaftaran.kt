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
import com.rowantech.vti.data.model.response.BannersItem
import com.rowantech.vti.data.model.response.TemplatesItem
import com.rowantech.vti.databinding.ListFormPendaftaranBinding
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterFromPendaftaran (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    private val callback: ((BannersItem, ImageView) -> Unit)?

) : DataBoundListAdapter<TemplatesItem, ListFormPendaftaranBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<TemplatesItem>() {
        override fun areItemsTheSame(
            oldItem: TemplatesItem,
            newItem: TemplatesItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TemplatesItem,
            newItem: TemplatesItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListFormPendaftaranBinding {
        val binding = DataBindingUtil
            .inflate<ListFormPendaftaranBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_form_pendaftaran,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListFormPendaftaranBinding, item: TemplatesItem) {
        binding.textNamaForm.text = item.question

        //binding.textDetailNamaForm.text = NumberUtil.moneyFormat(item.price.toString())
        if (item.required =="N"){
            binding.textItemWajib.visibility =View.VISIBLE
        }else  if (item.optional =="Y"){
            binding.textItemWajib.visibility =View.GONE
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