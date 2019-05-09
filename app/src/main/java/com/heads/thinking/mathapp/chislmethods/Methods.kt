package com.heads.thinking.mathapp

import net.objecthunter.exp4j.Expression

class Methods {
    companion object {
        fun exprToLambda1(expr : Expression) = { x : Double ->
            expr.setVariable("x", x).evaluate()
        }

        fun exprToLambda2(expr : Expression) = { x : Double, y : Double ->
            expr.setVariable("x", x).setVariable("y", y).evaluate()
        }

        fun integralTrapesoid(expr : Expression, left : Double, right : Double, pointsCount : Int) : Double {
            val function = exprToLambda1(expr)
            val step = (right - left) / pointsCount
            var result = (function(left) + function(right)) / 2
            for(i in 1 until pointsCount)
                result += function(left + step * i)
            return result * step
        }

        fun integralSimpson(expr : Expression, left : Double, right : Double, pointsCount : Int) : Double {
            val function = exprToLambda1(expr)
            val step = (right - left) / pointsCount
            var result = function(left) + function(right) + 4 * function(left + step*(pointsCount-1) + step/2)
            for(i in 1 until pointsCount)
                result += 4 * function(left + step*(i-1) + step/2) + 2 * function(left + step * i)
            return result * (step / 6)
        }

        fun diffurPredictionAndCorrection(expr: Expression, startPoint : Pair<Double, Double>, step : Double, stepsCount : Int) : DoubleArray {
            if(stepsCount < 1) return DoubleArray(0)
            val function = exprToLambda2(expr)
            val result : DoubleArray  = DoubleArray(stepsCount + 1)
            result[0] = startPoint.second
            var x : Double = startPoint.first
            for(i in 1..stepsCount) {
                val temp = result[i-1] + step*step*function(x, result[i-1])
                result[i] = result[i-1] + (step - 0.5) * function(x, result[i-1]) + 0.5*function(x + step*step, temp)
                x += step
            }
            return result
        }

        fun diffurRungaKutta3(expr: Expression, startPoint : Pair<Double, Double>, step : Double, stepsCount : Int) : DoubleArray {
            if(stepsCount < 1) return DoubleArray(0)
            val function = exprToLambda2(expr)
            val result : DoubleArray  = DoubleArray(stepsCount + 1)
            result[0] = startPoint.second
            var x = startPoint.first
            for(i in 1..stepsCount) {
                val f0 = step * function(x, result[i-1])
                val f1 = step * function(x + step/2, result[i-1] + f0/2)
                val f2 = step * function(x + step, result[i-1] - f0 + 2*f1)
                result[i] = result[i-1] + (f0 + 4*f1 + f2)/6
                x += step
            }
            return result
        }

        fun diffurRungaKutta4(expr: Expression, startPoint : Pair<Double, Double>, step : Double, stepsCount : Int) : DoubleArray {
            if(stepsCount < 1) return DoubleArray(0)
            val function = exprToLambda2(expr)
            val result : DoubleArray  = DoubleArray(stepsCount + 1)
            result[0] = startPoint.second
            var x = startPoint.first
            for(i in 1..stepsCount) {
                val f0 = step*function(x, result[i-1])
                val f1 = step*function(x + step/2, result[i-1] + f0/2)
                val f2 = step*function(x + step/2, result[i-1] + f1/2)
                val f3 = step*function(x + step, result[i-1] + f2)
                result[i] = result[i-1] + (f0 + 2*f1 + 2*f2 + f3)/6
                x += step
            }
            return result
        }
    }
}