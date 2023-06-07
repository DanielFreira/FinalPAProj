package cutejason.command

import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonNum
import cutejason.classes.CuteJasonObj


class AddCommand (private val cuteJasonObj: CuteJasonObj, private val propertyName: String, private val propertyValue: Any, private val listPropertyName: String? = null) : Command {
    private var used = false
    private val convertedPropertyValue = propertyValue.toCuteJason()
    private var addedPropertyIndex: Int? = null
    override fun execute() {
        if (!used) {

            if(listPropertyName == null) {
                cuteJasonObj.value[propertyName] = convertedPropertyValue
            }
            else {
                if(cuteJasonObj.value[listPropertyName] is CuteJasonList){
                    var tempCuteJasonList: CuteJasonList = cuteJasonObj.value[listPropertyName] as CuteJasonList
                    tempCuteJasonList.value.add(convertedPropertyValue)
                    addedPropertyIndex = tempCuteJasonList.value.size - 1
                    cuteJasonObj.value[listPropertyName] = tempCuteJasonList
                }
            }

            used = true
        }
        cuteJasonObj.updateObservers()
    }

    override fun undo() {
        if (used) {

            if(listPropertyName == null || addedPropertyIndex == null) {
                cuteJasonObj.value.remove(propertyName)
            }
            else {
                if(cuteJasonObj.value[listPropertyName] is CuteJasonList){
                    var tempCuteJasonList: CuteJasonList = cuteJasonObj.value[listPropertyName] as CuteJasonList
                    tempCuteJasonList.value.removeAt(addedPropertyIndex!!)
                    cuteJasonObj.value[listPropertyName] = tempCuteJasonList
                }
            }

            used = false
        }
        cuteJasonObj.updateObservers()
    }
}

