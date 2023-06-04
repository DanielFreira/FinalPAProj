package cutejason.command

import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class AddCommand (private val cuteJasonObj: CuteJasonObj, private val propertyName: String, private val propertyValue: Any) : Command {
    private var used = false
    private val convertedPropertyValue = propertyValue.toCuteJason()
    override fun execute() {
        if (!used) {
            cuteJasonObj.value[propertyName] = convertedPropertyValue
            used = true
        }
    }

    override fun undo() {
        if (used) {
            cuteJasonObj.value.remove(propertyName)
            used = false
        }
    }
}

