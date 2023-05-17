package cutejason.classes

// Base object type
sealed class CuteJasonVal {
    //Method to serialize the CuteJson objects (aka json)
    abstract fun generateJson(): String
}

//Object that represents the json struct
data class CuteJasonObj(val value: Map<String, CuteJasonVal>) : CuteJasonVal() {
    override fun generateJson(): String {
        return value.map{ "\"${it.key}\" : ${it.value.generateJson()}" }.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ","
        )
    }
}
//String type object for json struct
data class CuteJasonStr(val value: String) : CuteJasonVal() {
    override fun generateJson(): String {
        return "\"$value\""
    }
}
//Numeric type object for json struct
data class CuteJasonNum(val value: Double) : CuteJasonVal() {
    override fun generateJson(): String {
        return "$value"
    }
}
//Boolean type object for json struct
data class  CuteJasonBool(val value: Boolean) : CuteJasonVal() {
    override fun generateJson(): String {
        return "$value"
    }
}
//List type object for json struct
data class CuteJasonList(val value: List<CuteJasonVal>) : CuteJasonVal() {
    override fun generateJson(): String {
        return value.joinToString(
            prefix = "[",
            postfix = "]"
        ) {
            it.generateJson()
        }
    }
}
//Null type object fot json struct
object CuteJasonNull : CuteJasonVal() {
    override fun generateJson(): String {
        return "null"
    }
}

