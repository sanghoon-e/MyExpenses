package org.totschnig.myexpenses.util

import android.content.Context
import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.res.ResourcesCompat
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.model.CurrencyUnit
import java.util.*

object TextUtils {
    @JvmStatic
    fun <E : Enum<E>> joinEnum(enumClass: Class<E>): String =
        EnumSet.allOf(enumClass).joinToString(",") { "'${it.name}'" }

    @JvmStatic
    fun concatResStrings(ctx: Context, separator: String, vararg resIds: Int): String =
        resIds.joinToString(separator) { ctx.getString(it) }

    @JvmStatic
    fun appendCurrencySymbol(context: Context, resId: Int, currency: CurrencyUnit): String {
        return appendText(context, resId, currency.symbol)
    }

    fun appendCurrencyDescription(context: Context, resId: Int, currency: CurrencyUnit): String {
        return appendText(context, resId, currency.description)
    }

    private fun appendText(context: Context, resId: Int, symbol: String?): String {
        return String.format(Locale.ROOT, "%s (%s)", context.getString(resId), symbol)
    }

    fun formatQifCategory(mainLabel: String?, subLabel: String?): String? {
        val mainLabelSanitized = sanitizeQifCategory(mainLabel)
        val subLabelSanitized = sanitizeQifCategory(subLabel)
        return if (subLabelSanitized.isNullOrEmpty()) mainLabelSanitized else String.format(
            "%s:%s",
            mainLabelSanitized,
            subLabelSanitized
        )
    }

    private fun sanitizeQifCategory(label: String?): String? {
        val substitute = '|'
        return label?.replace('/', substitute)?.replace(':', substitute)
    }

    fun String.withAmountColor(resources: Resources, isIncome: Boolean) =
        SpannableString(this).apply {
            setSpan(
                ForegroundColorSpan(
                    ResourcesCompat.getColor(
                        resources,
                        if (isIncome) R.color.colorIncome else R.color.colorExpense,
                        null
                    )
                ), 0, length, 0
            )
        }
}