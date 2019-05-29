package kr.nextm.lib.extension

import kr.nextm.lib.AppInstance

fun CharSequence?.toSafeInt(defaultValue: Int = 0): Int {
    return try {
        this!!.filter { char ->
            char.isDigit() ||
                    char == '-' ||
                    char == '.'
        }.toString().toInt()
    } catch (t: Throwable) {
        defaultValue
    }
}

fun CharSequence?.toSafeDouble(defaultValue: Double = 0.0): Double {
    return try {
        this!!.filter { char ->
            char.isDigit() ||
                    char == '-' ||
                    char == '.'
        }.toString().toDouble()
    } catch (t: Throwable) {
        defaultValue
    }
}

fun Int.getFormatString(vararg formatArgs: Any): String {
    return AppInstance.get().getString(this, *formatArgs)
}