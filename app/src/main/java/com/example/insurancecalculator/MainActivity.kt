package com.example.insurancecalculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.insurancecalculator.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateInsurance() }
    }

    /**метод подсчета суммы страховки с учетом скидки
    (в зависимости от количества лет, использования услуг страховой компании)*/
    fun calculateInsurance() {

        //Получаем текстовое значение из поля ввода
        val amountValueString = binding.insuranceEditText.text.toString()

        //Переводим текстовое значение в числовое с типом Double
        val amount = amountValueString.toDoubleOrNull()
        if (amount == null || amount == 0.0) {
            binding.amountInsuranceResult.text = "0.0"
            return
        }


        //выбираем скидку взависимости от выбора определенной радиокнопки
        val insuranceType = when (binding.discountOption.checkedRadioButtonId) {
            R.id.three_years -> 0.30
            R.id.two_years -> 0.20
            else -> 0.05
        }

        //подсчет итоговой суммы страховки с учетом выбранной скидки
        var insuranceCost = amount * insuranceType
        //включение и выключение округдения суммы итоговой суммы страховки
        if (binding.roundUpSwitch.isChecked) {
            insuranceCost = ceil(insuranceCost)
        }

        //выводим стоимость страховки
        displayInsuranceCost(insuranceCost)

    }

    /**
     * Format the tip amount according to the local currency and display it onscreen.
     * Example would be "Tip Amount: $10.00".
     */
    private fun displayInsuranceCost(insuranceCost: Double) {
        //определяем формат валюты для нашей сумы в зависимости от локали
        val formattedInsuranceCost = NumberFormat.getCurrencyInstance().format(insuranceCost)
        //находим эелемент вывода текста и передаем в него в виде стринги:
        //текст(Cost of insurance: %s) и
        binding.amountInsuranceResult.text =
            getString(R.string.amount_insurance_result, formattedInsuranceCost)
    }

    /**
     * Key listener for hiding the keyboard when the "Enter" button is tapped.
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}