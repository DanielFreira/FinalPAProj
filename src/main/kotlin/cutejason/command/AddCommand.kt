package cutejason.command

import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonNull
import cutejason.classes.CuteJasonNum
import cutejason.classes.CuteJasonObj


class AddCommand (private val cuteJasonObj: CuteJasonObj, private val propertyName: String, private val listPropertyName: String? = null) : Command {
    private var used = false
    private var addedPropertyIndex: Int? = null
    override fun execute() {
        if (!used) {

            if(listPropertyName == null) {
                cuteJasonObj.value[propertyName] = CuteJasonNull
            }
            else {
                if(cuteJasonObj.value[listPropertyName] is CuteJasonList){
                    var tempCuteJasonList: CuteJasonList = cuteJasonObj.value[listPropertyName] as CuteJasonList
                    tempCuteJasonList.value.add(CuteJasonNull)
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

