package cutejason.controller

import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal
import cutejason.command.*


class Controller(private val invoker: Invoker, private val cuteJasonObj: CuteJasonObj) {

    fun addProperty(parentProperty: CuteJasonVal, propertyName: String) {
        val addCommand = AddCommand(cuteJasonObj, parentProperty, propertyName)
        invoker.useCommand(addCommand)
    }

    fun alterProperty(property: CuteJasonVal, newPropertyValue: Any) {
        val alterCommand = AlterCommand(cuteJasonObj, property, newPropertyValue)
        invoker.useCommand(alterCommand)
    }

    fun removeProperty(property: CuteJasonVal) {
        val removeCommand = RemoveCommand(cuteJasonObj, property)
        invoker.useCommand(removeCommand)
    }

    fun undo() {
        invoker.undoCommand()
    }

    fun canUndo():Boolean {
        return invoker.canUndo()
    }


}