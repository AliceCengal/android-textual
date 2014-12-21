package com.cengallut.textual.aside

object Sugar {

  implicit class Dim(val xy: (Int,Int)) extends AnyVal {

    def x: Int = xy._1

    def y: Int = xy._2

  }

}