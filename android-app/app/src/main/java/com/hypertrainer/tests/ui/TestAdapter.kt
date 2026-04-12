package com.hypertrainer.tests.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hypertrainer.tests.model.IndexEntry
import com.hypertrainer.tests.R

class TestAdapter(
    private val tests: List<IndexEntry>,
    private val onItemClick: (IndexEntry) -> Unit
) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testName: TextView = itemView.findViewById(R.id.testName)
        val testDescription: TextView = itemView.findViewById(R.id.testDescription)
        val testCategory: TextView = itemView.findViewById(R.id.testCategory)
        val testDifficulty: TextView = itemView.findViewById(R.id.testDifficulty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = tests[position]
        holder.testName.text = test.name
        holder.testDescription.text = test.description
        holder.testCategory.text = test.category
        holder.testDifficulty.text = when (test.difficulty) {
            "easy" -> "Лёгкий"
            "medium" -> "Средний"
            "hard" -> "Сложный"
            else -> test.difficulty
        }
        
        holder.itemView.setOnClickListener {
            onItemClick(test)
        }
    }

    override fun getItemCount(): Int = tests.size
}
