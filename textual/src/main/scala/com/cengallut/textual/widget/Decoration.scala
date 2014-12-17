package com.cengallut.textual.widget

import com.cengallut.textual.basic.WritableBuffer

trait Decoration {

  def decorate(buffer: WritableBuffer): Unit

}

object Decoration {

  def compose(d1: Decoration, d2: Decoration): Decoration = new Decoration {
    override def decorate(buffer: WritableBuffer): Unit = {
      d1.decorate(buffer)
      d2.decorate(buffer)
    }
  }

}