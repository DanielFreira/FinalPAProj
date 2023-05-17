import cutejason.classes.*

interface Visitor {

    fun visit(cuteJasonObj: CuteJasonObj)
    fun visit(cuteJasonStr: CuteJasonStr)
    fun visit(cuteJasonNum: CuteJasonNum)
    fun visit(cuteJasonBool: CuteJasonBool)
    fun visit(cuteJasonNull: CuteJasonNull)
    fun visit(cuteJasonList: CuteJasonList)

}