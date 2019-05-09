package com.heads.thinking.mathapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.heads.thinking.mathapp.ViewModels.IntegralsViewModel
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.fragment_integrals.*
import java.math.BigDecimal
import java.math.RoundingMode

class IntegralsFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel : IntegralsViewModel
    private var listener: OnFragmentListener? = null

    fun calculate() {
        try {
            val expr: Expression = ExpressionBuilder(functionET.text.toString()).variable("x").build()
            val left = leftET.text.toString().toDouble()
            val right = rightET.text.toString().toDouble()
            val pointsCount = pointsCountET.text.toString().toInt()
            val trapezoid = BigDecimal(Methods.integralTrapesoid(expr, left, right, pointsCount))
                    .setScale(5, RoundingMode.CEILING).toDouble()
            val simpson = BigDecimal(Methods.integralSimpson(expr, left, right, pointsCount))
                    .setScale(5, RoundingMode.CEILING).toDouble()
            history.text = "История:\nФункция: ${functionET.text.toString()}\nот $left до $right по $pointsCount точкам" +
                    "\nФормула трапеций: ${trapezoid}\nФормула Симпсона: ${simpson}\n" + history.text.toString().substring(8)
        } catch (exc : IllegalArgumentException) {
            Toast.makeText(this.context, "Проверьте функцию", Toast.LENGTH_LONG).show()
        } catch (exc : NumberFormatException) {
            Toast.makeText(this.context, "Проверьте все поля", Toast.LENGTH_LONG).show()
        } catch (exc : ArithmeticException) {
            Toast.makeText(this.context, "Деление на ноль. Поменяйте интервал или функцию", Toast.LENGTH_LONG).show()
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this.activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_integrals, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        history.movementMethod = ScrollingMovementMethod();
        calculateBtn.setOnClickListener(this)
        back.setOnClickListener(this)
        viewModel = ViewModelProviders.of(this).get(IntegralsViewModel::class.java)
        if(viewModel.history != "") {
            functionET.setText(viewModel.function)
            leftET.setText(viewModel.left)
            rightET.setText(viewModel.right)
            pointsCountET.setText(viewModel.pointsCount)
            history.text = viewModel.history
        }
    }

    override fun onPause() {
        viewModel.pointsCount = pointsCountET.text.toString()
        viewModel.history = history.text.toString()
        viewModel.right = rightET.text.toString()
        viewModel.left = leftET.text.toString()
        viewModel.function = functionET.text.toString()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun newInstance() = IntegralsFragment()
    }
}
