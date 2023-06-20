package cutejason.classes

import Visitor
import cutejason.observer.Observable
import cutejason.observer.Observer

// Base object type
sealed class CuteJasonVal : Observable {

    //lost of observers
    private val observers: MutableList<Observer> = mutableListOf()

    //Method to serialize the CuteJson objects (aka json)
    abstract fun generateJson(): String

    abstract fun accept(visitor: Visitor)

    //Adds new observer to the list observers
    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }
    //Removes observer from observers list
    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }
    //Updates every observer in observers list
    override fun updateObservers() {
        observers.forEach { it.update(this) }
    }

}

//Object that represents the json struct
data class CuteJasonObj(val value: MutableMap<String, CuteJasonVal>) : CuteJasonVal(), MutableMap<String, CuteJasonVal> by value {


    //Builds Json representation of CuteJasonVal and this values and encapsulates all in bracers
    override fun generateJson(): String {
        return value.map{ "\"${it.key}\" : ${it.value.generateJson()}" }.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ","
        )
    }

    //Accepts visitor obj
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

    override fun toString(): String {
        return value
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

    override fun toString(): String {
        return value.toString()
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

    override fun toString(): String {
        return value.toString()
    }
}
//List type object for json struct
data class CuteJasonList(val value: MutableList<CuteJasonVal>) : CuteJasonVal() {
    //Builds a string by calling each nested child generateJason method, then encapsulates all in square brackets
    override fun generateJson(): String {
        return value.joinToString(
            prefix = "[",
            postfix = "]"
        ) {
            it.generateJson()
        }
    }

    //Guides visitor obj to each nested child
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

    override fun toString(): String {
        return "null"
    }
}

