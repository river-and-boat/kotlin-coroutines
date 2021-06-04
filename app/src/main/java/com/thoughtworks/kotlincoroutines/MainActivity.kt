package com.thoughtworks.kotlincoroutines

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootLayout: ConstraintLayout = findViewById(R.id.rootLayout)
        val title: TextView = findViewById(R.id.title)
        val spinner: ProgressBar = findViewById(R.id.spinner)

        val database = getDatabase(this)
        val repository = TitleRepository(MainNetworkImp, database.titleDao)
        val viewModel = ViewModelProvider(this, MainViewModel.FACTORY(repository)).get(MainViewModel::class.java)

        rootLayout.setOnClickListener {
            viewModel.onMainViewClicked()
        }

        viewModel.title.observe(this, { value ->
            value?.let {
                title.text = it
            }
        })

        viewModel.spinner.observe(this, { value ->
            value?.let { show ->
                spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel.snackbar.observe(this, { text ->
            text?.let {
                Snackbar.make(rootLayout, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        })
    }
}