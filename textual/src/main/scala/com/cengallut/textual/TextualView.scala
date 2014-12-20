package com.cengallut.textual

import android.app.Activity
import android.content.Context
import android.graphics._
import com.cengallut.textual.TextualView.BufferStateListener
import com.cengallut.textual.aside.Sugar._
import com.cengallut.textual.core.{Cursor, CharGrid}

/** Displays an array of characters as a grid throughout its view window. */
abstract class TextualView(context: Context)
    extends android.view.View(context)
    with CharGrid.UpdateListener {

  def textSize: Float = 48f

  def textColor: Int = Color.WHITE

  def background: Int = Color.BLACK

  def bufferStateListener: BufferStateListener = new BufferStateListener {
    override def onBufferReady(buffer: CharGrid): Unit = ()
  }

  def gridTouchListener: GridTouchListener = new GridTouchListener {
    override def onGridTouch(x: Int, y: Int): Unit = ()
  }

  private[textual] var buffer = CharGrid.zero

  def getBuffer: CharGrid = buffer

  lazy val cursor: Cursor = Cursor.base(buffer)

  /** Holds the information needed to draw characters onto the canvas. This object is used to
    * calculate the individual sizes of each character, and so determines how big of a buffer is
    * needed to fill a screen. */
  private val paint = {
    val p = new Paint
    p.setTextSize(textSize)
    p.setColor(textColor)
    p.setTypeface(Typeface.MONOSPACE)
    p
  }

  /** Calculate the maximum bounding box for a character when it is drawn on a canvas, using
    * the paint object created previously. This dimension includes ascenders and descenders. */
  private val charDimension = {
    val rect = new Rect
    paint.getTextBounds(Array('j', 'k'), 0, 2, rect)
    val charHeight = rect.height()
    paint.getTextBounds(Array('m'), 0, 1, rect)
    val charWidth = rect.width()
    (charWidth, charHeight)
  }

  override /* WritableBuffer.UpdateListener */
  def onGridUpdated(): Unit = invalidate()

  override /* android.view.View */
  protected def onSizeChanged(w: Int, h: Int, ow: Int, oh: Int): Unit = {
    setOnTouchListener(gridTouchListener)
    buffer = CharGrid.ofDim(w / charDimension.x, h / charDimension.y)
    buffer.setUpdateListener(this)
    bufferStateListener.onBufferReady(buffer)
  }

  override /* android.view.View */
  protected def onDraw(c: Canvas): Unit = {
    c.drawColor(background)

    val charBuffer = Array('c')
    var x = buffer.width
    var y = buffer.height
    val xDelta = getWidth.toFloat / x
    val yDelta = getHeight.toFloat / y

    while (y > 0) {
      y -= 1
      x = buffer.width
      while (x > 0) {
        x -= 1

        charBuffer(0) = buffer.charAt(x, y)
        c.drawText(charBuffer, 0, 1, xDelta*x, yDelta*(y+1), paint)
      }
    }
  }

}

object TextualView {

  trait BufferStateListener {
    def onBufferReady(buffer: CharGrid): Unit
  }

  def create(act: Activity): TextualView = {
    act match {
      case a: Activity with GridTouchListener with BufferStateListener =>
        new TextualView(act) {
          override def bufferStateListener: BufferStateListener = a

          override def gridTouchListener: GridTouchListener = a
        }
      case a: Activity with GridTouchListener =>
        new TextualView(act) {
          override def gridTouchListener: GridTouchListener = a
        }
      case a: Activity with BufferStateListener =>
        new TextualView(act) {
          override def bufferStateListener: BufferStateListener = a
        }
      case _ =>
        new TextualView(act) {}
    }
  }

}


