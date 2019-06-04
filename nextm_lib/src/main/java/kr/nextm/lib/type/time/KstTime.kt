package kr.nextm.lib.type.time

import java.util.*

class KstTime(date: Date) : LocalTime(date, kstTimeZone) {

    constructor() : this(Calendar.getInstance().time)
    constructor(timeInMillis: Long) : this(Date(timeInMillis))
    constructor(input: String) : this(parseWithTimeZone(input, kstTimeZone))

    companion object {
        val kstTimeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
    }
}
