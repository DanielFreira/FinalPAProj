package cutejason.command

interface Command {
    fun execute()
    fun undo()
}