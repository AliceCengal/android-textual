package com.cengallut.textual.core

import com.cengallut.textual.core.CharGrid.{GridView, BasicGrid}

abstract class GridCreational extends GridFactory
    with GridPrototype with GridJavaPrototype {
}

trait GridFactory {

  /** Returns a Grid of dimension 0x0. */
  def zero: CharGrid = ofDim(0, 0)

  /** Returns a Grid of the specified. */
  def ofDim(xDim: Int, yDim: Int): CharGrid =
    new BasicGrid(xDim, yDim)

}

trait GridPrototype {

  /** Returns a view into the current Grid. if g is a 6x5 grid:
    *
    *     ######
    *     ######
    *     ######
    *     ######
    *     ######
    *
    * Then calling view(g, 1, 2, 3, 2) returns a view to
    * the region marked as 'O'
    *
    *     ######
    *     ######
    *     #OOO##
    *     #OOO##
    *     ######
    *
    * The returned Grid shares the underlying representation
    * with the original Grid. */
  def view(g: CharGrid, left: Int, top: Int, width: Int, height: Int): CharGrid =
    new GridView(g, (left, top, width, height))

  /** Returns a view into the current grid with the given distance from
    * the edges, in the order (left, top, right, bottom)
    *
    * Calling shrink(g, 1, 2, 1, 1) yields the following region marked as 'O'
    *
    *     ######
    *     ######
    *     #OOOO#
    *     #OOOO#
    *     ######
    *
    * The returned Grid view shares the underlying representation
    * with the original Grid. */
  def shrink(g: CharGrid, left: Int, top: Int, right: Int, bottom: Int): CharGrid = {
    val width = g.width - right - left
    val height = g.height - bottom - top
    new GridView(g, (left, top, width, height))
  }

  /** Returns a view into the current grid with the given distance from
    * each sides. */
  def shrink(g: CharGrid, offset: Int): CharGrid =
    shrink(g, offset, offset, offset, offset)

  /** Creates a new Buffer with a separate underlying representation
    * but the same content and same dimension. Any modification made to the new Buffer
    * does not affect the original, and vice versa. */
  def copy(g: CharGrid): CharGrid = {
    val bNew = new BasicGrid(g.width, g.height)
    for {
      x <- 0 until g.width
      y <- 0 until g.height
    } bNew.setChar(x, y, g.charAt(x, y))
    bNew
  }

}

object GridPrototype extends GridPrototype {

  /** Returns a pair (n1,n2) such that n1 + n2 == l  and n1/l == ratio */
  def partitionLength(l: Int, ratio: Double): (Int,Int) = {
    val first = (l * ratio).ceil.toInt
    ( first , l - first )
  }

}

trait GridJavaPrototype extends GridPrototype {
  import GridPrototype.partitionLength

  def topBisect(g: CharGrid): CharGrid = {
    val (_, topOffset) = partitionLength(g.height, 0.5)
    shrink(g, 0, 0, 0, topOffset)
  }

  def bottomBisect(g: CharGrid): CharGrid = {
    val (bottomOffset, _) = partitionLength(g.height, 0.5)
    shrink(g, 0, bottomOffset, 0, 0)
  }

  def leftBisect(g: CharGrid): CharGrid = {
    val (_, leftOffset) = partitionLength(g.width, 0.5)
    shrink(g, 0, 0, leftOffset, 0)

  }

  def rightBisect(g: CharGrid): CharGrid = {
    val (rightOffset, _) = partitionLength(g.width, 0.5)
    shrink(g, rightOffset, 0, 0, 0)
  }

  /** Returns two Buffers. */
  def leftSplit(g: CharGrid, ratio: Double): CharGrid = {
    val (_, leftOffset) = partitionLength(g.height, ratio)
    shrink(g, 0, 0, leftOffset, 0)
  }

  /** Returns two Buffers. */
  def rightSplit(g: CharGrid, ratio: Double): CharGrid = {
    val (rightOffset, _) = partitionLength(g.height, ratio)
    shrink(g, rightOffset, 0, 0, 0)
  }

  /** Returns two Buffers. */
  def topSplit(g: CharGrid, ratio: Double): CharGrid = {
    val (_, topOffset) = partitionLength(g.height, ratio)
    shrink(g, 0, 0, 0, topOffset)
  }

  /** Returns two Buffers. */
  def bottomSplit(g: CharGrid, ratio: Double): CharGrid = {
    val (bottomOffset, _) = partitionLength(g.height, ratio)
    shrink(g, 0, bottomOffset, 0, 0)
  }

}

/** Wrapper class for adding extension methods for cloning and sub-viewing
  * a CharGrid. */
class PrototypeWrapper(g: CharGrid) {
  import GridPrototype.partitionLength

  def view(left: Int, top: Int, width: Int, height: Int): CharGrid =
    GridPrototype.view(g, left, top, width, height)

  def shrink(left: Int, top: Int, right: Int, bottom: Int): CharGrid =
    GridPrototype.shrink(g, left, top, right, bottom)

  def shrink(offset: Int): CharGrid =
    GridPrototype.shrink(g, offset)

  /** Returns two Grids which are views into the original Grid. */
  def verticalBisect: (CharGrid,CharGrid) = {
    val (rightOffset, leftOffset) = partitionLength(g.width, 0.5)
    val left = shrink(0, 0, leftOffset, 0)
    val right = shrink(rightOffset, 0, 0, 0)
    (left, right)
  }

  /** Returns two Grids which are views into the original Grid. */
  def horizontalBisect: (CharGrid,CharGrid) = {
    val (bottomOffset, topOffset) = partitionLength(g.height, 0.5)
    val top = shrink(0, 0, 0, topOffset)
    val bottom = shrink(0, bottomOffset, 0, 0)
    (top, bottom)
  }

  /** Returns two Grids which are views into the original Grid. */
  def verticalSplit(ratio: Double): (CharGrid,CharGrid) = {
    val (rightOffset, leftOffset) = partitionLength(g.width, ratio)
    val left = shrink(0, 0, leftOffset, 0)
    val right = shrink(rightOffset, 0, 0, 0)
    (left, right)
  }

  /** Returns two Grids which are views into the original Grid. */
  def horizontalSplit(ratio: Double): (CharGrid,CharGrid) = {
    val (bottomOffset, topOffset) = partitionLength(g.height, ratio)
    val top = shrink(0, 0, 0, topOffset)
    val bottom = shrink(0, bottomOffset, 0, 0)
    (top, bottom)
  }

}