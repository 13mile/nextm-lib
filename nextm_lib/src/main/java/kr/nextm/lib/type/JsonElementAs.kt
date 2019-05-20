package kr.nextm.lib.type

import kr.nextm.lib.util.GsonHelper
import java.io.Serializable

open class JsonElementAs(val obj: Any, val outputType: OutputType) : Serializable {
    fun <T> get(): T = obj as T

    override fun toString() = GsonHelper.toJson(obj)
}
