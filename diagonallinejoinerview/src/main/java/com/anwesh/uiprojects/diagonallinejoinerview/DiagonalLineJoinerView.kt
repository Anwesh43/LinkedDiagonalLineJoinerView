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

class DiagonalLineJoinerView(ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class DLJNode(var i : Int, val state : State = State()) {

        private var next : DLJNode? = null
        private var prev : DLJNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = DLJNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : DLJNode {
            var curr : DLJNode? = next
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class DiagonalLineJoiner(var i : Int) {

        private var curr : DLJNode = DLJNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : DiagonalLineJoinerView) {

        private val dlj : DiagonalLineJoiner = DiagonalLineJoiner(0)
        private val animator : Animator = Animator(view)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            dlj.draw(canvas, paint)
            animator.animate {
                dlj.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            dlj.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : DiagonalLineJoinerView {
            val view : DiagonalLineJoinerView = DiagonalLineJoinerView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
