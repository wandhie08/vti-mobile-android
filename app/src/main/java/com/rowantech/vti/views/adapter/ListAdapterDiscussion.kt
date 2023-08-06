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
import com.rowantech.vti.data.model.response.DiscussionsItem
import com.rowantech.vti.databinding.ListDiscussionBinding
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterDiscussion (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    val clickListenerAddItem: (DiscussionsItem) -> Unit,

) : DataBoundListAdapter<DiscussionsItem, ListDiscussionBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<DiscussionsItem>() {
        override fun areItemsTheSame(
            oldItem: DiscussionsItem,
            newItem: DiscussionsItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DiscussionsItem,
            newItem: DiscussionsItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ListDiscussionBinding {
        val binding = DataBindingUtil
            .inflate<ListDiscussionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_discussion,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListDiscussionBinding, item: DiscussionsItem) {
        binding.name.text = item.nameCustomer
        binding.comment.text = item.title

        if (item.commentDiscuss!!.size>0){
            binding.commentUtama.visibility = View.VISIBLE
            binding.nameComment.text = item.commentDiscuss[0]!!.nameCustomer
            binding.commentDiscussion.text =  item.commentDiscuss[0]!!.message
        }
        if (item.commentDiscuss!!.size>1){
            binding.jawabanLainya.visibility = View.VISIBLE

            binding.jawabanLainya.text = (item.commentDiscuss!!.size-1).toString()+" Jawaban Lainnya"
        }
        //binding.btnAdd.setOnClickListener { clickListenerAddItem(item) }
        //item.photo?.let { binding.iconBrand.loadUrl(it) }


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