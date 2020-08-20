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
val scGap : Float = 0.02f / (circles * 2)
val strokeFactor : Float = 90f
val sizeFactor : Float = 12.2f
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawJoiner(i : Int, sf : Float, xGap : Float, yGap : Float, size : Float, paint : Paint) {
    val sfi : Float = sf.divideScale(i, circles)
    val sfi1 = sfi.divideScale(0, 2)
    val sfi2 = sfi.divideScale(1, 2)
    drawCircle(xGap, -yGap, size * sfi1, paint)
    drawLine(0f, 0f, xGap * sfi2, -yGap * sfi, paint)
}

fun Canvas.drawDiagonalLineJoiner(scale : Float, w : Float, h : Float, paint : Paint) {
    val sf : Float = scale.sinify()
    val xGap : Float = w / circles
    val yGap : Float = h / circles
    val size : Float = Math.min(w, h) / sizeFactor
    for (j in 0..(circles - 1)) {
        save()
        translate(w + xGap * j, h - yGap * j)
        drawJoiner(j, sf, xGap, yGap, size, paint)
        restore()
    }
}

fun Canvas.drawNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawDiagonalLineJoiner(scale, w, h, paint)
}

