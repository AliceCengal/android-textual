package com.cengallut.textual.basic

import android.graphics.Rect
import com.cengallut.textual.basic.WritableBuffer.UpdateListener

object WritableBuffer {

  def zero: WritableBuffer = WritableBuffer.ofDim(0, 0)

  def ofDim(xDim: Int, yDim: Int): WritableBuffer =
    new BasicBuffer(xDim, yDim)

  def takeView(b: WritableBuffer,
               left: Int, top: Int, width: Int, height: Int): WritableBuffer =
    new BufferView(b, (left, top, width, height))

  def takeView(b: WritableBuffer, rect: Rect): WritableBuffer =
    new BufferView(b, (rect.left, rect.top, rect.width(), rect.height()))

  def copy(b: WritableBuffer): WritableBuffer = {
    val bNew = new BasicBuffer(b.gridWidth, b.gridHeight)
    for {
      x <- 0 until b.gridWidth
      y <- 0 until b.gridHeight
    } bNew.setChar(x, y, b.charAt(x, y))
    bNew
  }

  trait UpdateListener {
    def onBufferUpdated(): Unit
  }

  object UpdateListener {
    def apply(listener: ()=>Unit) = new UpdateListener {
      override def onBufferUpdated(): Unit = listener()
    }

    def apply() = new UpdateListener {
      override def onBufferUpdated(): Unit = ()
    }
  }

}

trait WritableBuffer {

  def bufferChanged(): Unit

  def setUpdateListener(listener: UpdateListener): Unit

  def gridWidth: Int

  def gridHeight: Int

  def setChar(x: Int, y: Int, c: Char): Unit

  def charAt(x: Int, y: Int): Char

  def isInRange(x: Int, y: Int) =
    (x >= 0 && x < gridWidth) && (y >= 0 && y < gridHeight)

}

private class BasicBuffer(xDim: Int, yDim: Int) extends WritableBuffer {

  val buffer = Array.ofDim[Char](xDim * yDim)

  private var updateListener = UpdateListener()

  override def bufferChanged(): Unit = updateListener.onBufferUpdated()

  def charAt(x: Int, y: Int) = {
    assert(isInRange(x, y), "index out of bound")
    buffer(x + (y * gridWidth))
  }

  override def setUpdateListener(listener: UpdateListener): Unit =
    updateListener = listener

  override def gridWidth: Int = xDim

  override def setChar(x: Int, y: Int, c: Char): Unit =
    if (isInRange(x, y)) buffer(x + (y * gridWidth)) = c

  override def gridHeight: Int = yDim

}

private class BufferView(basis: WritableBuffer, bound: (Int,Int,Int,Int)) extends WritableBuffer {

  override def bufferChanged(): Unit =
    basis.bufferChanged()

  override def gridWidth: Int = bound._3

  override def gridHeight: Int = bound._4

  override def setChar(x: Int, y: Int, c: Char): Unit =
    basis.setChar(x + bound._1, y + bound._2, c)

  override def charAt(x: Int, y: Int): Char =
    basis.charAt(x + bound._1, y + bound._2)

  override def setUpdateListener(listener: UpdateListener): Unit =
    basis.setUpdateListener(listener)

}

