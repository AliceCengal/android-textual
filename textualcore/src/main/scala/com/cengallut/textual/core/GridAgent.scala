package com.cengallut.textual.core

trait GridAgent {
  def onAction(x: Int, y: Int): Unit
}

object GridAgent {
  def apply(f: (Int,Int)=>Unit): GridAgent = new GridAgent {
    override def onAction(x: Int, y: Int): Unit = f(x, y)
  }
}