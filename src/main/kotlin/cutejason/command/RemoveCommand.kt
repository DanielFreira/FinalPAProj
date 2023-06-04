package cutejason.command

import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class RemoveCommand(private val cuteJasonObj: CuteJasonObj, private val propertyName: String) : Command{

    private var used = false
    private var removedPropertyValue: CuteJasonVal? = null

    override fun execute() {
        if (!used){
            removedPropertyValue = cuteJasonObj.value.remove(propertyName)
            used = true
        }
    }

    override fun undo() {
        if (used) {
            if (propertyName != null){
                cuteJasonObj.value[propertyName] = removedPropertyValue!!
            }
            used = false
        }
    }


}