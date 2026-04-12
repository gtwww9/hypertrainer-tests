package com.hypertrainer.tests.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repository: TestRepository
    private lateinit var prefsManager: PreferencesManager
    
    private lateinit var rvTests: RecyclerView
    private lateinit var tvLoading: TextView
    private lateinit var btnExam: MaterialButton
    private lateinit var btnTraining: MaterialButton
    private lateinit var btnMistakes: MaterialButton
    private lateinit var btnSettings: MaterialButton
    
    private var tests: List<IndexEntry> = emptyList()
    private var selectedTest: IndexEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        repository = TestRepository()
        prefsManager = PreferencesManager(this)
        
        initViews()
        loadTests()
    }
    
    private fun initViews() {
        rvTests = findViewById(R.id.rvTests)
        tvLoading = findViewById(R.id.tvLoading)
        btnExam = findViewById(R.id.btnExam)
        btnTraining = findViewById(R.id.btnTraining)
        btnMistakes = findViewById(R.id.btnMistakes)
        btnSettings = findViewById(R.id.btnSettings)
        
        rvTests.layoutManager = LinearLayoutManager(this)
        
        btnExam.setOnClickListener {
            selectedTest?.let { test ->
                startQuiz(test, "exam")
            } ?: Toast.makeText(this, "Выберите тест", Toast.LENGTH_SHORT).show()
        }
        
        btnTraining.setOnClickListener {
            selectedTest?.let { test ->
                startQuiz(test, "training")
            } ?: Toast.makeText(this, "Выберите тест", Toast.LENGTH_SHORT).show()
        }
        
        btnMistakes.setOnClickListener {
            selectedTest?.let { test ->
                startQuiz(test, "mistakes")
            } ?: Toast.makeText(this, "Выберите тест", Toast.LENGTH_SHORT).show()
        }
        
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
    
    private fun loadTests() {
        tvLoading.visibility = View.VISIBLE
        rvTests.visibility = View.GONE
        
        lifecycleScope.launch {
            val result = repository.getIndex()
            result.onSuccess { indexFile ->
                tests = indexFile.tests
                rvTests.adapter = TestAdapter(tests) { test ->
                    selectedTest = test
                    updateButtonsState()
                }
                tvLoading.visibility = View.GONE
                rvTests.visibility = View.VISIBLE
            }.onFailure {
                tvLoading.text = "Ошибка загрузки: ${it.message}"
                Toast.makeText(this@MainActivity, "Ошибка: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun updateButtonsState() {
        val isEnabled = selectedTest != null
        btnExam.isEnabled = isEnabled
        btnTraining.isEnabled = isEnabled
        btnMistakes.isEnabled = isEnabled
    }
    
    private fun startQuiz(test: IndexEntry, mode: String) {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra(QuizActivity.EXTRA_TEST, test)
            putExtra(QuizActivity.EXTRA_MODE, mode)
        }
        startActivity(intent)
    }
}
