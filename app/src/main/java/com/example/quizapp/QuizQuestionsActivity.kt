package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var quizBinding: ActivityQuizQuestionsBinding

    private var mCurrentPosition: Int = 1

    private var mQuestionsList: ArrayList<Question>? = null

    private var mSelectedOptionPosition: Int = 0

    private var mCorrectAnswers = 0

    private var mUserName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(quizBinding.root)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        quizBinding.tvOptionOne.setOnClickListener(this)
        quizBinding.tvOptionTwo.setOnClickListener(this)
        quizBinding.tvOptionThree.setOnClickListener(this)
        quizBinding.tvOptionFour.setOnClickListener(this)

        quizBinding.btnSubmit.setOnClickListener(this)

    }

    private fun setQuestion() {

        val question = mQuestionsList!!.get(mCurrentPosition - 1)

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {

            quizBinding.btnSubmit.text = "FInish"

        } else {

            quizBinding.btnSubmit.text = "Submit"

        }

        quizBinding.progressBar.progress = mCurrentPosition

        quizBinding.tvProgress.text = "$mCurrentPosition" + "/" + quizBinding.progressBar.max

        quizBinding.tvQuestion.text = question!!.question

        quizBinding.ivImage.setImageResource(question.image)

        quizBinding.tvOptionOne.text = question.optionOne
        quizBinding.tvOptionTwo.text = question.optionTwo
        quizBinding.tvOptionThree.text = question.optionThree
        quizBinding.tvOptionFour.text = question.optionFour

    }

    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()

        options.add(0, quizBinding.tvOptionOne)
        options.add(1, quizBinding.tvOptionTwo)
        options.add(2, quizBinding.tvOptionThree)
        options.add(3, quizBinding.tvOptionFour)

        for (option in options) {

            option.setTextColor(Color.parseColor("#7a8089"))

            option.typeface = Typeface.DEFAULT

            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)

        }

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tv_option_one -> {

                selectedOptionView(quizBinding.tvOptionOne, 1)

            }

            R.id.tv_option_two -> {

                selectedOptionView(quizBinding.tvOptionTwo, 2)

            }

            R.id.tv_option_three -> {

                selectedOptionView(quizBinding.tvOptionThree, 3)

            }

            R.id.tv_option_four -> {

                selectedOptionView(quizBinding.tvOptionFour, 4)

            }

            R.id.btn_submit -> {

                if (mSelectedOptionPosition == 0) {

                    mCurrentPosition++

                    when {

                        mCurrentPosition <= mQuestionsList!!.size -> {

                            setQuestion()

                        }
                        else -> {

                            Toast.makeText(this, "You have successfully completed the quiz", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWER, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }

                    }

                } else {

                    val question = mQuestionsList?.get(mCurrentPosition - 1)

                    if (question!!.correctAnswer != mSelectedOptionPosition) {

                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)

                    } else {

                        mCorrectAnswers++

                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList!!.size) {

                        quizBinding.btnSubmit.text = "FInish"

                    } else {

                        quizBinding.btnSubmit.text = "Go to Next Question"

                    }

                    mSelectedOptionPosition = 0

                }

            }

        }

    }

    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {

            1 -> {

                quizBinding.tvOptionOne.background = ContextCompat.getDrawable(this, drawableView)

            }

            2 -> {

                quizBinding.tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView)

            }

            3 -> {

                quizBinding.tvOptionThree.background = ContextCompat.getDrawable(this, drawableView)

            }

            4 -> {

                quizBinding.tvOptionFour.background = ContextCompat.getDrawable(this, drawableView)

            }

        }

    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNumber

        tv.setTextColor(Color.parseColor("#363a43"))

        tv.setTypeface(tv.typeface, Typeface.BOLD)

        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)

    }
}