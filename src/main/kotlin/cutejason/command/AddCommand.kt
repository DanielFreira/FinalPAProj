package cutejason.command

import cutejason.classes.*
import cutejason.classes.CuteJasonConverter.toCuteJason


class AddCommand (private val cuteJasonObj: CuteJasonObj, private val parentProperty: CuteJasonVal, private val propertyName: String) : Command {
    private var used = false

    private var parent: CuteJasonVal? = null
    private var path: String? = null

    override fun execute() {

        if (!used) {
            val parentWithPath = cuteJasonObj.findParentWithPath(parentProperty)
            if (parentWithPath != null) {
                val (parentObject, currentPath) = parentWithPath

                parent = parentObject
                path = currentPath

                if (parent is CuteJasonObj) {
                    (parent as CuteJasonObj).value[propertyName] = CuteJasonNull
                } else if (parent is CuteJasonList) {
                    val addedPropertyName = CuteJasonObj(
                        mutableMapOf(
                            propertyName to CuteJasonNull
                        ))

                    (parent as CuteJasonList).value.add(addedPropertyName)
                }

                used = true
            }
        }

        cuteJasonObj.updateObservers()
    }

    override fun undo() {
        if (used && parent != null && path != null) {
            if (parent is CuteJasonObj) {
                (parent as CuteJasonObj).value.remove(propertyName)
            } else if (parent is CuteJasonList) {
                val list = (parent as CuteJasonList).value
                val lastIndex = list.lastIndex
                if (list.isNotEmpty() && list[lastIndex] is CuteJasonObj) {
                    val lastObj = list[lastIndex] as CuteJasonObj
                    lastObj.value.remove(propertyName)
                    if (lastObj.value.isEmpty()) {
                        list.removeAt(lastIndex)
                    }
                }
            }

            used = false
        }
        cuteJasonObj.updateObservers()
    }

}

