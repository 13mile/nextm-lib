package kr.nextm.lib

import android.util.Log

object TLog {

    var enabled = true
    private val DEFAULT_TAG:String = TLog::class.java.canonicalName
    var functionPostProcess: (s: String) -> Unit = {}

    @JvmStatic
    fun e(message: Any) {
        e(DEFAULT_TAG, message)
    }

    @JvmStatic
    fun w(message: Any) {
        w(DEFAULT_TAG, message)
    }

    @JvmStatic
    fun i(message: Any) {
        i(DEFAULT_TAG, message)
    }

    @JvmStatic
    fun d(message: Any) {
        d(DEFAULT_TAG, message)
    }

    @JvmStatic
    fun v(message: Any) {
        v(DEFAULT_TAG, message)
    }

    @JvmStatic
    fun e(e: Throwable) {
        e(DEFAULT_TAG, e)
    }

    @JvmStatic
    fun e(tag: String, message: Any?, e: Throwable) {
        if (enabled)
            Log.e(tag, convertToString(message), e)
        postProcessor(message)
    }

    @JvmStatic
    fun e(tag: String, message: Any?) {
        if (enabled)
            Log.e(tag, convertToString(message))
        postProcessor(message)
    }

    @JvmStatic
    fun w(tag: String, message: Any) {
        if (enabled)
            Log.w(tag, convertToString(message))
        postProcessor(message)
    }

    @JvmStatic
    fun i(tag: String, message: Any) {
        if (enabled)
            Log.i(tag, convertToString(message))
        postProcessor(message)
    }

    @JvmStatic
    fun d(tag: String, message: Any) {
        if (enabled)
            Log.d(tag, convertToString(message))
        postProcessor(message)
    }

    @JvmStatic
    fun v(tag: String, message: Any) {
        if (enabled)
            Log.v(tag, convertToString(message))
        postProcessor(message)
    }

    @JvmStatic
    fun e(tag: String, e: Throwable) {
        if (enabled)
            e(tag, e.message, e)
        postProcessor(e.message)
    }

    private fun convertToString(message: Any?): String {
        if (message == null) return "<null>"
        return message.toString()
    }

    private fun postProcessor(message: Any?) {
        functionPostProcess(convertToString(message))
    }
}
