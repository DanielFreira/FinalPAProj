package cutejason.controller

import cutejason.classes.CuteJasonObj
import cutejason.command.*


class Controller(private val invoker: Invoker, private val cuteJasonObj: CuteJasonObj) {

    fun addProperty(propertyName: String) {
        val addCommand = AddCommand(cuteJasonObj, propertyName)
        invoker.useCommand(addCommand)
    }

    fun alterProperty(propertyName: String, newPropertyValue: Any) {

        if(cuteJasonObj.contains(propertyName)){
            val alterCommand = AlterCommand(cuteJasonObj, propertyName, newPropertyValue)
            invoker.useCommand(alterCommand)
        }

    }

    fun removeProperty(propertyName: String, index: Int? = null) {
        val removeCommand = RemoveCommand(cuteJasonObj, propertyName)
        invoker.useCommand(removeCommand)
    }

    fun undo() {
        invoker.undoCommand()
    }

    fun canUndo():Boolean {
        return invoker.canUndo()
    }

}