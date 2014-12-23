package com.cengallut.textual

import java.awt.event._
import scala.swing._


object TestApp extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title = "A Sample Scala Swing GUI"
    
    val textual = new TextualPanel
    contents = textual
    size = new Dimension(1600, 1000)
    centerOnScreen()
    
    listenTo(textual)
    
    reactions += {
      case e => println(e)
    }
  }
  
}