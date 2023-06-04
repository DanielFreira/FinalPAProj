package cutejason.command

import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class AlterCommand(private val cuteJasonObj: CuteJasonObj, private val propertyName: String, private val newPropertyValue: Any) : Command {

    private var used = false
    private var previousPropertyValue: CuteJasonVal? = null
    private val convertedNewPropertyValue = newPropertyValue.toCuteJason()

    override fun execute() {
        if (!used) {
            previousPropertyValue = cuteJasonObj.value[propertyName]
            cuteJasonObj.value[propertyName] = convertedNewPropertyValue
            used = true
        }
    }

    override fun undo() {
        if (used) {
            if (previousPropertyValue != null) {
                cuteJasonObj.value[propertyName] = previousPropertyValue!!
            } else {
                cuteJasonObj.value.remove(propertyName)
            }
            used = false
        }
    }


}