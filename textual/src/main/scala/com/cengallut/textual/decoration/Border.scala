package com.cengallut.textual.decoration

import com.cengallut.textual.core.WritableBuffer

object Border {

  class Simple(border: Char) extends Decoration {
    override def decorate(buffer: WritableBuffer): Unit = {
      for (x <- 0 until buffer.gridWidth) {
        buffer.setChar(x, 0, border)
        buffer.setChar(x, buffer.gridHeight - 1, border)
      }
      for (y <- 1 until buffer.gridHeight - 1) {
        buffer.setChar(0, y, border)
        buffer.setChar(buffer.gridWidth - 1, y, border)
      }
    }
  }

}

