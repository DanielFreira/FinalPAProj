package cutejason.command

import cutejason.classes.*


class RemoveCommand(private val cuteJasonObj: CuteJasonObj, private val property: CuteJasonVal) : Command{

    private var used = false
    private var parent: CuteJasonVal? = null
    private var path: String? = null
    private var removedProperty: CuteJasonVal? = null

    override fun execute() {
        if (!used) {
            val parentWithPath = cuteJasonObj.findParentWithPath(property)
            if (parentWithPath != null) {
                val (parentObject, currentPath) = parentWithPath
                parent = parentObject
                path = currentPath

                if (parentObject is CuteJasonObj) {
                    val propertyKey = currentPath.substringAfterLast('.')
                    removedProperty = parentObject.value.remove(propertyKey)
                } else if (parentObject is CuteJasonList) {
                    val index = currentPath.substringAfterLast('[').substringBeforeLast(']').toInt()
                    removedProperty = parentObject.value.removeAt(index)
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
                (parent as CuteJasonObj).value[propertyKey] = removedProperty!!
            } else if (parent is CuteJasonList) {
                val index = path!!.substringAfterLast('[').substringBeforeLast(']').toInt()
                (parent as CuteJasonList).value.add(index, removedProperty!!)
            }

            used = false
        }

        cuteJasonObj.updateObservers()
    }

}