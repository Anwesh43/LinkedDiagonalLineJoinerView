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

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()


