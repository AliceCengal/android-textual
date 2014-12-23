package com.cengallut.textual

import java.awt.event._
import scala.swing._

class ComponentEventDemo extends ComponentListener {
  
  def componentHidden(e: ComponentEvent) = {
    println("Component Hidden")
  }
  
  def componentMoved(e: ComponentEvent) = {
    println("Component moved")
  }
  
  def componentResized(e: ComponentEvent) = {
    println("Component resized")
  }
  
  def componentShown(e: ComponentEvent) = {
    println("Component shown")
  }
  
}

class TextualPanel extends Panel {
  
  override def paintComponent(g: Graphics2D) {
    g.clearRect(0, 0, size.width, size.height)
    
  }
  
}


