package kr.nextm.lib

import android.app.Application
import java.lang.ref.WeakReference

object AppInstance {

    var instance: WeakReference<Application>? = null

    fun get(): Application {
        if (instance == null || instance!!.get() == null) {
            instance = WeakReference(getApplication())
        }
        return instance!!.get()!!
    }

    fun getApplicationContext() = get().applicationContext

    private fun getApplication(): Application {
        val className = Class.forName("android.app.ActivityThread")
        val method = className.getMethod("currentApplication")
        val result = method.invoke(null, *arrayOfNulls<Any>(0))

        return result as Application
    }

}