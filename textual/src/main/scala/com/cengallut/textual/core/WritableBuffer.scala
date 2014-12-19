package com.cengallut.textual.core

import android.graphics.Rect

object WritableBuffer {

  def zero: WritableBuffer = WritableBuffer.ofDim(0, 0)

  def ofDim(xDim: Int, yDim: Int): WritableBuffer =
    new BasicBuffer(xDim, yDim)

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

  /** Wrapper class for adding extension methods for cloning and sub-viewing
    * a buffer. */
  implicit class Prototype(val b: WritableBuffer) extends AnyVal {

    /** Returns a view into the current buffer.
      *
      * offset_1 :: left
      * offset_2 :: top
      * offset_3 :: width
      * offset_4 :: height
      *
      * if b is a 6x5 buffer:
      *
      *     ######
      *     ######
      *     ######
      *     ######
      *     ######
      *
      * Then calling b.takeView(1, 2, 3, 2) return a view to
      * the region marked as 'O'
      *
      *     ######
      *     ######
      *     #OOO##
      *     #OOO##
      *     ######
      *
      * The returned Buffer shares the underlying representation
      * with the original Buffer. */
    def takeView(offset: (Int,Int,Int,Int)): WritableBuffer =
      new BufferView(b, offset)

    /** Same as before. */
    def takeView(left: Int, top: Int, width: Int, height: Int): WritableBuffer =
      new BufferView(b, (left, top, width, height))

    /** Same as before. */
    def takeView(rect: Rect): WritableBuffer =
      new BufferView(b, (rect.left, rect.top, rect.width(), rect.height()))

    /**  */
    def shrink(offset: (Int,Int,Int,Int)): WritableBuffer = {
      val width = b.gridWidth - offset._3 - offset._1
      val height = b.gridHeight - offset._4 - offset._2
      new BufferView(b, offset.copy(_3 = width, _4 = height))
    }

    def copy: WritableBuffer = {
      val bNew = new BasicBuffer(b.gridWidth, b.gridHeight)
      for {
        x <- 0 until b.gridWidth
        y <- 0 until b.gridHeight
      } bNew.setChar(x, y, b.charAt(x, y))
      bNew
    }

  }

}

trait WritableBuffer {

  def bufferChanged(): Unit

  def setUpdateListener(listener: WritableBuffer.UpdateListener): Unit

  def gridWidth: Int

  def gridHeight: Int

  def setChar(x: Int, y: Int, c: Char): Unit

  def charAt(x: Int, y: Int): Char

  def isInRange(x: Int, y: Int) =
    (x >= 0 && x < gridWidth) && (y >= 0 && y < gridHeight)

}

private class BasicBuffer(xDim: Int, yDim: Int) extends WritableBuffer {
  import WritableBuffer._

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
  import WritableBuffer._

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

