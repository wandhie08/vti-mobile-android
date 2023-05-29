/**
 *
 */
package com.rowantech.vti.utilities

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * @author Anggoro Biandono
 * 6:05:38 PM
 */
object NumberUtil {
    fun getMaskingCardNumber(cardNumber: String): String {
        var result = ""
        if (cardNumber.length >= 9) {
            result = cardNumber.substring(cardNumber.length - 4, cardNumber.length)
            result = "*****$result"
            result = cardNumber.substring(0, cardNumber.length - 9) + result
        } else {
            return cardNumber
        }
        return result
    }
    fun moneyFormat(str: String): String {
        var convertedString = ""
        try {


            var length = 0
            for (i in 0 until str.length) {
                if (str[i] == '.')
                    break
                length++
            }

            if (length > 0) {
                var temp = ""
                var i = length
                while (i > 0) {
                    if (i > 2) {
                        if (temp.equals("", ignoreCase = true))
                            temp = str.substring(i - 3, i)
                        else
                            temp = str.substring(i - 3, i) + "." + temp
                    } else {
                        if (temp.equals("", ignoreCase = true))
                            temp = str.substring(0, i)
                        else
                            temp = str.substring(0, i) + "." + temp
                        i = 0
                    }
                    i = i - 3
                }
                var decimal = if (str.length == length)
                    "00"
                else
                    str
                        .substring(length + 1)
                decimal = if (decimal.length == 1) decimal + "0" else decimal
                convertedString = "Rp. "+"$temp"

            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        return convertedString
    }

    fun kata(number: Int): String {
        var number = number
        number = Math.abs(number)

        val angka = arrayOf(
            "",
            "satu",
            "dua",
            "tiga",
            "empat",
            "lima",
            "enam",
            "tujuh",
            "delapan",
            "sembilan",
            "sepuluh",
            "sebelas"
        )
        var temp = ""

        if (number < 12) {
            temp = " " + angka[number]
        } else if (number < 20) {
            temp = kata(number - 10) + " belas"
        } else if (number < 100) {
            temp = kata(number / 10) + " puluh" + kata(number % 10)
        } else if (number < 200) {
            temp = " seratus" + kata(number - 100)
        } else if (number < 1000) {
            temp = kata(number / 100) + " ratus" + kata(number % 100)
        } else if (number < 2000) {
            temp = " seribu" + kata(number - 1000)
        } else if (number < 1000000) {
            temp = kata(number / 1000) + " ribu" + kata(number % 1000)
        } else if (number < 1000000000) {
            temp = kata(number / 1000000) + " juta" + kata(number % 1000000)
        }

        return temp
    }

    fun terbilang(number: Int): String {
        val terbilangTeks = kata(number).trim { it <= ' ' }
        return terbilangTeks.substring(0, 1).toUpperCase() + terbilangTeks.substring(1) + " rupiah."
    }

    fun toCurr(number: BigDecimal): String {
        val formatter = DecimalFormat("#,##0.00;-#,##0.00")

        return formatter.format(number)
    }


    fun toCurrWithoutDecimal(number: BigDecimal): String {
        val formatter = DecimalFormat("#,##0;-#,##0")

        return formatter.format(number)
    }

    fun dpToInteger(padding_in_dp: Int, scale: Float): Int {

        return (padding_in_dp * scale + 0.5f).toInt()
    }

    fun toCurrDigitGrouping(number: String): String {
        val dec = BigDecimal(number)
        val formatter = DecimalFormat("#,###.##")
        return "Rp " + formatter.format(dec)
    }

    fun formatRupiah(number: Double): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number)
    }

    fun String.toCurrencyFormat(): String {
        val localeID = Locale("in", "ID")
        val doubleValue = this.toDoubleOrNull() ?: return this
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.minimumFractionDigits = 0
        return numberFormat.format(doubleValue)
    }
}
