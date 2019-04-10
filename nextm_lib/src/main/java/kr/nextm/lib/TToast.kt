package kr.nextm.lib

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import java.lang.ref.WeakReference


/**
 * @param context The context to use.  Usually your [Application]
 * or [Activity] object.
 */

class TToast : Toast(context) {

    init {
        if (useCustomView()) {
            initCustomView()
        }
    }

    private fun initCustomView() {
        val view = (context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(idCustomView!!, null)

        setGravity(Gravity.FILL_HORIZONTAL.or(Gravity.BOTTOM), 0, 100.dpToPixels())

        val params = WindowManager.LayoutParams()
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        view.layoutParams = params

        setView(view)
    }

    override fun setText(s: CharSequence) {
        if (useCustomView()) {
            val textView = view.findViewById(idMessageTextView!!) as TextView
            textView.text = s
        } else {
            super.setText(s)
        }
    }

    companion object {

        // custom view 사용시, 활성화 시키기
        @LayoutRes
        private var idCustomView: Int? = null//R.layout.view_toast
        @IdRes
        private var idMessageTextView: Int? = null//R.id.text_message

        private fun useCustomView() = idCustomView != null && idMessageTextView != null

        private var lastToast: WeakReference<Toast>? = null


        /**
         * Make a custom toast that just contains a text view.
         *
         * @param context  The context to use.  Usually your [android.app.Application]
         * or [android.app.Activity] object.
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        private fun makeText(context: Context, text: CharSequence, duration: Int): Toast {
            clearLastOne()

            val toast = createToast(context, text, duration)
            lastToast = WeakReference(toast)
            return toast
        }

        private fun createToast(context: Context, text: CharSequence, duration: Int): Toast {
            if (useCustomView()) {
                val toast = TToast()
                toast.setText(text)
                toast.duration = duration

                return toast
            }
            val toast = Toast.makeText(context, text, duration)
            val group = toast.view as ViewGroup
            val messageTextView = group.getChildAt(0) as TextView
            messageTextView.textSize = 25f
            return toast
        }

        private val context: Context
            get() {
                return AppInstance.get()
            }

        @JvmStatic
        fun show(o: Any) {

            if (o is Throwable) {
                show(context, o.message
                        ?: "Null message", LENGTH_LONG)
            } else {
                show(context, o.toString(), LENGTH_SHORT)
            }
        }

        @JvmStatic
        fun show(text: CharSequence) {
            show(context, text, LENGTH_SHORT)
        }

        @JvmStatic
        fun show(@StringRes stringId: Int) {
            show(context.getString(stringId))
        }


        @JvmStatic
        fun showWithFlag(active: Boolean, text: CharSequence) {
            if (active) {
                show(text)
            }
        }

        /**
         * Make a custom toast that just contains a text view.
         *
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        @JvmStatic
        fun show(text: CharSequence, duration: Int) {
            show(context, text, duration)
        }

        @JvmStatic
        fun show(@StringRes stringId: Int, duration: Int) {
            show(context.getString(stringId), duration)
        }


        /**
         * Make a custom toast that just contains a text view.
         *
         * @param context  The context to use.  Usually your [android.app.Application]
         * or [android.app.Activity] object.
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or
         * [.LENGTH_LONG]
         */
        @JvmStatic
        fun show(context: Context, text: CharSequence, duration: Int) {
            Handler(Looper.getMainLooper())
                .post{
                    makeText(context, text, duration).show()
                    TLog.d("#TOAST", text.toString())
                }
        }

        /**
         * Make a custom toast that just contains a text view.
         *
         * @param text The text to show.  Can be formatted text.
         */
        @JvmStatic
        fun showLong(text: CharSequence) {
            show(context, text, LENGTH_LONG)
        }

        @JvmStatic
        fun showLong(@StringRes stringId: Int) {
            showLong(context.getString(stringId))
        }

        @JvmStatic
        fun clearLastOne() {
            if (lastToast != null && lastToast!!.get() != null) {
                lastToast!!.get()!!.cancel()
            }

            lastToast = null
        }


        @JvmStatic
        fun showDebug(debugMessage: CharSequence) {
            if (TLog.enabled) {
                show("DEBUG:$debugMessage")
            }
        }

        @JvmStatic
        fun showDebug(t: Throwable) {
            if (TLog.enabled) {
                t.printStackTrace()
                TLog.e(t)
                show("DEBUG: Throwable: ${t.message}")
            }
        }
    }

}
