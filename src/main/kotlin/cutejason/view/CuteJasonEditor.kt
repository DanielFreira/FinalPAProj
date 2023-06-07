package cutejason.view

import cutejason.classes.CuteJasonList
import cutejason.classes.CuteJasonObj
import cutejason.classes.CuteJasonVal
import cutejason.controller.Controller
import cutejason.observer.Observable
import cutejason.observer.Observer
import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*

class CuteJasonEditor(private val controller: Controller, private val cuteJasonObj: CuteJasonObj) : Observer {

    private var srcArea = JTextArea()

    init {
        cuteJasonObj.addObserver(this)
    }

    val frame = JFrame("CuteJason Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(600, 600)

        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(panel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)

        val right = JPanel()
        right.layout = GridLayout()
        srcArea.tabSize = 2
        srcArea.text = cuteJasonObj.generateJson()
        right.add(srcArea)
        add(right)
    }

    fun open() {
        frame.isVisible = true
    }

    fun panel(): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            cuteJasonObj.value.forEach {
                add(widget(it.key,it.value))
            }

            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Options")

                        val add = JButton("add")
                        add.addActionListener {
                            val propertyName = JOptionPane.showInputDialog("Property name")
                            val propertyValue = JOptionPane.showInputDialog("Property value")
                            controller.alterProperty(propertyName, propertyValue)
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()

                        }

                        val del = JButton("delete all")
                        del.addActionListener {
                            cuteJasonObj.value.keys.toList().forEach { propertyName ->
                                controller.removeProperty(propertyName)
                            }
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }
                        menu.add(add)
                        menu.add(del)
                        menu.show(this@apply, 100, 100)
                    }
                }
            })
        }


    private fun widget(propertyName: String, cuteJasonVal: CuteJasonVal): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            add(JLabel(propertyName))

            when (cuteJasonVal) {
                is CuteJasonObj -> {
                    val nestedPanel = JPanel()
                    nestedPanel.layout = BoxLayout(nestedPanel, BoxLayout.Y_AXIS)
                    cuteJasonVal.value.forEach {
                        nestedPanel.add(widget(it.key, it.value))
                    }
                    val nestedScrollPane = JScrollPane(nestedPanel).apply {
                        horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                        verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
                    }
                    add(nestedScrollPane)

                    cuteJasonVal.value.forEach {
                        if (it.value is CuteJasonObj || it.value is CuteJasonList) {
                            widget(it.key, it.value).also { nestedWidget ->
                                nestedPanel.add(nestedWidget)
                            }
                        }
                    }

                }
                is CuteJasonList -> {
                    val nestedPanel = JPanel()
                    nestedPanel.layout = BoxLayout(nestedPanel, BoxLayout.Y_AXIS)
                    val nestedScrollPane = JScrollPane(nestedPanel).apply {
                        horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                        verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
                    }
                    add(nestedScrollPane)

                    cuteJasonVal.value.forEachIndexed { index, element ->
                        widget("[$index]", element).also { nestedWidget ->
                            nestedPanel.add(nestedWidget)
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
                else -> {
                    val text = JTextField(cuteJasonVal.generateJson())
                    text.addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {
                            val newValue = text.text
                            controller.alterProperty(propertyName, newValue)
                        }
                    })
                    add(text)
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

    fun onAddProperty(propertyName: String, propertyValue: Any) {
        controller.addProperty(propertyName, propertyValue)
    }

    fun onAlterProperty(propertyName: String, newPropertyValue: Any) {
        controller.alterProperty(propertyName, newPropertyValue)
    }

    fun onRemoveProperty(propertyName: String) {
        controller.removeProperty(propertyName)
    }

    fun onUndo() {
        controller.undo()
    }

    override fun update(observable: Observable) {
        println("update called, observable: $observable")
        if (observable == cuteJasonObj){
            SwingUtilities.invokeLater {
                this.srcArea.text = cuteJasonObj.generateJson()
            }
        }
    }
}






