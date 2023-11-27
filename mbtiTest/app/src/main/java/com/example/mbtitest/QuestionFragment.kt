package com.example.mbtitest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class QuestionFragment : Fragment() {

    private  var questionType: Int = 0

    private val questionTitle = listOf(
        R.string.question1_title,
        R.string.question2_title,
        R.string.question3_title,
        R.string.question4_title
    )

    private val questionTexts = listOf(
        listOf(R.string.question1_1,R.string.question1_2,R.string.question1_3),
        listOf(R.string.question2_1,R.string.question2_2,R.string.question2_3),
        listOf(R.string.question3_1,R.string.question3_2,R.string.question3_3),
        listOf(R.string.question4_1,R.string.question4_2,R.string.question4_3)
    )

    private val questionAnswers = listOf(
        listOf(
            listOf(R.string.question1_1_answer1, R.string.question1_1_answer2),
            listOf(R.string.question1_2_answer1, R.string.question1_2_answer2),
            listOf(R.string.question1_3_answer1, R.string.question1_3_answer2)
        ),

        listOf(
            listOf(R.string.question2_1_answer1, R.string.question2_1_answer2),
            listOf(R.string.question2_2_answer1, R.string.question2_2_answer2),
            listOf(R.string.question2_3_answer1, R.string.question2_3_answer2)
        ),

        listOf(
            listOf(R.string.question3_1_answer1, R.string.question3_1_answer2),
            listOf(R.string.question3_2_answer1, R.string.question3_2_answer2),
            listOf(R.string.question3_3_answer1, R.string.question3_3_answer2)
        ),

        listOf(
            listOf(R.string.question4_1_answer1, R.string.question4_1_answer2),
            listOf(R.string.question4_2_answer1, R.string.question4_2_answer2),
            listOf(R.string.question4_3_answer1, R.string.question4_3_answer2)
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //몇번 화면인지 받는것
        arguments?. let {
            questionType = it.getInt(ARG_QUESTION_TYPE)
        }
    }

    //레이아웃 다음 클릭할 때 마다 그 페이지번호에 맞는 텍스트로 변경하게 불러오는 코드
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //inflate한다 = 레이아웃으로 만든 xml 코드를 가져온다 (R.layout.만들어놓은레이아웃xml, 컨테이너에 넣어서, 어태치false)
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        val title : TextView = view.findViewById(R.id.tv_question_title)
        title.text = getString(questionTitle[questionType])

        val questionTextView = listOf<TextView>(
            view.findViewById(R.id.tv_question_1),
            view.findViewById(R.id.tv_question_2),
            view.findViewById(R.id.tv_question_3),
        )

        val answerRadioGroup = listOf<RadioGroup>(
            view.findViewById(R.id.rg_answer_1),
            view.findViewById(R.id.rg_answer_2),
            view.findViewById(R.id.rg_answer_3)
        )

        //선언한 val 들을 반목문으로 뿌리기
        for (i in questionTextView.indices) {
            questionTextView[i].text = getString(questionTexts[questionType][i])

            val radioButton1 = answerRadioGroup[i].getChildAt(0) as RadioButton
            val radioButton2 = answerRadioGroup[i].getChildAt(1) as RadioButton

            radioButton1.text = getString(questionAnswers[questionType][i][0])
            radioButton2.text = getString(questionAnswers[questionType][i][1])


        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val answerRadioGroup = listOf<RadioGroup>(
            view.findViewById(R.id.rg_answer_1),
            view.findViewById(R.id.rg_answer_2),
            view.findViewById(R.id.rg_answer_3)
        )

        val btn_next : Button = view.findViewById(R.id.btn_next)

        btn_next.setOnClickListener {

            //-1체크안되어있을때를 뜻함.
            val isAllAnswered = answerRadioGroup.all { it.checkedRadioButtonId != -1 }

            if(isAllAnswered) {
                val responses = answerRadioGroup.map {radioGroup ->
                    //first만 해도 되는 이유는 지금 라디오버튼이 두개 밖에 없으니까 하나만 있나없나를 확인해도 나머지값을 알수 있음
                    val firstRadioButton = radioGroup.getChildAt(0 ) as RadioButton
                    //responses에 첫번째라디오버튼 확인해서 되어있으면 1을 넣고 아니면 2를 넣어라
                    if (firstRadioButton.isChecked) 1 else 2
                }

                (activity as? TestActivity)?.questionnaireResults?.addResponses(responses)
                (activity as? TestActivity)?.moveToNextQuestion()

            } else {
                Toast.makeText(context, "모든 질문에 답해주세요", Toast.LENGTH_SHORT).show()
            }

        }
    }



    //생성자먼저만들고시작
    companion object{
        private const val ARG_QUESTION_TYPE = "questionType"

        //nextItem번호 받아주는곳
        fun newInstance(questionType : Int): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putInt(ARG_QUESTION_TYPE, questionType)
            fragment.arguments = args
            return  fragment
        }
    }

}