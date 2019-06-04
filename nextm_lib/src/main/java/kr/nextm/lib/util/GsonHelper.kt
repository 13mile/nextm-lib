package kr.nextm.lib.util

import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.nextm.api.model.type.CurrencyAmountKrw
import kr.nextm.api.model.type.CurrencyAmountSgd
import kr.nextm.api.model.type.time.SingaporeTime
import kr.nextm.lib.TLog
import kr.nextm.lib.exceptions.ErrorLogException
import kr.nextm.lib.type.*
import kr.nextm.lib.type.time.GmtTime
import kr.nextm.lib.type.time.KstTime
import kr.nextm.lib.type.time.LocalTime
import java.lang.reflect.Type

object GsonHelper {
    private val gsonInstance: Gson by lazy {
        builder().create()
    }

    fun gson(): Gson {
        return gsonInstance
    }

    val pretty: Gson by lazy {
        builder().setPrettyPrinting()
            .create()
    }

    private fun builder(): GsonBuilder {
        return GsonBuilder().apply {
            setLenient()
            registerTypeAdapter(JsonElementAs::class.java, stringAdapter())
            registerTypeAdapter(JsonElementAsObject::class.java, stringAdapter())
            registerTypeAdapter(JsonElementAsString::class.java, stringAdapter())

            if (Build.VERSION.SDK_INT >= 27) {
                registerNotSupportedType<android.icu.util.CurrencyAmount>()
            }

            registerTypeAdapter(LocalTime::class.java, timestampAdapter { LocalTime(it) })
            registerTypeAdapter(GmtTime::class.java, timestampAdapter { GmtTime(it) })
            registerTypeAdapter(KstTime::class.java, timestampAdapter { KstTime(it) })
            registerTypeAdapter(SingaporeTime::class.java, timestampAdapter { SingaporeTime(it) })

            registerTypeAdapter(
                CurrencyAmount::class.java,
                currencyAdapter<CurrencyAmount> { CurrencyAmount(it, Prefs.system.getCurrency()) })
            registerTypeAdapter(
                CurrencyAmountKrw::class.java,
                currencyAdapter<CurrencyAmountKrw> { CurrencyAmountKrw(it) })
            registerTypeAdapter(
                CurrencyAmountSgd::class.java,
                currencyAdapter<CurrencyAmountSgd> { CurrencyAmountSgd(it) })

            registerTypeAdapter(Int::class.java, numberConverter({ it.toInt() }))

            registerTypeAdapter(Double::class.java, numberConverter({ it.toDouble() }))

        }
    }

    private inline fun <reified T> GsonBuilder.registerNotSupportedType() {
        registerTypeAdapter(T::class.java, object : TypeAdapter<Any>() {
            override fun write(writer: JsonWriter, value: Any?) {
                throw ErrorLogException("GSON 파서가 지원하지 않는 타입 " + T::class.java.canonicalName)
            }

            override fun read(reader: JsonReader): Any {
                throw ErrorLogException("GSON 파서가 지원하지 않는 타입 " + T::class.java.canonicalName)
            }
        })
    }

    private fun <T> GsonBuilder.registerStringConvertable(typeOfClass: Class<T>, transform: (T) -> String, creator: (String) -> T) {
        registerTypeAdapter(typeOfClass, toStringConvertable(transform, creator))
    }

    private fun <T> numberConverter(creator: (String) -> T): TypeAdapter<T> = object : TypeAdapter<T>() {
        override fun write(writer: JsonWriter, value: T?) {
            value?.let {
                writer.jsonValue(it.toString())
            }
        }

        override fun read(reader: JsonReader): T {
            return try {
                creator(reader.nextString())
            } catch (t: Throwable) {
                creator("0")
            }
        }
    }

    private fun stringAdapter() = object : TypeAdapter<JsonElementAs>() {
        override fun write(writer: JsonWriter, value: JsonElementAs?) {
            value?.let {
                if (it.outputType == OutputType.ToObject) {
                    writer.jsonValue(it.toString())
                } else {
                    writer.value(it.toString())
                }
            }
        }

        override fun read(reader: JsonReader): JsonElementAs {
            throw ErrorLogException("구현 하지마시오")
        }
    }

    private fun <T> toStringConvertable(
        transform: (T) -> String,
        creator: (string: String) -> T
    ) = object : TypeAdapter<T>() {
        override fun write(writer: JsonWriter, value: T?) {
            value?.let {
                writer.value(transform(it))
            }
        }

        override fun read(reader: JsonReader): T {
            val string = reader.nextString()
            val value = creator(string)
            return value
        }
    }


    private fun <T : LocalTime> timestampAdapter(creator: (string: String) -> T) = object : TypeAdapter<T>() {
        override fun write(writer: JsonWriter, value: T?) {
            value?.let {
                writer.value(it.serialize())
            }
        }

        override fun read(reader: JsonReader): T {
            val string = reader.nextString()
            return creator(string)
        }
    }

    private fun <T : CurrencyAmount> currencyAdapter(creator: (String) -> CurrencyAmount) = object : TypeAdapter<T>() {
        override fun write(writer: JsonWriter, value: T?) {
            value?.let {
                val string = it.toString()

                writer.value(string)
            }
        }

        override fun read(reader: JsonReader): T {
            val string = reader.nextString()
            val amount = creator(string) as T
            return amount
        }
    }

    fun <T> toJson(any: T): String {
        return gson().toJson(any)
    }

    fun <T> fromJson(json: String?, clazz: Type, functionDefaultObjectCreator: (t: Throwable) -> T = { null!! }): T {
        return try {
            if (json.isNullOrEmpty()) {
                throw ErrorLogException("Cannot parse empty or null string for $clazz")
            }
            gson().fromJson(json, clazz)
        } catch (t: Throwable) {
            TLog.e(t)
            functionDefaultObjectCreator.invoke(t)
        }
    }

    inline fun <reified T> fromJson(json: String?, noinline functionDefaultObjectCreator: (t: Throwable) -> T = { t: Throwable -> throw t }): T {
        return fromJson(json, object : TypeToken<T>() {}.type, functionDefaultObjectCreator)
    }

    inline fun <T, reified R> fromObject(obj: T): R = fromJson(toJson(obj))


}