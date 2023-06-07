package cutejason.command

import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class RemoveCommand(private val cuteJasonObj: CuteJasonObj, private val propertyName: String, private val index: Int? = null) : Command{

    private var used = false
    private var removedProperty: CuteJasonVal? = null
    private var removedListProperty: CuteJasonVal? = null

    override fun execute() {
        if (!used){

            if(index == null){
                removedProperty = cuteJasonObj.value.remove(propertyName)
                used = true
            }
            else{
                if(cuteJasonObj.value[propertyName] is CuteJasonList){
                    val tempCuteJasonList: CuteJasonList = cuteJasonObj.value[propertyName] as CuteJasonList
                    removedListProperty = tempCuteJasonList.value.removeAt(index)
                    used = true
                }
            }

        }

        cuteJasonObj.updateObservers()
    }

    override fun undo() {
        if (used) {
            if (removedProperty != null){

                if(index == null){
                    cuteJasonObj.value[propertyName] = removedProperty!!
                }
                else{
                    if(cuteJasonObj.value[propertyName] is CuteJasonList){
                        var tempCuteJasonList: CuteJasonList = cuteJasonObj.value[propertyName] as CuteJasonList
                        tempCuteJasonList.value.add(index, removedListProperty!!)
                        cuteJasonObj.value[propertyName] = tempCuteJasonList
                    }
                }
            }
            used = false
        }

        cuteJasonObj.updateObservers()
    }



}