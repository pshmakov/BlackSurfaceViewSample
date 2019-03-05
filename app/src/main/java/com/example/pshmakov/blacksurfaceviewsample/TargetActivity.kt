package com.example.pshmakov.blacksurfaceviewsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup

class TargetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        addSurfaceView()
    }

    fun addSurfaceView() {
        val surfaceView = DrawingSurfaceView(this).apply {
            layoutParams = ViewGroup.MarginLayoutParams(500, 300).apply { bottomMargin = 100 }
        }
        findViewById<ViewGroup>(R.id.layout).addView(surfaceView)
    }


    internal inner class DrawingSurfaceView : SurfaceView {

        constructor(context: Context) : this(context, null)
        constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
            holder.addCallback(object : SurfaceHolder.Callback2 {

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    draw()
                }

                override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
                    draw()
                }

                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                }

            })
        }

        private var path: Path? = null

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = Color.WHITE
        }

        private val fillPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.rgb(100, 100, 255)
        }


        override fun onTouchEvent(event: MotionEvent): Boolean {
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    path = Path().also { it.moveTo(event.x, event.y) }
                }
                event.action == MotionEvent.ACTION_MOVE -> path?.lineTo(event.x, event.y)
                event.action == MotionEvent.ACTION_UP -> path?.lineTo(event.x, event.y)
            }

            draw()
            return true
        }

        fun draw(color: Int = Color.rgb(100, 100, 255)) {
            fillPaint.color = color
            val canvas = holder.lockCanvas()
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)
            path?.let { canvas?.drawPath(it, paint) }
            holder.unlockCanvasAndPost(canvas)
        }


    }
}
