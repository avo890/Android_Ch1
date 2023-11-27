package com.example.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    //초기화 및 레이아웃과 값 연결

    //lazy 사용한 늦은 초기화(호출시점 초기화) : 호출시점 최초 1회 초기화, val에만 사용가능, 선언과 동시에 초기화 필수
    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()


    //앱 시작 하는 건 여기부터
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numPick.minValue = 1
        numPick.maxValue = 45

        initAddButton()
        initRunButton()
        initClearButton()

    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            when {

                //예외 경우를 고려 하여 예외 방지 작성
                didRun -> showToast("초기화 후에 시도해주세요.")
                pickNumberSet.size >= 6 -> showToast("숫자는 최대 6개까지 선택할 수 있습니다.")
                //numpick.value : 내가 고른 값. = 같은 값
                pickNumberSet.contains(numPick.value) -> showToast("이미 선택된 숫자입니다.")

                //ㅇㅖ외가 없으니까 이제 공 6개 불러오기
                else -> {
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    textView.text = numPick.value.toString()

                    setNumBack(numPick.value, textView)
                    pickNumberSet.add(numPick.value)

                }
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach {it.isVisible = false}
            didRun = false
            numPick.value = 1
        }
    }

    private fun initRunButton() {

        runButton.setOnClickListener {
            val list = getRendom()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }

    }

    private fun getRendom() : List<Int> {
        val numbers = (1..45).filter { it !in pickNumberSet }

        //sorted로 정렬
        return (pickNumberSet + numbers.shuffled().take(6-pickNumberSet.size)).sorted()
        //take로 갯수(6-pickNumberSet.size) 만큼 가져오기.
        //가져온값들을 이미 pickNumberSet 한 숫자들과 +(합쳐서) return
    }

    private fun setNumBack(number: Int, textView: TextView) {
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}