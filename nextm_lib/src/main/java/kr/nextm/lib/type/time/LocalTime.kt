package kr.nextm.lib.type.time

import kr.nextm.api.model.type.time.Pattern
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


open class LocalTime(val date: Date,
                     private val timeZone: TimeZone = localTimeZone,
                     private var serializePattern: String = Pattern.serializeDefault
) : Serializable {

    constructor()
            : this(Calendar.getInstance().time, localTimeZone)

    constructor(timeMillis: Long)
            : this(Date(timeMillis), localTimeZone)

    constructor(input: String, timeZone: TimeZone = localTimeZone)
            : this(parseWithTimeZone(input, timeZone))


    val time: Long = date.time

    fun serialize() = formatString(serializePattern)

    fun formatString(pattern: String = Pattern.toStringDefault): String {
        return formatStringWithTimeZone(date, pattern, this.timeZone)
    }

    fun extractDate() = formatString("yyyy-MM-dd")
    fun extractTime() = formatString("HH:mm:ss")

    override fun toString() = formatString()

    operator fun minus(other: LocalTime) = LocalTime(Date(this.time - other.time), this.timeZone)

    override fun equals(other: Any?): Boolean {
        if (other is LocalTime) {
            val t1 = time / 1000
            val t2 = other.time / 1000
            return t1 == t2
        }
        return false
    }

    fun <T : LocalTime> setSerializePattern(pattern: String): T {
        this.serializePattern = pattern
        return this as T
    }

    companion object {
        var localTimeZone: TimeZone = KstTime.kstTimeZone

        fun formatStringWithTimeZone(
            date: Date,
            pattern: String = Pattern.toStringDefault,
            timeZone: TimeZone
        ): String {
            val sdf = SimpleDateFormat(pattern, Locale.US)
            sdf.timeZone = timeZone
            return sdf.format(date)
        }

        fun parseWithTimeZone(input: String, timeZone: TimeZone): Date {

            if (input.contains("T")) {
                val parts = input.split("T")
                val modifiedTs = parts[0] + " " + parts[1]
                return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(modifiedTs)
            }

            val replaced = input
                    .filter { it != '/' }
                    .filter { it != '-' }
                    .filter { it != ' ' }
                    .filter { it != ':' }

            if (replaced.isEmpty()) {
                return Date(0)
            }

            val sdf = when {
                replaced.length == 6 ->
                    SimpleDateFormat(Pattern.HHmmss, Locale.US)
                replaced.length == 8 ->
                    SimpleDateFormat(Pattern.yyyyMMdd, Locale.US)
                replaced.length == 12 ->
                    SimpleDateFormat(Pattern.yyMMddHHmmss, Locale.US)
                replaced.length == 13 ->
                    SimpleDateFormat(Pattern.yyMMddHHmmssF, Locale.US)
                else ->
                    SimpleDateFormat(Pattern.yyyyMMddHHmmss, Locale.US)
            }

            sdf.timeZone = timeZone

            return sdf.parse(replaced)
        }

    }

}
