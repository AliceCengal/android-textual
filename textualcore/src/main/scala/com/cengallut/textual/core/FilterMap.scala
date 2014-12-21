package com.cengallut.textual.core

/** Just a filter and a map operation. Filter is used to crop out a subset of a
  * coordinate space. Map is used to transpose coordinates from one space to
  * another. Used in CharGrid. */
trait FilterMap {
  def filter(x: Int, y: Int): Boolean
  def map(x: Int, y: Int): (Int,Int)
}

object FilterMap {

  /** The filter always return true. The map just returns the argument. */
  val identity: FilterMap = new FilterMap {
    override def filter(x: Int, y: Int): Boolean = true
    override def map(x: Int, y: Int): (Int, Int) = (x, y)
  }

}