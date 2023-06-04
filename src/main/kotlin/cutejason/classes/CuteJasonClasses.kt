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
    //Builds Json representation of CuteJasonVal and tis values and encapsulates all in bracers
    override fun generateJson(): String {
        return value.map{ "\"${it.key}\" : ${it.value.generateJson()}" }.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ","
        )
    }

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)){
            value.values.forEach{it.accept(visitor)}
        }
    }
}
//String type object for json struct
data class CuteJasonStr(val value: String) : CuteJasonVal() {
    //Returns value in quotation marks and as a string
    override fun generateJson(): String {
        return "\"$value\""
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//Numeric type object for json struct
data class CuteJasonNum(val value: Number) : CuteJasonVal() {
    //Returns value as a string
    override fun generateJson(): String {
        return "$value"
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//Boolean type object for json struct
data class  CuteJasonBool(val value: Boolean) : CuteJasonVal() {
    //Returns value as a string
    override fun generateJson(): String {
        return "$value"
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}
//List type object for json struct
data class CuteJasonList(val value: List<CuteJasonVal>) : CuteJasonVal() {
    //Builds a a string by calling each nested child generateJason method, then encapsulates all in square brackets
    override fun generateJson(): String {
        return value.joinToString(
            prefix = "[",
            postfix = "]"
        ) {
            it.generateJson()
        }
    }

    //Guides vositor obj to each nested child
    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            value.forEach{it.accept(visitor)}
        }
    }
}
//Null type object fot json struct
object CuteJasonNull : CuteJasonVal() {
    //returns a string "null" to represent null values
    override fun generateJson(): String {
        return "null"
    }

    override fun accept(visitor: Visitor) {
        return visitor.visit(this)
    }
}

