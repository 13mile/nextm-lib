package kr.nextm.api.model.type

import kr.nextm.lib.type.CurrencyAmount

class CurrencyAmountKrw(amount: Double) : CurrencyAmount(amount, currencyCode = "KRW") {
    constructor(amount: String) : this(stringToDouble(amount))
}
