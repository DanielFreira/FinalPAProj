package cutejason.command

import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal

class AlterCommand(private val cuteJasonObj: CuteJasonObj, private val property: CuteJasonVal, private val newPropertyValue: Any) : Command {

    private var used = false
    private var parent: CuteJasonVal? = null
    private var path: String? = null
    private var previousPropertyValue: CuteJasonVal? = null
    private val convertedNewPropertyValue = newPropertyValue.toCuteJason()

    override fun execute() {
        if (!used) {

            val parentWithPath = cuteJasonObj.findParentWithPath(property)
            println(parentWithPath)
            if (parentWithPath != null) {
                val (parentObject, currentPath) = parentWithPath
                parent = parentObject
                path = currentPath

                if (parentObject is CuteJasonObj) {
                    val propertyKey = currentPath.substringAfterLast('.')
                    //println("Path: " + currentPath)
                    //println("PropertyKey: " +propertyKey)
                    previousPropertyValue = parentObject.value[propertyKey]
                    (parent as CuteJasonObj).value[propertyKey] = convertedNewPropertyValue!!
                    //println("Converted: " +convertedNewPropertyValue)
                } else if (parentObject is CuteJasonList) {
                    val index = currentPath.substringAfterLast('[').substringBeforeLast(']').toInt()
                    previousPropertyValue = parentObject.value[index]
                    (parent as CuteJasonList).value.add(index, convertedNewPropertyValue!!)
                    //println(convertedNewPropertyValue)
                }
                used = true
            }

        }
        cuteJasonObj.updateObservers()
    }

    override fun undo() {

        if (used && parent != null && path != null) {
            if (parent is CuteJasonObj) {
                val propertyKey = path!!.substringAfterLast('.')
                (parent as CuteJasonObj).value[propertyKey] = previousPropertyValue!!
            } else if (parent is CuteJasonList) {
                val index = path!!.substringAfterLast('[').substringBeforeLast(']').toInt()
                (parent as CuteJasonList).value[index] = previousPropertyValue!!
            }
            used = false
        }
        cuteJasonObj.updateObservers()
    }


}