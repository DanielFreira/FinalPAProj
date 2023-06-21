import cutejason.classes.*
import cutejason.controller.Controller
import cutejason.observer.*
import java.awt.*
import java.awt.event.*
import javax.swing.*

class CuteJasonEditor(private val controller: Controller, private val cuteJasonObj: CuteJasonObj) : Observer {

    private val srcArea = JTextArea()
    private val undoButton = JButton("Undo")
    private val frame = JFrame("CuteJasonObj Editor")
    private val mainPanel = JPanel()
    private val nestedPanel = JPanel()

    init {
        cuteJasonObj.addObserver(this)
        undoButton.isEnabled = false
        undoButton.addActionListener {
            controller.undo()
            updateUI()
        }

        configureFrame()
        configureMenuBar()
        configureLeftPanel()
        configureRightPanel()

        updateUI()
    }

    fun open(){
        frame.isVisible = true
    }

    private fun configureFrame() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.layout = BorderLayout()
        frame.size = Dimension(600, 600)
        frame.add(mainPanel, BorderLayout.CENTER)
    }

    private fun configureMenuBar() {
        val menuBar = JMenuBar()

        val undoToggle = JToggleButton("Undo")
        undoToggle.addActionListener {
            controller.undo()
            updateUI()
        }

        val panel = JPanel()
        panel.layout = FlowLayout(FlowLayout.LEFT)
        panel.add(undoToggle)

        menuBar.add(panel)

        frame.jMenuBar = menuBar
    }

    private fun configureLeftPanel() {
        mainPanel.apply {
            layout = GridLayout()
            val scrollPane = JScrollPane(nestedPanel).apply {
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            }
            add(scrollPane)
        }
    }

    private fun configureRightPanel() {
        val rightPanel = JPanel().apply {
            layout = GridLayout()
            srcArea.tabSize = 2
            add(srcArea)
        }
        mainPanel.add(rightPanel)
    }

    private fun updateUI() {
        nestedPanel.removeAll()
        nestedPanel.repopulate()
        nestedPanel.revalidate()
        nestedPanel.repaint()

        srcArea.text = cuteJasonObj.generateJsonWithIdents()
        undoButton.isEnabled = controller.canUndo()
    }

    private fun JPanel.repopulate() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS) // Set vertical layout

        cuteJasonObj.value.forEach { (key, value) ->
            add(widget(key, value))
        }
    }
    private fun widget(propertyName: String, cuteJasonVal: CuteJasonVal, parentPath: String = ""): JPanel {
        val panel = JPanel().apply {
            layout = BorderLayout()
            border = BorderFactory.createLineBorder(Color.WHITE, 5)
            background = Color.LIGHT_GRAY

        }

        val propertyNameLabel = JLabel("$propertyName ")

        panel.add(propertyNameLabel, BorderLayout.WEST)

        val valuePanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }

        val popupMenu = JPopupMenu()
        val removeMenuItem = JMenuItem("Remove")
        removeMenuItem.addActionListener {
            controller.removeProperty(cuteJasonVal)
            updateUI()
        }
        popupMenu.add(removeMenuItem)

        val addMenuItem = JMenuItem("Add")
        addMenuItem.addActionListener {
            controller.addProperty(cuteJasonVal, JOptionPane.showInputDialog("Property name"))
            updateUI()
        }
        popupMenu.add(addMenuItem)

        panel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(panel, e.x, e.y)
                }
            }
        })

        when (cuteJasonVal) {
            is CuteJasonObj -> {
                cuteJasonVal.value.forEach { (key, value) ->
                    valuePanel.add(widget(key, value))
                }
            }
            is CuteJasonList -> {
                cuteJasonVal.value.forEach {
                    valuePanel.add(widget("    ", it))
                }
            }
            is CuteJasonBool -> {
                val checkBox = JCheckBox()
                checkBox.isSelected = cuteJasonVal.value
                checkBox.addItemListener { checkBox ->
                    if (checkBox.stateChange == ItemEvent.SELECTED) {
                        controller.alterProperty(cuteJasonVal, true)
                    } else if (checkBox.stateChange == ItemEvent.DESELECTED) {
                        controller.alterProperty(cuteJasonVal, false)
                    }
                }
                valuePanel.add(checkBox)

                cuteJasonVal.addObserver(object : Observer {
                    override fun update(observable: Observable) {
                        SwingUtilities.invokeLater {
                            checkBox.isSelected = cuteJasonVal.value
                        }
                    }
                })
            }
            else -> {
                val textPanel = JPanel().apply {
                    layout = BorderLayout()
                }

                val text = JTextField(cuteJasonVal.toString())
                text.addFocusListener(object : FocusAdapter() {
                    override fun focusLost(e: FocusEvent) {
                        if(cuteJasonVal.toString() != text.text){
                            controller.alterProperty(cuteJasonVal, text.text)
                        }

                    }
                })
                textPanel.add(text, BorderLayout.CENTER)

                valuePanel.add(textPanel)

                cuteJasonVal.addObserver(object : Observer {
                    override fun update(observable: Observable) {
                        SwingUtilities.invokeLater {
                            updateUI()
                        }
                    }
                })
            }
        }

        panel.add(valuePanel, BorderLayout.CENTER)

        return panel
    }


    override fun update(observable: Observable) {
        println("update called, observable: $observable")
        if (observable == cuteJasonObj) {
            SwingUtilities.invokeLater {
                updateUI()
            }
        }
    }

}
