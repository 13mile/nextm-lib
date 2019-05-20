package kr.nextm.lib.exceptions

import kr.nextm.lib.TLog

open class ErrorLogException(val title: String, message: Any, cause: Throwable? = null) :
        RuntimeException(message.toString(), cause) {

    constructor(message: Any, cause: Throwable? = null) :
            this("ERROR", message, cause)

    constructor(throwable: Throwable) :
            this(throwable.message ?: throwable.javaClass.simpleName, throwable.cause)

    init {
        if (TLog.enabled) {
            printStackTrace()
            TLog.e("ExceptionWithLog", "[$title] $message")
        }
    }

}