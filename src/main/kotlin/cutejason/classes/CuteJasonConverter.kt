package cutejason.classes

import kotlin.reflect.full.declaredMemberProperties



object CuteJasonConverter {


    fun Any?.toCuteJason(): CuteJasonVal = when (this){

            null -> CuteJasonNull
            is Map<*,*> -> CuteJasonObj(this.mapKeys { it.key.toString() }.mapValues { it.value.toCuteJason() }.toMutableMap())
            is Collection<*> -> CuteJasonList( this.map { it.toCuteJason() }.toMutableList() )
            is Enum<*> -> CuteJasonStr(this.name)
            is Boolean -> CuteJasonBool(this)
            is Number -> CuteJasonNum(this)
            is String -> {
                val convertedNumber = this.toDoubleOrNull()
                if (convertedNumber != null) {
                    CuteJasonNum(convertedNumber)
                } else {
                    CuteJasonStr(this)
                }
            }
            else -> this.toCuteJasonObj()

    }

    private fun Any.toCuteJasonObj() : CuteJasonObj {

        val propertyMap = mutableMapOf<String, CuteJasonVal>()
        this::class.declaredMemberProperties
            .forEach { property ->
                val value = property.call(this)
                val key = property.name
                propertyMap[key] = value?.toCuteJason() ?: CuteJasonNull
            }
        return CuteJasonObj(propertyMap)
    }



}