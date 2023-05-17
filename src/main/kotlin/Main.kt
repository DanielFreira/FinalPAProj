import cutejason.classes.*

fun main(args: Array<String>) {

    val testJasonObj = CuteJasonObj(mapOf(
        "uc" to CuteJasonStr("PA"),
        "ectcs" to CuteJasonNum(6.0),
        "data-exame" to CuteJasonNull,
        "inscritos" to CuteJasonList(listOf(
            CuteJasonObj(mapOf(
                "numero" to CuteJasonNum(101101.0),
                "nome" to CuteJasonStr("Dave Farley"),
                "internacional" to CuteJasonBool(true)
            )),
            CuteJasonObj(mapOf(
                "numero" to CuteJasonNum(101102.0),
                "nome" to CuteJasonStr("Martin Fowler"),
                "internacional" to CuteJasonBool(true)
            )),
            CuteJasonObj(mapOf(
                "numero" to CuteJasonNum(26503.0),
                "nome" to CuteJasonStr("André Santos"),
                "internacional" to CuteJasonBool(false)
            ))
        ))
    ))

    val testJasonObj2 = CuteJasonList(listOf(
        CuteJasonObj(mapOf(
            "keyString1" to CuteJasonStr("value"),
            "keyNumeric1" to CuteJasonNum(123456.0),
            "keyBoolean1" to CuteJasonBool(true),
            "keyNull1" to CuteJasonNull
        )),
        CuteJasonObj(mapOf(
            "keyString2" to CuteJasonStr("value2"),
            "keyNumeric2" to CuteJasonNum(654321.0),
            "keyBoolean2" to CuteJasonBool(false),
            "keyNull2" to CuteJasonNull
        ))
    ))

    println(testJasonObj.generateJson())
    println(testJasonObj2.generateJson())

}