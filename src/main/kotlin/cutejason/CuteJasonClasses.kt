package cutejason

// Base object type
sealed class CuteJasonVal

//Object that represents the json struct
data class CuteJasonObj(val value: Map<String, CuteJasonVal>) : CuteJasonVal()
//String type object for json struct
data class CuteJasonStr(val value: String) : CuteJasonVal()
//Numeric type object for json struct
data class CuteJasonNum(val value: Double) : CuteJasonVal()
//Boolean type object for json struct
data class  CuteJasonBool(val value: Boolean) : CuteJasonVal()
//List type object for json struct
data class CuteJasonList(val value: List<CuteJasonVal>) : CuteJasonVal()
//Null type object fot json struct
object CuteJasonNull : CuteJasonVal()

