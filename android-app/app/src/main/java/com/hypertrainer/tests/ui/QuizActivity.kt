package com.hypertrainer.tests.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.hypertrainer.tests.R
import com.hypertrainer.tests.data.PreferencesManager
import com.hypertrainer.tests.data.TestRepository
import com.hypertrainer.tests.model.IndexEntry
import com.hypertrainer.tests.model.Question
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private lateinit var repository: TestRepository
    private lateinit var prefsManager: PreferencesManager
    
    private lateinit var rvQuestions: RecyclerView
    private lateinit var tvQuestionNumber: TextView
    private lateinit var tvQuestionText: TextView
    private lateinit var rbOption1: RadioButton
    private lateinit var rbOption2: RadioButton
    private lateinit var rbOption3: RadioButton
    private lateinit var rbOption4: RadioButton
    private lateinit var btnNext: MaterialButton
    private lateinit var btnFinish: MaterialButton
    
    private var questions: List<Question> = emptyList()
    private var currentIndex = 0
    private var mistakesCount = 0
    private var isExamMode = false
    private var testId: String = ""
    private var madeMistakesOnCurrentQuestion = false
    
    companion object {
        const val EXTRA_TEST = "test"
        const val EXTRA_MODE = "mode" // "exam", "training", "mistakes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        
        repository = TestRepository()
        prefsManager = PreferencesManager(this)
        
        initViews()
        
        val test = intent.getParcelableExtra<IndexEntry>(EXTRA_TEST)
        val mode = intent.getStringExtra(EXTRA_MODE) ?: "training"
        
        if (test != null) {
            testId = test.id
            isExamMode = (mode == "exam")
            
            lifecycleScope.launch {
                when (mode) {
                    "exam" -> loadExamQuestions(test.filename)
                    "training" -> loadAllQuestions(test.filename)
                    "mistakes" -> loadMistakesQuestions()
                }
            }
        } else {
            Toast.makeText(this, "Ошибка: тест не найден", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber)
        tvQuestionText = findViewById(R.id.tvQuestionText)
        rbOption1 = findViewById(R.id.rbOption1)
        rbOption2 = findViewById(R.id.rbOption2)
        rbOption3 = findViewById(R.id.rbOption3)
        rbOption4 = findViewById(R.id.rbOption4)
        btnNext = findViewById(R.id.btnNext)
        btnFinish = findViewById(R.id.btnFinish)
        
        btnNext.setOnClickListener {
            checkAnswerAndNext()
        }
        
        btnFinish.setOnClickListener {
            finishQuiz()
        }
    }
    
    private suspend fun loadExamQuestions(filename: String) {
        val result = repository.getTestContent(filename)
        result.onSuccess { testContent ->
            questions = testContent.questions.shuffled().take(10)
            if (questions.isEmpty()) {
                Toast.makeText(this, "Нет вопросов для экзамена", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                showQuestion(0)
            }
        }.onFailure {
            Toast.makeText(this, "Ошибка загрузки: ${it.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private suspend fun loadAllQuestions(filename: String) {
        val result = repository.getTestContent(filename)
        result.onSuccess { testContent ->
            questions = testContent.questions
            if (questions.isEmpty()) {
                Toast.makeText(this, "Нет вопросов", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                showQuestion(0)
            }
        }.onFailure {
            Toast.makeText(this, "Ошибка загрузки: ${it.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private suspend fun loadMistakesQuestions() {
        val mistakes = prefsManager.getMistakes(testId)
        if (mistakes.isEmpty()) {
            Toast.makeText(this, "Нет ошибок для работы", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            questions = mistakes
            showQuestion(0)
        }
    }
    
    private fun showQuestion(index: Int) {
        if (index >= questions.size) {
            finishQuiz()
            return
        }
        
        currentIndex = index
        madeMistakesOnCurrentQuestion = false
        val question = questions[index]
        
        tvQuestionNumber.text = "Вопрос ${index + 1} из ${questions.size}"
        tvQuestionText.text = question.question
        
        rbOption1.text = question.answers.getOrNull(0)?.answer ?: ""
        rbOption2.text = question.answers.getOrNull(1)?.answer ?: ""
        rbOption3.text = question.answers.getOrNull(2)?.answer ?: ""
        rbOption4.text = question.answers.getOrNull(3)?.answer ?: ""
        
        rbOption1.isChecked = false
        rbOption2.isChecked = false
        rbOption3.isChecked = false
        rbOption4.isChecked = false
        
        rbOption1.isEnabled = true
        rbOption2.isEnabled = true
        rbOption3.isEnabled = true
        rbOption4.isEnabled = true
        
        btnFinish.visibility = if (index == questions.size - 1) View.VISIBLE else View.GONE
        btnNext.text = if (index == questions.size - 1) "Завершить" else "Далее"
    }
    
    private fun checkAnswerAndNext() {
        val selectedId = when {
            rbOption1.isChecked -> 0
            rbOption2.isChecked -> 1
            rbOption3.isChecked -> 2
            rbOption4.isChecked -> 3
            else -> -1
        }
        
        if (selectedId == -1) {
            Toast.makeText(this, "Выберите вариант ответа", Toast.LENGTH_SHORT).show()
            return
        }
        
        val question = questions[currentIndex]
        val isCorrect = question.isAnswerCorrect(selectedId)
        
        if (!isCorrect) {
            madeMistakesOnCurrentQuestion = true
            
            if (isExamMode) {
                mistakesCount++
                if (mistakesCount > 2) {
                    Toast.makeText(this, "Экзамен провален! Допущено более 2 ошибок", Toast.LENGTH_LONG).show()
                    finishQuiz()
                    return
                }
            }
            
            // Сохраняем ошибку
            prefsManager.addMistake(testId, question)
            
            // Показываем правильный ответ
            showCorrectAnswer(selectedId)
        } else {
            // Если режим работы над ошибками и ответили правильно - удаляем из ошибок
            if (intent.getStringExtra(EXTRA_MODE) == "mistakes") {
                prefsManager.removeMistake(testId, question)
            }
            currentIndex++
            showQuestion(currentIndex)
        }
    }
    
    private fun showCorrectAnswer(selectedId: Int) {
        rbOption1.isEnabled = false
        rbOption2.isEnabled = false
        rbOption3.isEnabled = false
        rbOption4.isEnabled = false
        
        val correctIndex = questions[currentIndex].getCorrectAnswerIndex()
        when (correctIndex) {
            0 -> rbOption1.isChecked = true
            1 -> rbOption2.isChecked = true
            2 -> rbOption3.isChecked = true
            3 -> rbOption4.isChecked = true
        }
        
        Toast.makeText(this, "Неправильно! Правильный ответ подсвечен", Toast.LENGTH_LONG).show()
        
        // Задержка перед следующим вопросом
        btnNext.postDelayed({
            if (!isExamMode || mistakesCount <= 2) {
                currentIndex++
                showQuestion(currentIndex)
            }
        }, 2000)
    }
    
    private fun finishQuiz() {
        val message = if (isExamMode) {
            if (mistakesCount <= 2) {
                "Экзамен сдан! Ошибок: $mistakesCount из 2 допустимых"
            } else {
                "Экзамен провален! Ошибок: $mistakesCount"
            }
        } else {
            "Тест завершён!"
        }
        
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }
}
