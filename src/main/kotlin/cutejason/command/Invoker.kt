package cutejason.command

class Invoker {

    private val usedCommands: MutableList<Command> = mutableListOf()


    //executes command and adds it to used commands list
    fun useCommand(command: Command){
        command.execute()
        usedCommands.add(command)
    }

    //undoes the last used command
    fun undoCommand(){
        if (usedCommands.isNotEmpty()) {
            val lastUsedCommand = usedCommands.removeLast()
            lastUsedCommand.undo()
        }
    }

}