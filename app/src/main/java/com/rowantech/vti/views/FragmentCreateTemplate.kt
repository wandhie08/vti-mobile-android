package com.rowantech.vti.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.rowantech.vti.MainApplication
import com.rowantech.vti.R
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.model.request.CreateTemplateRequest
import com.rowantech.vti.data.model.request.GetEventTerkaitRequest
import com.rowantech.vti.data.model.request.TemplatesItem
import com.rowantech.vti.data.model.response.*
import com.rowantech.vti.databinding.FragmentCreateTemplateBinding
import com.rowantech.vti.di.Injectable
import com.rowantech.vti.utilities.Constant
import com.rowantech.vti.utilities.autoCleared
import com.rowantech.vti.viewmodels.MainViewModel
import javax.inject.Inject


class FragmentCreateTemplate : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<FragmentCreateTemplateBinding>()

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    internal lateinit var data: EventsItem
    var isForm: Boolean = true
    var isCheckbox: Boolean = false

    var getEventTerkaitRequest = GetEventTerkaitRequest()
    var createTemplateRequest = CreateTemplateRequest()

    var getEventTerkaitResponse = GetFormTemplateResponse()
    internal lateinit var dataLogin: LoginResponse
    val listOfTemplate: MutableList<TemplatesItem> = mutableListOf()
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateTemplateBinding.inflate(inflater, container, false)
        context ?: return binding.root
        data = Gson().fromJson(arguments?.getString("data"), EventsItem::class.java)
        getEventTerkaitRequest.eventId = data.eventId
        createTemplateRequest.eventId = data.eventId
        dataLogin = Gson().fromJson(MainApplication().getStringPref(context, "dataLogin"), LoginResponse::class.java)
        println("data.customer!!.customerId :"+dataLogin.customer!!.customerId)
        createTemplateRequest.customerId = dataLogin.customer!!.customerId

        binding.btnRegistrasi.setOnClickListener {
            isForm =true
            for (item in getEventTerkaitResponse.templates!!) {

                if (item!!.questionType=="Date"){
                    if (item.required=="Y"){

                    }
                }

                if (item!!.questionType=="Dropdown"){
                    if (item.required=="Y"){

                    }
                }

                if (item!!.questionType=="File Download"){
                    if (item.required=="Y"){

                    }
                }

                if (item!!.questionType=="File Upload"){
                    if (item.required=="Y"){

                    }
                }

                if (item!!.questionType=="Long Answer"){
                    val editText = binding.root.findViewWithTag<EditText>(item.templateId)
                    if (item.required=="Y"){
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            isForm =false
                            Snackbar.make(binding.root, item.question.toString()+" tidak boleh kosong", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                if (item!!.questionType=="Multiple Choice"){

                    val gson = GsonBuilder().create()
                    val options = gson.fromJson<ArrayList<String>>(item!!.freeData1, object :
                        TypeToken<ArrayList<String>>(){}.type)

                    for (itemOps in options.indices) {
                        val checkBox = binding.root.findViewWithTag<CheckBox>(item.templateId+"itemOptions"+itemOps)
                        if (checkBox.isChecked ){
                            isCheckbox=true
                            println(options[itemOps]+":"+checkBox.text.toString())
                        }
                    }
                    if (item.required=="Y"){
                        if (!isCheckbox){
                            isForm =false
                            Snackbar.make(binding.root, item.question.toString()+" tidak boleh kosong", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                if (item!!.questionType=="Short Answer"){
                    val editText = binding.root.findViewWithTag<EditText>(item.templateId)
                    if (item.required=="Y"){
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            isForm =false
                            Snackbar.make(binding.root, item.question.toString()+" tidak boleh kosong", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }


                if (item!!.questionType=="Single Choice"){
                    val radioGroup = binding.root.findViewWithTag<RadioGroup>(item.templateId)
                    val selectedId: Int = radioGroup.getCheckedRadioButtonId()
                    if (item.required=="Y") {

                        if (selectedId == -1) {
                            Snackbar.make(binding.root, "Silahkan pilih salah satu", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                if (item!!.questionType=="Time"){

                }

            }
            //isForm=false
            if (isForm){
                for (item in getEventTerkaitResponse.templates!!) {
                    val templatesItem = TemplatesItem()
                    templatesItem.templateId = item!!.templateId
                    if (item!!.questionType=="Date"){

                    }

                    if (item!!.questionType=="Dropdown"){

                    }

                    if (item!!.questionType=="File Download"){

                    }

                    if (item!!.questionType=="File Upload"){

                    }

                    if (item!!.questionType=="Long Answer"){
                        val editText = binding.root.findViewWithTag<EditText>(item.templateId)
                        templatesItem.answer = editText.getText().toString()
                    }

                    if (item!!.questionType=="Multiple Choice"){
                        val gson = GsonBuilder().create()
                        val options = gson.fromJson<ArrayList<String>>(item!!.freeData1, object :
                            TypeToken<ArrayList<String>>(){}.type)
                        templatesItem.answer=""
                        for (itemOps in options.indices) {
                            val checkBox = binding.root.findViewWithTag<CheckBox>(item.templateId+"itemOptions"+itemOps)
                            if (checkBox.isChecked ){
                                isCheckbox=true
                                if (itemOps > 0){
                                    templatesItem.answer=templatesItem.answer+","+checkBox.text.toString()
                                }else{
                                    templatesItem.answer=checkBox.text.toString()
                                }


                            }
                        }
                        println("templatesItem.answer:"+templatesItem.answer)
                    }

                    if (item!!.questionType=="Short Answer"){
                        val editText = binding.root.findViewWithTag<EditText>(item.templateId)
                        templatesItem.answer = editText.getText().toString()
                    }


                    if (item!!.questionType=="Single Choice"){
                        val radioGroup = binding.root.findViewWithTag<RadioGroup>(item.templateId)
                        val selectedId: Int = radioGroup.getCheckedRadioButtonId()

                            if (selectedId == -1) {
                                templatesItem.answer = ""
                            }else{
                                val radioButton = binding.root.findViewById(selectedId) as RadioButton
                                templatesItem.answer = radioButton.getText().toString()

                            }

                    }

                    if (item!!.questionType=="Time"){

                    }
                    println("templatesItem :"+templatesItem.answer)
                    listOfTemplate.add(templatesItem)

                }

                println("templatesItem :"+listOfTemplate.size)

                if (listOfTemplate.size>0) {
                    progressShow()

                    createTemplateRequest.templates = listOfTemplate
                    mainViewModel.paramWithBody(
                        "",
                        Constant.CREATE_TEMPLATE,
                        Gson().toJson(createTemplateRequest)
                    )
                    mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->
                        progressDismis()

                        if (result.status == Status.SUCCESS) {


                            if (data.eventType == "paid") {
                                val bundle = Bundle()
                                bundle.putString("data", Gson().toJson(data))
                                findNavController().navigate(R.id.fragmentProductPayment, bundle)
                            } else if (data.eventType == "free") {
                                val bundle = Bundle()
                                bundle.putString("data", Gson().toJson(data))
                                findNavController().navigate(R.id.fragmentHome, bundle)

                            }


                        } else {
                            if (!TextUtils.isEmpty(result.data)) {
                                val response =
                                    Gson().fromJson(result.data, MessageResponse::class.java)
                                response.error?.let {
                                    Snackbar.make(
                                        binding!!.root,
                                        it,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    })
                }
            }
        }
        getFormTemplate(binding)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getFormTemplate(binding: FragmentCreateTemplateBinding) {



        getEventTerkaitRequest.page = 0
        getEventTerkaitRequest.size = 100
        mainViewModel.paramWithBody(
            "",
            Constant.FORM_TEMPLATE,
            Gson().toJson(getEventTerkaitRequest)
        )
        mainViewModel.data!!.observe(viewLifecycleOwner, Observer { result ->

            if (result.status == Status.SUCCESS) {
                getEventTerkaitResponse =
                    Gson().fromJson(result.data, GetFormTemplateResponse::class.java)

                if (getEventTerkaitResponse.templates!!.size > 0) {
                    for (item in getEventTerkaitResponse.templates!!) {

                        if (item!!.questionType=="Date"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val editText = Button(binding.root.context)
                            editText.setText("Pilih Tanggal")
                            editText.setBackgroundResource(R.drawable.button_blue)
                            editText.setPadding(30, 10, 30, 10)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            editText.tag = item!!.templateId
                            editText.setTextAppearance(R.style.btn_style)
                            editText.layoutParams = params
                            val param = editText.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            editText.layoutParams = param
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(editText)
                        }

                        if (item!!.questionType=="Dropdown"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val spinner = Spinner(binding.root.context)
                            spinner.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                220
                            )
                            val param = spinner.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            spinner.layoutParams = param
                            spinner.setPadding(30, 10, 30, 10)
                            val gson = GsonBuilder().create()
                            val theList = gson.fromJson<ArrayList<String>>(item!!.freeData1, object :
                                TypeToken<ArrayList<String>>(){}.type)
                            spinner.setBackgroundResource(R.drawable.bg_form)
                            val arrayAdapter = ArrayAdapter(binding.root.context,android.R.layout.simple_spinner_dropdown_item,theList)

                            spinner.adapter = arrayAdapter

//                            spinner.onItemSelectedListener =
//                                object : AdapterView.OnItemSelectedListener {
//                                    override fun onItemSelected(
//                                        parent: AdapterView<*>?,
//                                        view: View,
//                                        position: Int,
//                                        id: Long
//                                    ) {
//
//                                    }
//
//                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
//                                }

                                binding.linearLayout.addView(textView)
                                binding.linearLayout.addView(spinner)


                        }

                        if (item!!.questionType=="File Download"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val editText = Button(binding.root.context)
                            editText.setText("Download")
                            editText.setBackgroundResource(R.drawable.button_blue)
                            editText.setPadding(30, 10, 30, 10)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            editText.tag = item!!.templateId
                            editText.setTextAppearance(R.style.btn_style)
                            editText.layoutParams = params
                            val param = editText.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            editText.layoutParams = param
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(editText)
                        }

                        if (item!!.questionType=="File Upload"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val editText = Button(binding.root.context)
                            editText.setText("Upload")
                            editText.setBackgroundResource(R.drawable.button_blue)
                            editText.setPadding(30, 10, 30, 10)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            editText.tag = item!!.templateId
                            editText.setTextAppearance(R.style.btn_style)
                            editText.layoutParams = params
                            val param = editText.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            editText.layoutParams = param
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(editText)
                        }

                        if (item!!.questionType=="Long Answer"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val editText = EditText(binding.root.context)
                            editText.setHint(item!!.description)
                            editText.setPadding(30, 30, 30, 30)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                               800
                            )
                            editText.gravity =Gravity.TOP
                            editText.setBackgroundResource(R.drawable.bg_form)
                            editText.layoutParams = params
                            editText.inputType =InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            val param = editText.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            editText.layoutParams = param
                            editText.setSingleLine(false);
                            editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION)
                            editText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                            editText.setLines(5)
                            editText.setMaxLines(10)
                            editText.setVerticalScrollBarEnabled(true)
                            editText.setMovementMethod(ScrollingMovementMethod.getInstance())
                            editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(editText)
                        }

                        if (item!!.questionType=="Multiple Choice"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val spinner = RadioButton(binding.root.context)
                            spinner.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                220
                            )


                            val gson = GsonBuilder().create()
                            val options = gson.fromJson<ArrayList<String>>(item!!.freeData1, object :
                                TypeToken<ArrayList<String>>(){}.type)
                            // create a radio group
                            binding.linearLayout.addView(textView)
                            for (itemOps in options.indices) {
                            //for (itemOptions in options){
                                val rg = CheckBox(binding.root.context)
                                rg.setText(options[itemOps])
                                rg.setPadding(30, 30, 30, 30)
                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                rg.tag = item.templateId+"itemOptions"+itemOps
                                rg.layoutParams = params
                                val param = rg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(40,40,40,10)
                                binding.linearLayout.addView(rg)
                            }



                        }

                        if (item!!.questionType=="Short Answer"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val editText = EditText(binding.root.context)
                            editText.setHint(item!!.description)
                            editText.setPadding(30, 10, 30, 10)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            editText.tag = item!!.templateId
                            editText.setBackgroundResource(R.drawable.bg_form)
                            editText.layoutParams = params
                            val param = editText.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            editText.layoutParams = param
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(editText)

                        }


                        if (item!!.questionType=="Single Choice"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val spinner = RadioButton(binding.root.context)
                            spinner.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                220
                            )


                            val gson = GsonBuilder().create()
                            val options = gson.fromJson<ArrayList<String>>(item!!.freeData1, object :
                                TypeToken<ArrayList<String>>(){}.type)
                            // create a radio group
                            val rg = RadioGroup(binding.root.context)
                            rg.setBackgroundResource(R.drawable.bg_form)
                            rg.orientation = RadioGroup.VERTICAL
                            rg.setPadding(30, 30, 30, 30)
                            rg.tag = item!!.templateId
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            rg.layoutParams = params
                            val param = rg.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            for (i in options.indices) {
                                val rb = RadioButton(binding.root.context)
                                rb.text = options[i]
                                rb.id = View.generateViewId()
                                rg.addView(rb)
                            }
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(rg)
                        }

                        if (item!!.questionType=="Time"){
                            val textView = TextView(binding.root.context)
                            textView.layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                120
                            )
                            val paramtextView = textView.layoutParams as ViewGroup.MarginLayoutParams
                            paramtextView.setMargins(40,40,40,10)
                            textView.layoutParams = paramtextView
                            textView.setPadding(30, 10, 30, 10)
                            textView.setText(item!!.question)
                            textView.setTextAppearance(binding.root.context, R.style.text_style_medium_blue);

                            val editText = Button(binding.root.context)
                            editText.setText("Pilih Waktu")
                            editText.setBackgroundResource(R.drawable.button_blue)
                            editText.setPadding(30, 10, 30, 10)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            editText.tag = item!!.templateId
                            editText.setTextAppearance(R.style.btn_style)
                            editText.layoutParams = params
                            val param = editText.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(40,40,40,10)
                            editText.layoutParams = param
                            binding.linearLayout.addView(textView)
                            binding.linearLayout.addView(editText)
                        }

                    }
                }
            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun convertPixelsToDp(px: Int, context: Context): Int {
        val resources: Resources = context.getResources()
        val metrics: DisplayMetrics = resources.getDisplayMetrics()
        return (px / (metrics.densityDpi / 160f)).toInt()
    }
}