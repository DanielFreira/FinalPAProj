import cutejason.*

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
        CuteJasonStr("hello"),
        CuteJasonNum(123.0),
        CuteJasonBool(true)
    ))

    println(testJasonObj.generateJson())
    println(testJasonObj2.generateJson())

}