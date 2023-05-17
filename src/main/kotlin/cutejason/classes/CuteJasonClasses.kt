package cutejason.classes

import Visitor

// Base object type
sealed class CuteJasonVal {

    //Method to serialize the CuteJson objects (aka json)
    abstract fun generateJson(): String

    abstract fun accept(visitor: Visitor)
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

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//String type object for json struct
data class CuteJasonStr(val value: String) : CuteJasonVal() {
    override fun generateJson(): String {
        return "\"$value\""
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//Numeric type object for json struct
data class CuteJasonNum(val value: Double) : CuteJasonVal() {
    override fun generateJson(): String {
        return "$value"
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//Boolean type object for json struct
data class  CuteJasonBool(val value: Boolean) : CuteJasonVal() {
    override fun generateJson(): String {
        return "$value"
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
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

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//Null type object fot json struct
object CuteJasonNull : CuteJasonVal() {
    override fun generateJson(): String {
        return "null"
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}

