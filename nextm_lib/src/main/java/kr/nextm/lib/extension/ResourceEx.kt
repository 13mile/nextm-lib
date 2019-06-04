package kr.nextm.lib.extension

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import kr.nextm.lib.AppInstance

private val app get() = AppInstance.get()

fun Int.getDimension(): Float {
    return (app.resources.getDimension(this))
}

fun Int.getString(): String {
    return app.getString(this)
}

fun Int.getString(vararg formatArgs: Any): String {
    return app.getString(this, *formatArgs)
}

fun Number.dpToPixels(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        app.resources.displayMetrics
    ).toInt()
}

fun <E> Collection<E>.getSample(index: Int): E {
    return this.elementAt(index % size)
}

fun <E> Array<E>.getSample(index: Int): E {
    return this[index % size]
}

fun Int.getDrawable(): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        app.resources.getDrawable(this, null)
    } else {
        AppInstance.get().resources.getDrawable(this)
    }
}

fun Int.getColor() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    app.getColor(this)
} else {
    ActivityCompat.getColor(app, this)
}

fun CharSequence.toHtml(): Spanned {
    val text = replaceNewLineToBrForPlainText()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    else
        Html.fromHtml(text)
}

private fun CharSequence.replaceNewLineToBrForPlainText(): String {
    return if (!contains("<br>"))
        toString().replace("\n", "<br>")
    else
        toString()
}

fun CharSequence.containsIgnoreCase(other: CharSequence): Boolean {
    return contains(other, ignoreCase = true)
}
