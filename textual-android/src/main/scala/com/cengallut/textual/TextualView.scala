package com.cengallut.textual

import android.content.Context
import android.graphics._
import com.cengallut.textual.TextualView.BufferStateListener
import com.cengallut.textual.aside.Sugar._
import com.cengallut.textual.core.{TextCursor, FilterMap, Cursor, CharGrid}

/** Displays an array of characters as a grid throughout its view window. */
class TextualView private (context: Context, btl: BufferStateListener)
    extends android.view.View(context) {

  private var textSize_ = 48f

  def textSize(s: Float): TextualView = {
    textSize_ = s

    this
  }

  private var textColor_ = Color.WHITE

  def textColor(c: Int) = {
    textColor_ = c
    this
  }

  private var background_ = Color.BLACK

  def background(c: Int) = {
    background_ = c
    this
  }

  private val bufferStateListener = btl

  private[textual] var buffer = CharGrid.zero

  private[textual] var filterMap = FilterMap.identity

  lazy val cursor: TextCursor = Cursor.text(buffer)

  /** Holds the information needed to draw characters onto the canvas. This object is used to
    * calculate the individual sizes of each character, and so determines how big of a buffer is
    * needed to fill a screen. */
  private lazy val paint = {
    val p = new Paint
    p.setTextSize(textSize_)
    p.setColor(textColor_)
    val mono = Typeface.createFromAsset(context.getAssets, "Everson Mono.ttf")
    p.setTypeface(mono)
    p
  }

  /** Calculate the maximum bounding box for a character when it is drawn on a canvas, using
    * the paint object created previously. This dimension includes ascenders and descenders. */
  private lazy val charDimension = {
    val rect = new Rect
    paint.getTextBounds(Array('j', 'k'), 0, 2, rect)
    val charHeight = rect.height()
    paint.getTextBounds(Array('m'), 0, 1, rect)
    val charWidth = rect.width()
    (charWidth, charHeight)
  }

  private lazy val gridUpdateListener = new CharGrid.UpdateListener {
    override def onGridUpdated(): Unit = invalidate()
  }

  override /* android.view.View */
  protected def onSizeChanged(w: Int, h: Int, ow: Int, oh: Int): Unit = {
    buffer = CharGrid.ofDim(w / charDimension.x, h / charDimension.y)
    filterMap = new FilterMap {
      override def filter(x: Int, y: Int): Boolean = true
      override def map(x: Int, y: Int): (Int, Int) =
        (x * buffer.width / w,
          y * buffer.height / h)
    }

    buffer.setUpdateListener(gridUpdateListener)
    bufferStateListener.onBufferReady(buffer)
  }

  override /* android.view.View */
  protected def onDraw(c: Canvas): Unit = {
    c.drawColor(background_)

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

  def create(ctx: Context with BufferStateListener): TextualView =
    new TextualView(ctx, ctx)

  def create(ctx: Context, listener: BufferStateListener): TextualView =
    new TextualView(ctx, listener)

}


