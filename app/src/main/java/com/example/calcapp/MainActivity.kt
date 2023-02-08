package com.example.calcapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calcapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //line 1
        binding.btnAllClear.setOnClickListener {
            onAllClearClick()
        }

        binding.btnClear.setOnClickListener {
            onClearClick()
        }

        binding.btnPercent.setOnClickListener {
            onPercentClick()
        }

        binding.btnDivide.setOnClickListener {
            onOperatorClick(binding.btnDivide)
        }

        //line 2
        binding.btn7.setOnClickListener {
            onDigitClick(binding.btn7)
        }

        binding.btn8.setOnClickListener {
            onDigitClick(binding.btn8)
        }

        binding.btn9.setOnClickListener {
            onDigitClick(binding.btn9)
        }

        binding.btnMultiply.setOnClickListener {
            onOperatorClick(binding.btnMultiply)
        }

        //line 3
        binding.btn4.setOnClickListener {
            onDigitClick(binding.btn4)
        }

        binding.btn5.setOnClickListener {
            onDigitClick(binding.btn5)
        }

        binding.btn6.setOnClickListener {
            onDigitClick(binding.btn6)
        }

        binding.btnSubtract.setOnClickListener {
            onOperatorClick(binding.btnSubtract)
        }

        //line 4
        binding.btn1.setOnClickListener {
            onDigitClick(binding.btn1)
        }

        binding.btn2.setOnClickListener {
            onDigitClick(binding.btn2)
        }

        binding.btn3.setOnClickListener {
            onDigitClick(binding.btn3)
        }

        binding.btnAdd.setOnClickListener {
            onOperatorClick(binding.btnAdd)
        }

        //line 5
        binding.btnBackspace.setOnClickListener {
            onBackspaceClick()
        }

        binding.btn0.setOnClickListener {
            onDigitClick(binding.btn0)
        }

        binding.btnDot.setOnClickListener {
            onDotClick()
        }

        binding.btnEqual.setOnClickListener {
            onEqualClick()
        }
    }

    private fun onDotClick() {
        binding.tvData.append(".")
        lastDot = true
    }

    //correct?
    private fun onPercentClick() {
        binding.tvData.append("/100")
        onEqual()
    }

    private fun onAllClearClick() {
        binding.tvData.text = ""
        binding.tvResult.text = ""
        stateError = false
        lastNumeric = false
        lastDot = false
        flag = false
    }

    private fun onClearClick() {
        binding.tvData.text = ""
        stateError = false
        lastNumeric = false
        lastDot = false
        flag = false
    }

    private fun onDigitClick(view: View) {
        if (stateError) {
            binding.tvData.text = (view as Button).text
            stateError = false
        } else binding.tvData.append((view as Button).text)

        lastNumeric = true
        onEqual()
    }

    private fun onOperatorClick(view: View) {
        if (lastNumeric && !stateError) {
            binding.tvData.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
            flag = true
        }
    }

    private fun onBackspaceClick() {
        binding.tvData.text = binding.tvData.text.dropLast(1)
        try {
            if (binding.tvData.text.last().isDigit()) onEqual()
        } catch (ex: java.lang.Exception) {
            Log.e("MyLog", "${getString(R.string.error)}: $ex")
            binding.tvResult.text = getString(R.string.error)
        }
    }

    private fun onEqualClick() {
        onEqual()
        //remove the equal sign from the beginning of the string if no error in result textview
        if (binding.tvResult.text != getString(R.string.error)) binding.tvData.text =
            binding.tvResult.text.toString().drop(1)
    }

    @SuppressLint("SetTextI18n")
    private fun onEqual() {
        if (lastNumeric && !stateError && binding.tvData.text.last().isDigit()) {
            try {
                val result = ExpressionBuilder(binding.tvData.text.toString()).build().evaluate()
                if (flag)
                    if (result != result.roundToInt().toDouble()) binding.tvResult.text = "=$result"
                    //discard zeros from an integer
                    else binding.tvResult.text = "=%.0f".format(result.toFloat())
            } catch (ex: java.lang.ArithmeticException) {
                Log.e("MyLog", "${getString(R.string.error)}: $ex")
                binding.tvResult.text = getString(R.string.error)
                stateError = false
                lastDot = false
                lastNumeric = false
            }
        }
    }
}