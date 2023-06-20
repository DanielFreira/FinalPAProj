import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal
import cutejason.controller.Controller
import cutejason.observer.Observable
import cutejason.observer.Observer
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*

class CuteJasonEditor(private val controller: Controller, private val cuteJasonObj: CuteJasonObj) : Observer {

    private var srcArea = JTextArea()
    private var undoButton = JButton("Undo")

    init {
        cuteJasonObj.addObserver(this)
        undoButton.isEnabled = false
        undoButton.addActionListener {

            controller.undo()
            panel().apply {
                revalidate()
                repaint()
            }
        }
    }

    val frame = JFrame("CuteJason Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = BorderLayout()
        size = Dimension(600, 600)

        val menuBar = JMenuBar()
        val mainMenu = JMenu("Options")
        menuBar.add(mainMenu)
        mainMenu.add(undoButton)

        jMenuBar = menuBar

        val mainPanel = JPanel()
        mainPanel.layout = GridLayout(0, 2)

        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(panel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        mainPanel.add(left)

        val right = JPanel()
        right.layout = GridLayout()
        srcArea.tabSize = 2
        srcArea.text = formatJson(cuteJasonObj.generateJson())
        right.add(srcArea)
        mainPanel.add(right)

        add(mainPanel, BorderLayout.CENTER)
    }

    fun open() {
        frame.isVisible = true
    }

    fun JPanel.repopulate(){
        this.apply {
            removeAll()
            cuteJasonObj.value.forEach {
                add(widget(it.key,it.value))
            }
            revalidate()
            repaint()
        }
    }

    fun panel(): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT


            this.repopulate()


            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Options")

                        val add = JButton("add")
                        add.addActionListener {
                            val propertyName = JOptionPane.showInputDialog("Property name")
                            controller.addProperty(propertyName)
                            menu.isVisible = false
                            removeAll()
                            repopulate()
                            revalidate()
                            repaint()

                        }

                        val undo = JButton("Undo")
                        undo.addActionListener {
                            controller.undo()
                            menu.isVisible = false

                            JPanel().repopulate()
                            revalidate()
                            repaint()
                        }


                        menu.add(add)
                        menu.add(undo)
                        menu.show(this@apply, 100, 100)
                    }
                }
            })
        }


    private fun widget(propertyName: String, cuteJasonVal: CuteJasonVal, index:Int? = null): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(propertyName))

            when (cuteJasonVal) {
                is CuteJasonObj -> {
                    val nestedPanel = JPanel()
                    nestedPanel.layout = BoxLayout(nestedPanel, BoxLayout.Y_AXIS)
                    add(nestedPanel)

                    cuteJasonVal.value.forEach {
                        nestedPanel.add(widget(it.key, it.value))
                    }

                    val removeButton = JButton("Remove")
                    removeButton.addActionListener {
                        controller.removeProperty(propertyName)
                        removeAll()
                        JPanel().repopulate()
                        revalidate()
                        repaint()
                    }
                    add(removeButton)

                }
                is CuteJasonList -> {
                    val nestedPanel = JPanel()
                    nestedPanel.layout = BoxLayout(nestedPanel, BoxLayout.Y_AXIS)
                    add(nestedPanel)

                    cuteJasonVal.value.forEachIndexed { index, element ->
                        nestedPanel.add(widget("  ", element,index))
                    }

                    val removeButton = JButton("Remove")
                    removeButton.addActionListener {
                        controller.removeProperty(propertyName, index)
                        JPanel().repopulate()
                    }
                    add(removeButton)

                    cuteJasonVal.addObserver(object : Observer {
                        override fun update(observable: Observable) {
                            SwingUtilities.invokeLater {
                                removeAll()
                                JPanel().repopulate()
                                revalidate()
                                repaint()
                            }
                        }
                    })

                }
                else -> {
                    val text = JTextField(cuteJasonVal.toString())
                    text.addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {
                            val newValue = text.text
                            controller.alterProperty(propertyName, newValue)
                        }
                    })
                    add(text)

                    val removeButton = JButton("Remove")
                    removeButton.addActionListener {
                        controller.removeProperty(propertyName)
                        removeAll()
                        JPanel().repopulate()
                        revalidate()
                        repaint()
                    }
                    add(removeButton)
                }
            }

            cuteJasonVal.addObserver(object : Observer {
                override fun update(observable: Observable) {
                    SwingUtilities.invokeLater {
                        revalidate()
                        repaint()
                    }
                }
            })

        }


    override fun update(observable: Observable) {
        println("update called, observable: $observable")
        if (observable == cuteJasonObj) {
            SwingUtilities.invokeLater {
                this.srcArea.text = formatJson(cuteJasonObj.generateJson())
                undoButton.isEnabled = controller.canUndo()
            }
        }
    }


    private fun formatJson(json: String): String {
        val indentChar = "    "
        val builder = StringBuilder()
        var level = 0
        var inQuote = false

        for (char in json) {
            when (char) {
                '{', '[' -> {
                    builder.append(char)
                    builder.append('\n')
                    level++
                    for(l in 0..level){
                        builder.append(indentChar)
                    }

                }
                '}', ']' -> {
                    builder.append('\n')
                    level--
                    for(l in 0..level){
                        builder.append(indentChar)
                    }
                    builder.append(char)
                }
                ',' -> {
                    builder.append(char)
                    if (!inQuote) {
                        builder.append('\n')
                        for(l in 0..level){
                            builder.append(indentChar)
                        }
                    }
                }
                '"' -> {
                    builder.append(char)
                    inQuote = !inQuote
                }
                else -> {
                    builder.append(char)
                }
            }
        }

        return builder.toString()
    }

}
