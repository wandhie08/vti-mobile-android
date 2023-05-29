package com.rowantech.vti.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


internal fun View.visible() {
    this.visibility = View.VISIBLE
}

internal fun View.gone() {
    this.visibility = View.GONE
}

internal fun View.invisible() {
    this.visibility = View.INVISIBLE
}

internal fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

internal fun BigDecimal.toCurr(): String {
    val otherSymbols = DecimalFormatSymbols(Locale.ENGLISH)
//    otherSymbols.decimalSeparator = '.'
//    otherSymbols.groupingSeparator = ','
//    val formatter = DecimalFormat("#,##0.00;-#,##0.00", otherSymbols)
    val formatter = DecimalFormat("#,###.##")

    return formatter.format(this)
}

internal fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

internal fun Date.toFormattedString(dateFormat: String = Constant.DATE_FORMAT): String {
    return SimpleDateFormat(dateFormat, Locale.getDefault()).format(this)
}

//internal fun MutableList<DataProduct>.subtotalProduk(): BigDecimal{
//    var result = BigDecimal.ZERO
//    this.forEach {
//        var dat = it.produk_harga.toBigDecimal().multiply(BigDecimal(it.count))
//        result = result.add(dat)
//    }
//    return result
//}
//
//internal fun MutableList<DataProduct>.totalPayment(disk: BigDecimal, tax: BigDecimal): BigDecimal{
//    var result = BigDecimal.ZERO
//    this.forEach {
//        var dat = it.produk_harga.toBigDecimal().multiply(BigDecimal(it.count))
//        result = result.add(dat)
//    }
//    return result.minus(disk).minus(tax)
//}
//
//internal fun MutableList<DataProduct>.totalItem(): BigDecimal{
//    var result = BigDecimal.ZERO
//    this.forEach {
//        result = result.add(it.count.toBigDecimal())
//    }
//    return result
//}

internal fun String.discountToPercent(amount: BigDecimal): BigDecimal {
    return if(this.isNotEmpty())(BigDecimal(this).divide(amount,2,  RoundingMode.HALF_EVEN)).multiply(BigDecimal(100)) else BigDecimal.ZERO
}

internal fun String.discountToAmount(total: BigDecimal): BigDecimal{
    return if(this.isNotEmpty())(BigDecimal(this).divide(BigDecimal(100))).multiply(total) else BigDecimal.ZERO
}

internal fun String.taxToAmount(total: BigDecimal): BigDecimal{
    return if(this.isNotEmpty())(BigDecimal(this).divide(BigDecimal(100))).multiply(total) else BigDecimal.ZERO
}
