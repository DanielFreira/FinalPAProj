import cutejason.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class CuteJasonTests {

    @Test
    fun testCuteJasonNullGenerateJson() {
        assertEquals("null", CuteJasonNull.generateJson())
    }

    @Test
    fun testCuteJasonStringGenerateJson(){
        val cuteJasonStr = CuteJasonStr("Teste")
        assertEquals("\"Teste\"", cuteJasonStr.generateJson())
    }

    @Test
    fun testCuteJasonNumGenerateJson(){
        val cuteJasonNum = CuteJasonNum(123456.0)
        assertEquals("123456.0", cuteJasonNum.generateJson())
    }

    @Test
    fun testCuteJasonBoolGenerateJson(){
        val cuteJasonBool = CuteJasonBool(true)
        assertEquals("true", cuteJasonBool.generateJson())
    }

    @Test
    fun testCuteJasonListGenerateJson(){
        val cuteJasonList = CuteJasonList(listOf(
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

        assertEquals("[{\"keyString1\" : \"value\"," +
                "\"keyNumeric1\" : 123456.0," +
                "\"keyBoolean1\" : true," +
                "\"keyNull1\" : null}," +
                " {\"keyString2\" : \"value2\"," +
                "\"keyNumeric2\" : 654321.0," +
                "\"keyBoolean2\" : false," +
                "\"keyNull2\" : null}]", cuteJasonList.generateJson())
    }

    @Test
    fun testCuteJasonObjGenerateJson(){
        val cuteJasonObj = CuteJasonObj(mapOf(
            "keyString" to CuteJasonStr("value"),
            "keyNumeric" to CuteJasonNum(123456.0),
            "keyBoolean" to CuteJasonBool(true),
            "keyNull" to CuteJasonNull
        ))

        assertEquals("{\"keyString\" : \"value\"," +
                "\"keyNumeric\" : 123456.0," +
                "\"keyBoolean\" : true," +
                "\"keyNull\" : null}", cuteJasonObj.generateJson())
    }

}