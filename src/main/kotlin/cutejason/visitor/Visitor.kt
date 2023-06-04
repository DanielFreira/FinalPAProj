import cutejason.classes.*


interface Visitor {

    fun visit(cuteJasonObj: CuteJasonObj): Boolean = true
    fun visit(cuteJasonStr: CuteJasonStr)
    fun visit(cuteJasonNum: CuteJasonNum)
    fun visit(cuteJasonBool: CuteJasonBool)
    fun visit(cuteJasonNull: CuteJasonNull)
    fun visit(cuteJasonList: CuteJasonList): Boolean = true

}