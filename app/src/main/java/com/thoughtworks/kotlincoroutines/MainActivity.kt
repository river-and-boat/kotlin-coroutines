package com.thoughtworks.kotlincoroutines

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        findViewById<ConstraintLayout>(R.id.rootLayout).setOnClickListener {
            viewModel.onMainViewClicked()
        }

        viewModel.snackbar.observe(this, {
            if (it != null)
                showProgress()
            else
                hideProgress()
        })
    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.spinner).visibility = View.VISIBLE
        viewModel.onSnackbarShown()
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.spinner).visibility = View.GONE
    }
}