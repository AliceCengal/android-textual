package com.cengallut.textual.decoration

import com.cengallut.textual.core.CharGrid

trait Decoration {

  def decorate(buffer: CharGrid): Unit

}

object Decoration {

  def compose(d1: Decoration, d2: Decoration): Decoration = new Decoration {
    override def decorate(buffer: CharGrid): Unit = {
      d1.decorate(buffer)
      d2.decorate(buffer)
    }
  }

}