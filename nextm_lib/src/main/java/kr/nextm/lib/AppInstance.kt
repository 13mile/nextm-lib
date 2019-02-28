package kr.nextm.lib

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

    fun restart(startingActivityClass: Class<*>) {
        reserveStartActivity(get(), startingActivityClass)
        System.exit(0)
    }

    private fun reserveStartActivity(context: Context, startingActivityClass: Class<*>) {
        val startingActivity = Intent(context, startingActivityClass)
        val mPendingIntentId = 123456
        val mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, startingActivity,
            PendingIntent.FLAG_CANCEL_CURRENT)
        val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
    }

    fun terminate(activity: Activity) {
        activity.finishAffinity()
        System.exit(0)
    }
}