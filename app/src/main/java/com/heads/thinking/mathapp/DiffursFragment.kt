package com.heads.thinking.mathapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.heads.thinking.mathapp.ViewModels.DiffursViewModel
import kotlinx.android.synthetic.main.fragment_diffurs.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode

class DiffursFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentListener? = null
    private var plotListener: PlotListener? = null
    private lateinit var viewModel : DiffursViewModel

    interface PlotListener {
        fun startGraphActivity(data : String, startPoint : String)
    }

    fun parseMethodsData(arr : DoubleArray) : String {
        var resultStr = "\n"
        for(i in 1 until arr.size) {
            val tmp : Double = BigDecimal(arr[i]).setScale(5, RoundingMode.CEILING).toDouble()
            resultStr += "$i) ${tmp}\n"
        }
        return resultStr
    }

    fun calculate() {
        try {
            val expr = ExpressionBuilder(functionET.text.toString()).variables("x", "y").build()
            val startPoint = Pair(startX.text.toString().toDouble(), startY.text.toString().toDouble())
            val step = 0.1
            val predAndCorr = Methods.diffurPredictionAndCorrection(expr, startPoint, step, 3)
            val rk3 = Methods.diffurRungaKutta3(expr, startPoint, step, 3)
            val rk4 = Methods.diffurRungaKutta4(expr, startPoint, step, 3)
            history.text = "История:\nФункция: ${functionET.text.toString()}\nf(${startPoint.first}) = ${startPoint.second}\n" +
                    "Прогноза и коррекции: " + parseMethodsData(predAndCorr) + "\n" +
                    "Рунге-Кутта 3: " + parseMethodsData(rk3) + "\n" +
                    "Рунге-Кутта 4: " + parseMethodsData(rk4) + "\n" + history.text.toString().substring(8)
        } catch (exc : IllegalArgumentException) {
            Toast.makeText(this.context, "Проверьте функцию", Toast.LENGTH_LONG).show()
        } catch (exc : NumberFormatException) {
            Toast.makeText(this.context, "Проверьте все поля", Toast.LENGTH_LONG).show()
        } catch (exc : ArithmeticException) {
            Toast.makeText(this.context, "Деление на ноль. Поменяйте начальные значение или функцию", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.calculateBtn -> {
                calculate()
            }
            R.id.back -> {
                listener?.changeFragment(MainFragment())
            }
            R.id.plot -> {
                try {
                    //check function
                    ExpressionBuilder(functionET.text.toString()).variables("x", "y").build()
                    //start activity
                    plotListener?.startGraphActivity(functionET.text.toString(),
                            startX.text.toString() + " " + startY.text.toString())
                } catch (exc : IllegalArgumentException) {
                    Toast.makeText(this.context, "Проверьте функцию", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this.activity as MainActivity
        plotListener = this.activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_diffurs, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        history.movementMethod = ScrollingMovementMethod();
        back.setOnClickListener(this)
        plot.setOnClickListener(this)
        calculateBtn.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(DiffursViewModel::class.java)
        if(viewModel.history != "") {
            history.text = viewModel.history
            functionET.setText(viewModel.function)
            startX.setText(viewModel.startX)
            startY.setText(viewModel.startY)
        }
    }

    override fun onPause() {
        viewModel.history = history.text.toString()
        viewModel.function = functionET.text.toString()
        viewModel.startX = startX.text.toString()
        viewModel.startY = startY.text.toString()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() = DiffursFragment()
    }
}
