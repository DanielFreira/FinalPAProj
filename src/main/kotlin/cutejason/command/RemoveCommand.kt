package cutejason.command

import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class RemoveCommand(private val cuteJasonObj: CuteJasonObj, private val propertyName: String) : Command{

    private var used = false
    private var removedProperty: CuteJasonVal? = null

    override fun execute() {
        if (!used){
            removedProperty = cuteJasonObj.value.remove(propertyName)
            used = true
        }
    }

    override fun undo() {
        if (used) {
            if (removedProperty != null){
                cuteJasonObj.value[propertyName] = removedProperty!!
            }
            used = false
        }
    }



}