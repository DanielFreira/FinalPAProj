package cutejason.controller

import cutejason.classes.CuteJasonObj
import cutejason.command.*


class Controller(private val invoker: Invoker, private val cuteJasonObj: CuteJasonObj) {

    fun addProperty(propertyName: String, propertyValue: Any) {
        val addCommand = AddCommand(cuteJasonObj, propertyName, propertyValue)
        invoker.useCommand(addCommand)
    }

    fun alterProperty(propertyName: String, newPropertyValue: Any) {

        if(cuteJasonObj.contains(propertyName)){
            val alterCommand = AlterCommand(cuteJasonObj, propertyName, newPropertyValue)
            invoker.useCommand(alterCommand)
        }

    }

    fun removeProperty(propertyName: String) {
        val removeCommand = RemoveCommand(cuteJasonObj, propertyName)
        invoker.useCommand(removeCommand)
    }

    fun undo() {
        invoker.undoCommand()
    }


}