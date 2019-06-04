package kr.nextm.api.model.type.time

import kr.nextm.lib.type.time.LocalTime
import java.util.*

class SingaporeTime(date: Date) : LocalTime(date, singaporeTimeZone) {

    constructor() : this(Calendar.getInstance().time)
    constructor(timeInMillis: Long) : this(Date(timeInMillis))
    constructor(input: String) : this(LocalTime.parseWithTimeZone(input, singaporeTimeZone))

    companion object {
        val singaporeTimeZone: TimeZone = TimeZone.getTimeZone("Asia/Singapore")
    }
}
