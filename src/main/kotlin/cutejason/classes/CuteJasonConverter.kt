package cutejason.classes

import cutejason.classes.*
import java.util.SimpleTimeZone
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible


object CuteJasonConverter {


    fun Any?.toCuteJason(): CuteJasonVal = when (this){

            is Map<*,*> -> CuteJasonObj(this.mapKeys { it.key.toString() }.mapValues { it.value.toCuteJason() })
            is Collection<*> -> CuteJasonList( this.map { it.toCuteJason() } )
            is Enum<*> -> CuteJasonStr(this.name)
            is Boolean -> CuteJasonBool(this)
            is String -> CuteJasonStr(this)
            is Number -> CuteJasonNum(this.toDouble())
            null -> CuteJasonNull
            else -> this.toCuteJasonObj()

    }

    private fun Any.toCuteJasonObj() : CuteJasonObj {

        val map = mutableMapOf<String, CuteJasonVal>()
        this::class.declaredMemberProperties
            .forEach { property ->
                val value = property.call(this)
                val key = property.name
                map[key] = value?.toCuteJason() ?: CuteJasonNull
            }
        return CuteJasonObj(map)
    }



}