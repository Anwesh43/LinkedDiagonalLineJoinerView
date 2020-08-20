package com.anwesh.uiprojects.diagonallinejoinerview

/**
 * Created by anweshmishra on 21/08/20.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val colors : Array<String> = arrayOf("#BE0312", "#12BEAB", "#23FF23", "#2323FF", "#AC23AC")
val circles : Int = 4
val parts : Int = circles + 1
val scGap : Float = 0.02f /  parts
val strokeFactor : Float = 90f
val sizeFactor : Float = 12.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")


