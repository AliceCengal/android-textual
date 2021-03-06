package com.cengallut.textualapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.{ListView, ArrayAdapter, AdapterView}

class Main extends Activity with AdapterView.OnItemClickListener {

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)

    val adapter = new ArrayAdapter[String](
      this, android.R.layout.simple_list_item_1,
      Array("Demo 1", "Demo 2", "Demo 3", "Demo 4", "Minesweeper"))

    val list = new ListView(this)
    list.setAdapter(adapter)
    list.setOnItemClickListener(this)
    setContentView(list)
  }

  override def onItemClick(
      parent:   AdapterView[_],
      view:     View,
      position: Int,
      id:       Long): Unit = {

    position match {
      case 0 => startActivity(new Intent(this, classOf[Demo1]))
      case 1 => startActivity(new Intent(this, classOf[Demo2]))
      case 2 => startActivity(new Intent(this, classOf[Demo3]))
      case 3 => startActivity(new Intent(this, classOf[Demo4]))
      case 4 => startActivity(new Intent(this, classOf[Minesweeper]))
    }

  }
}