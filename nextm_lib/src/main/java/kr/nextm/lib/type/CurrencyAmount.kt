package kr.nextm.lib.type

import kr.nextm.lib.extension.requireEquals
import kr.nextm.lib.extension.toSafeDouble
import java.io.Serializable
import java.text.DecimalFormat
import java.util.*

open class CurrencyAmount(doubleAmount: Double,
                          val currencyCode: String = getDefaultCurrency()
) : Serializable {


    val amount: Double

    val currency: Currency
//        @Exclude
        get() = Currency.getInstance(currencyCode)

    init {
        amount = extractDoubleByFractionDigits(doubleAmount, currency)
    }

    constructor() : this(0)

    constructor(amount: Double) :
            this(amount, getDefaultCurrency())

    constructor(doubleAmount: Double, currency: Currency) : this(doubleAmount, currency.currencyCode)

    constructor(intAmount: Int) :
            this(intAmount.toDouble())

    constructor(intAmount: Int, currencyCode: String) :
            this(intAmount.toDouble(), currencyCode)

    constructor(longAmount: Long) :
            this(longAmount.toDouble())

    constructor(longAmount: Long, currencyCode: String) :
            this(longAmount.toDouble(), currencyCode)

    constructor(stringAmount: CharSequence) :
            this(stringToDouble(stringAmount))

    constructor(stringAmount: CharSequence, currencyCode: String) :
            this(stringToDouble(stringAmount), currencyCode)

    override fun toString(): String {
        val formatString = "%.${this.currency.defaultFractionDigits}f"
        return String.format(formatString, amount)
    }

    fun toFormatted(): String {
        return toFormated(this.amount, currency)
    }

    fun toCurrencyFormatted(): String {
        val formatted = toFormatted()
        return formatted + getPostFix()
    }

//    @Exclude
    fun getPostFix(): String {
        return when (currency.currencyCode) {
//            "KRW" -> " ${AppInstance.get().getString(R.string.currency_display_name_krw)}"
//            "SGD" -> " ${AppInstance.get().getString(R.string.currency_display_name_sgd)}"
            else -> " ${currency.displayName}"
        }
    }

    fun toCurrencySignFormatted(isPay: Boolean): String {
        val sign = if (isPay || isZero()) "" else "-"
        return sign + toCurrencyFormatted()
    }

    companion object {
        var defaultCurrencyCode = "KRW"

        fun getDefaultCurrency() = Currency.getInstance(defaultCurrencyCode)!!.currencyCode!!

        fun extractDoubleByFractionDigits(amount: Double, currency: Currency): Double {
            return toFormated(amount, currency).toSafeDouble()
        }

        fun stringToDouble(s: CharSequence): Double {
            val allowedCharacters = ('0'..'9').plus(listOf('.', '-', '+'))

            return try {
                s.filter { it in allowedCharacters }
                        .toString().toDouble()
            } catch (e: Exception) {
                0.0
            }
        }

        fun getDefaultMaxAmount() =
                when (defaultCurrencyCode) {
                    "KRW" -> CurrencyAmount(1_000_000_000.0)
                    "SGD" -> CurrencyAmount(100_000_000.0)
                    else -> CurrencyAmount(100_000_000.0)
                }

        private fun toFormated(amount: Double, currency: Currency): String {
            var pattern = "#,##0"

            val fractionDigits = currency.defaultFractionDigits
            if (fractionDigits > 0) {
                pattern = "$pattern."
                (0 until fractionDigits)
                        .forEach {
                            pattern = "${pattern}0"
                        }
            }

            val df = DecimalFormat(pattern)

            return df.format(amount)
        }
    }

    operator fun plus(intAmount: Int): CurrencyAmount = plus(CurrencyAmount(intAmount.toDouble(), currency))

    operator fun plus(other: CurrencyAmount): CurrencyAmount {
        checkCurrencyCompatible(other)
        return CurrencyAmount(amount + other.amount, currency)
    }


    operator fun times(value: Int): CurrencyAmount = times(value.toDouble())
    operator fun times(value: Double): CurrencyAmount = CurrencyAmount(amount * value, this.currency.currencyCode)

    operator fun div(cnt: Int): CurrencyAmount = div(cnt.toDouble())
    operator fun div(value: Double): CurrencyAmount = CurrencyAmount(amount / value, this.currency.currencyCode)

    operator fun minus(value: Int): CurrencyAmount = minus(value.toDouble())
    operator fun minus(value: Long): CurrencyAmount = minus(value.toDouble())
    operator fun minus(value: Double): CurrencyAmount = minus(CurrencyAmount(value, currency))

    operator fun minus(other: CurrencyAmount): CurrencyAmount {
        checkCurrencyCompatible(other)

        return CurrencyAmount(amount - other.amount, currency)
    }

    operator fun compareTo(other: CurrencyAmount): Int {
        checkCurrencyCompatible(other)

        return ((this.amount - other.amount) * 100).toInt()
    }

    private fun checkCurrencyCompatible(other: CurrencyAmount) {
        requireEquals(this.currency, other.currency) { "같은 통화끼리만 연산이 가능함." }
    }

    operator fun compareTo(other: Double) = compareTo(CurrencyAmount(other, currency))
    operator fun compareTo(other: Int) = compareTo(other.toDouble())

    override fun equals(other: Any?): Boolean {
        if (other is CurrencyAmount) {
            if (this.currency != other.currency) {
                return false
            }

            return amount == other.amount
        }

        return amount == other
    }

    fun toDouble() = amount
//    @Exclude
    fun isZero(): Boolean = amount == 0.0

//    @Exclude
    fun isNotZero() = !isZero()

    fun toInt(): Int {
        requireEquals(CurrencyAmount(amount.toInt()), this) { "소숫점 아래 수치가 있음. amount: $amount" }
        return amount.toInt()
    }

    operator fun rem(other: Int) = amount % other
}