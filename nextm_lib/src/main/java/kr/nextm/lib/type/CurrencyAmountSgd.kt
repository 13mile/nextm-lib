package kr.nextm.api.model.type

import kr.nextm.lib.type.CurrencyAmount

class CurrencyAmountSgd(amount: Double) : CurrencyAmount(amount, currencyCode = "SGD") {
    constructor(amount: String) : this(stringToDouble(amount))
}
