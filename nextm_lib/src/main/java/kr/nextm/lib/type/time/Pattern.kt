package kr.nextm.api.model.type.time

object Pattern {
    const val HHmmss = "HHmmss"
    const val yyMMdd = "yyMMdd"
    const val yyyyMMdd = "yyyyMMdd"
    const val yyMMddHHmmss = "yyMMddHHmmss"
    const val yyMMddHHmmssF = "yyMMddHHmmssF"
    const val yyyyMMddHHmmss = "yyyyMMddHHmmss"

    const val toStringDefault = "yyyy-MM-dd HH:mm:ss"
    const val serializeDefault = yyyyMMddHHmmss
    const val year = "yyyy"
    const val month = "MM"
    const val day = "dd"

}
