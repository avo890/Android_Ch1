package com.example.mbtitest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class TestActivity : AppCompatActivity() {

    private lateinit var  viewPager : ViewPager2

    val questionnaireResults = QuestionnaireResults()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        //응답 3개를 다 선택해서 다음을 눌러야지만 넘어갈수있도록 설정
        viewPager.isUserInputEnabled = false
    }

    fun moveToNextQuestion() {
        if(viewPager.currentItem==3) {

            val intent = Intent(this, ResultActivity::class.java)
            intent.putIntegerArrayListExtra("results", ArrayList(questionnaireResults.results))
            startActivity(intent)


        } else {
            val nextItem = viewPager.currentItem +1
            if(nextItem < viewPager.adapter?.itemCount ?: 0) {
                //다음페이지로 넘기기. (다음페이지번호, 페이지스크롤링되는 옵션true)
                viewPager.setCurrentItem(nextItem, true)
            }
        }
    }

}

class QuestionnaireResults {
    val results = mutableListOf<Int>()

    fun addResponses(response : List<Int>) {
        val mostFrequent = response.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
        //nullable일 때 ? 붙이기
        mostFrequent?.let {results.add(it)}
    }
}