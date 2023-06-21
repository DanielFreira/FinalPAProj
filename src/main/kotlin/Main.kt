import cutejason.classes.*
import cutejason.classes.CuteJasonConverter.toCuteJason
import cutejason.command.AddCommand
import cutejason.command.AlterCommand
import cutejason.command.Invoker
import cutejason.command.RemoveCommand
import cutejason.controller.Controller
import cutejason.visitor.SearchByKeyVisitor

data class Enrolled(
    val numero: Int,
    val nome: String,
    val internacional: Boolean
)
data class Course(
    val uc: String,
    val ects: Double,
    val dataexame: String?,
    val inscritos: List<Enrolled>?
)

fun main(args: Array<String>) {

    val testJasonObj = CuteJasonObj(
        mutableMapOf(
        "uc" to CuteJasonStr("PA"),
        "ects" to CuteJasonNum(6.0),
        "data-exame" to CuteJasonNull,
        "inscritos" to CuteJasonList(mutableListOf(
            CuteJasonObj(mutableMapOf(
                "numero" to CuteJasonNum(101101.0),
                "nome" to CuteJasonStr("Dave Farley"),
                "internacional" to CuteJasonBool(true)
            )),
            CuteJasonObj(mutableMapOf(
                "numero" to CuteJasonNum(101102.0),
                "nome" to CuteJasonStr("Martin Fowler"),
                "internacional" to CuteJasonBool(true)
            )),
            CuteJasonObj(mutableMapOf(
                "numero" to CuteJasonNum(26503.0),
                "nome" to CuteJasonStr("André Santos"),
                "internacional" to CuteJasonBool(false)
            ))
        )
        )
    )
    )

    val testJasonObj2 = CuteJasonList(mutableListOf(
        CuteJasonObj(mutableMapOf(
            "keyString1" to CuteJasonStr("value"),
            "keyNumeric1" to CuteJasonNum(123456.0),
            "keyBoolean1" to CuteJasonBool(true),
            "keyNull1" to CuteJasonNull
        )),
        CuteJasonObj(mutableMapOf(
            "keyString2" to CuteJasonStr("value2"),
            "keyNumeric2" to CuteJasonNum(654321.0),
            "keyBoolean2" to CuteJasonBool(false),
            "keyNull2" to CuteJasonNull
        ))
    ))

    val myCourse = Course(
        "PA",
        6.0,
        null,
        listOf(
            Enrolled(101101,"Dave Farley", true),
            Enrolled(101102, "Martin Fowler", true),
            Enrolled(26503, "André Santos",false)
        )
    )



    val invoker = Invoker()

    val controller = Controller(invoker, testJasonObj)
    val cuteJasonEditor = CuteJasonEditor(controller, testJasonObj)

    cuteJasonEditor.open()




}