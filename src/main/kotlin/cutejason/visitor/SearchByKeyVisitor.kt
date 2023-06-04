package cutejason.visitor

import Visitor
import cutejason.classes.*

class SearchByKeyVisitor(val keyToSearch: String) : Visitor {

    //List of values found
    private val valuesOfSearchedKey = mutableListOf<CuteJasonVal>()


    fun getValues() : List<CuteJasonVal>? {
        return if (valuesOfSearchedKey.isEmpty()) {
            null
        } else {
            valuesOfSearchedKey
        }
    }
    override fun visit(cuteJasonObj: CuteJasonObj): Boolean {
        //
        cuteJasonObj.value[keyToSearch]?.let { valuesOfSearchedKey.add(it) }
        return true
    }

    override fun visit(cuteJasonStr: CuteJasonStr) {}

    override fun visit(cuteJasonNum: CuteJasonNum) {}

    override fun visit(cuteJasonBool: CuteJasonBool) {}

    override fun visit(cuteJasonNull: CuteJasonNull) {}


    override fun visit(cuteJasonList: CuteJasonList): Boolean {
        //redirects visitor to the objects of the list
        return true
    }

}