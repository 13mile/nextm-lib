package kr.nextm.lib

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.Html
import android.text.Spanned
import android.util.TypedValue

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

fun Int.pixelsToDp(): Float {
    val density = app.resources.displayMetrics.density

    val value: Float = when (density) {
        1.0f -> this / 4.0f
        1.05f -> this / (1.5 * (8 / 3))
        2.0f -> this / 4.0f
        else -> this / density
    } as Float

    return value
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

fun CharSequence.toHtml(replaceNewLineToBrForPlainText: Boolean = true): Spanned {
    val text = if (replaceNewLineToBrForPlainText)
        replaceNewLineToBrForPlainText()
    else
        this.toString()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    else
        Html.fromHtml(text)
}

private fun CharSequence.replaceNewLineToBrForPlainText(): String {
    return if (!contains("<br") && !contains("<p"))
        toString().replace("\n", "<br>")
    else
        toString()
}

fun CharSequence.containsIgnoreCase(other: CharSequence): Boolean {
    return contains(other, ignoreCase = true)
}
