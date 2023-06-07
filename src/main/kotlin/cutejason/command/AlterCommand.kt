package cutejason.command

import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class AlterCommand(private val cuteJasonObj: CuteJasonObj, private val propertyName: String, private val newPropertyValue: Any, private val listPropertyName: String? = null, private val index: Int? = null) : Command {

    private var used = false
    private var previousPropertyValue: CuteJasonVal? = null
    private val convertedNewPropertyValue = newPropertyValue.toCuteJason()

    override fun execute() {
        if (!used) {

            if(index == null || listPropertyName == null) {
                previousPropertyValue = cuteJasonObj.value[propertyName]
                cuteJasonObj.value[propertyName] = convertedNewPropertyValue
            }
            else {
                if(cuteJasonObj.value[listPropertyName] is CuteJasonList){
                    var tempCuteJasonList: CuteJasonList = cuteJasonObj.value[listPropertyName] as CuteJasonList
                    previousPropertyValue = tempCuteJasonList.value[index]
                    tempCuteJasonList.value[index] = convertedNewPropertyValue
                }
            }

            used = true
        }
        cuteJasonObj.updateObservers()
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
        cuteJasonObj.updateObservers()
    }


}