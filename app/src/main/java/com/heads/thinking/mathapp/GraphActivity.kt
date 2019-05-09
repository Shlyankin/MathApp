package com.heads.thinking.mathapp

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_graph.*
import net.objecthunter.exp4j.ExpressionBuilder

class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        back.setOnClickListener {
            onBackPressed()
        }
        val function = intent.getStringExtra("function")
        val expr = ExpressionBuilder(function).variables("x", "y").build()
        val point = intent.getStringExtra("startPoint")
        val startX = point.substring(0, point.indexOf(' ')).toDouble()
        val startY = point.substring(point.indexOf(' ') + 1, point.length).toDouble()
        val startPoint = Pair(startX, startY)
        val step = 0.1
        val predAndCorr = Methods.diffurPredictionAndCorrection(expr, startPoint, step, 3)
        val rk3 = Methods.diffurRungaKutta3(expr, startPoint, step, 3)
        val rk4 = Methods.diffurRungaKutta4(expr, startPoint, step, 3)
        val x : DoubleArray = DoubleArray(4, {
            startX + step * it
        })
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(startX)
        graph.viewport.setMaxX(startX + step * 3)

        graph.viewport.isScalable = true
        graph.viewport.isScrollable = true
        graph.viewport.setScalableY(true)
        graph.viewport.setScrollableY(true)

        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP
        graph.legendRenderer.width = 400
        createSeries(Triple(x, predAndCorr, "Прогноза и корр./GREEN"))
        createSeries(Triple(x, rk3, "Рунге-Кутта 3/RED"))
        createSeries(Triple(x, rk4, "Рунге-Кутта 4/BLUE"))
    }

    fun createSeries(XandYandLegend : Triple<DoubleArray, DoubleArray, String>) {
        object : AsyncTask<Triple<DoubleArray, DoubleArray, String>, Unit, Pair<LineGraphSeries<DataPoint>, String>>() {
            override fun doInBackground(vararg argArray: Triple<DoubleArray, DoubleArray, String>): Pair<LineGraphSeries<DataPoint>, String> {
                val arg = argArray[0]
                val series = LineGraphSeries<DataPoint>(Array<DataPoint>(arg.first.size, {
                    DataPoint(arg.first[it], arg.second[it])
                }))
                return Pair(series, arg.third)
            }

            override fun onPostExecute(result: Pair<LineGraphSeries<DataPoint>, String>) {
                result.first.title =  result.second.substring(0, result.second.indexOf('/'))
                when(result.second.substring(result.second.indexOf('/') + 1, result.second.length)) {
                    "RED" -> result.first.color = Color.RED
                    "GREEN" -> result.first.color = Color.GREEN
                    "BLUE" -> result.first.color = Color.BLUE
                }
                graph.addSeries(result.first)
                super.onPostExecute(result)
            }
        }.execute(XandYandLegend)
    }
}