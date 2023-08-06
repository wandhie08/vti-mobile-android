package com.rowantech.vti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetValidationResponse(

	@field:SerializedName("extensions")
	val extensions: List<String?>? = null,

	@field:SerializedName("number_of_files")
	val numberOfFiles: Int? = null,

	@field:SerializedName("file_extension")
	val fileExtension: String? = null
)
