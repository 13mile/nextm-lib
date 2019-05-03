package kr.nextm.lib

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

object QToast {
    val list = mutableListOf<Any>()

    fun addDebug(message: Any?, time: Long = 1, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        if (TLog.enabled) {
            add(message, time/*, timeUnit*/)
        }
    }

    fun add(message: Any?, time: Long = 1/*, timeUnit: TimeUnit = TimeUnit.SECONDS*/) {

        if (message == null) {
            add("null", time/*, timeUnit*/)
            return
        }

        list.add(message)
        showToast()

        Timer().schedule(time) {
            val index = list.indexOfFirst { it === message }
            list.removeAt(index)

            showToast()
        }

//        Observable.timer(time, timeUnit)
//                .subscribe {
//                    val index = list.indexOfFirst { it === message }
//                    list.removeAt(index)
//
//                    showToast()
//                }



    }

    private fun showToast() {

        val statement = statement().joinToString("\n")

        if (statement.isEmpty()) {
            TToast.clearLastOne()
            return
        }

        TLog.d("QToast::showToast", statement)
        TToast.show(statement)
    }

    fun statement(): List<String> {
        return ArrayList(list.map { it.toString() })
    }

    fun reset() {
        list.clear()
    }
}