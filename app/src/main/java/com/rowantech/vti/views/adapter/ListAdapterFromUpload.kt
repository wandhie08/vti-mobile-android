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
import com.google.gson.Gson
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.model.response.BannersItem
import com.rowantech.vti.data.model.response.GetParameterResponse
import com.rowantech.vti.data.model.response.GetValidationResponse
import com.rowantech.vti.data.model.response.TemplatesItem
import com.rowantech.vti.databinding.ListFormUploadBinding
import com.rowantech.vti.utilities.NumberUtil
import com.rowantech.vti.views.commons.DataBoundListAdapter

class ListAdapterFromUpload (
    private val dataBindingComponent: DataBindingComponent,
    private val context: Context,
    appExecutors: AppExecutors,
    private val callback: ((BannersItem, ImageView) -> Unit)?

) : DataBoundListAdapter<TemplatesItem, ListFormUploadBinding>(
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

    override fun createBinding(parent: ViewGroup): ListFormUploadBinding {
        val binding = DataBindingUtil
            .inflate<ListFormUploadBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_form_upload,
                parent,
                false,
                dataBindingComponent
            )
        return binding
    }
    internal lateinit var getParameterResponse: GetValidationResponse
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun bind(binding: ListFormUploadBinding, item: TemplatesItem) {
        binding.textNamaForm.text = item.question
        binding.textDetailNamaForm.text = item.description

        if (item.questionType=="File Upload"){
            if (item.validation!=""){
                getParameterResponse =
                    Gson().fromJson(item.validation, GetValidationResponse::class.java)
                binding.textJumlahFileValue.text = getParameterResponse.numberOfFiles.toString()
                binding.textBerkasFileValue.text = getParameterResponse.extensions!![0]
                binding.imageVideo.setBackgroundResource(R.drawable.baseline_video_library_24)
                if (getParameterResponse.extensions!![0] =="image/*"){
                    binding.imageVideo.setBackgroundResource(R.drawable.baseline_image_24)
                }else{
                    binding.imageVideo.setBackgroundResource(R.drawable.baseline_video_library_24)
                }
            }
        }else{
            binding.textJumlahFileValue.visibility =View.GONE
            binding.textBerkasFileValue.visibility =View.GONE
            binding.imageVideo.visibility =View.GONE
            binding.textJumlahFile.visibility =View.GONE
            binding.textBerkasFile.visibility =View.GONE

        }
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