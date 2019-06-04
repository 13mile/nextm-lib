package kr.nextm.lib.type.time

import java.util.*

class GmtTime(date: Date) : LocalTime(date, gmtTimeZone) {

    constructor() : this(Calendar.getInstance().time)
    constructor(input: String) : this(parseWithTimeZone(input, gmtTimeZone))

    companion object {
        val gmtTimeZone: TimeZone = TimeZone.getTimeZone("GMT")
    }

}
