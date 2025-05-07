/*package com.example.trackr

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class CategoryAnalysisActivity : AppCompatActivity() {
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_analysis)

        pieChart = findViewById(R.id.pieChart)
        loadCategoryData()
    }

    private fun loadCategoryData() {
        val categoryMap = TransactionManager.getCategorySummary()

        val entries = ArrayList<PieEntry>()
        for ((category, total) in categoryMap) {
            entries.add(PieEntry(total.toFloat(), category))
        }

        val dataSet = PieDataSet(entries, "Spending by Category")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Category Breakdown"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }
}

*/

package com.example.trackr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.trackr.util.SharedPrefManager

class CategoryAnalysisActivity : AppCompatActivity() {
    private lateinit var pieChart: PieChart
    private lateinit var prefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_analysis)

        prefManager = SharedPrefManager(this)
        pieChart = findViewById(R.id.pieChart)
        loadCategoryData()
    }

    private fun loadCategoryData() {
        val categoryMap = getCategorySummary()

        val entries = ArrayList<PieEntry>()
        for ((category, total) in categoryMap) {
            entries.add(PieEntry(total.toFloat(), category))
        }

        // Handle empty data case
        if (entries.isEmpty()) {
            entries.add(PieEntry(100f, "No Data"))
        }

        val dataSet = PieDataSet(entries, "Spending by Category")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Category Breakdown"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun getCategorySummary(): Map<String, Double> {
        val transactions = prefManager.getTransactions()
        if (transactions.isEmpty()) return emptyMap()

        val categoryMap = mutableMapOf<String, Double>()

        for (transaction in transactions) {
            val category = transaction.category
            val amount = transaction.amount

            categoryMap[category] = (categoryMap[category] ?: 0.0) + amount
        }

        return categoryMap
    }
}
