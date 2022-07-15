package ru.netology.newtopmovies.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.ActivityDownloadMovieBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class ActivityDownloadMovie : AppCompatActivity() {

    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var binding: ActivityDownloadMovieBinding
    private lateinit var nameMovie: String
    private var parameterList = mutableListOf<Int>()
    private var parameterListDouble = mutableListOf<Double>()
    private var parameterListString = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.buttonSaveAll.setOnClickListener {
            val inputText = binding.editTextPersonName.text.toString()
            val i = inputText.split("/10")
            val j = i.reversed()
            for (movie in j) {
                Log.d("MyLog", "movie = $movie")
                addOneMovie(movie)
            }
            finish()
        }

        binding.buttomNotSave.setOnClickListener {
            Toast.makeText(applicationContext, "Фильм не сохранен", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun addOneMovie(inputOneMovie: String) {
        val modText = inputOneMovie.split("\n")

        val mutableModText = mutableListOf<String>()
        for (string in modText) mutableModText.add(string)
        Log.d("MyLog", "mutableModText = $mutableModText")
        mutableModText.removeAt(0)
        mutableModText.removeAt(0)
        mutableModText.removeAt(mutableModText.size - 1)
        nameMovie = mutableModText[0]
        mutableModText.removeAt(0)

        for (string in mutableModText) {
            val i = string.split(" ")
            parameterListString.add(i[1])
        }

        for (string in parameterListString) {
            val i = string.toDouble()
            parameterListDouble.add(i)
        }

        for (string in parameterListDouble) {
            val i = (string * 10).toInt()
            parameterList.add(i)
        }

        viewModel.addMovie(
            Movie(
                nameMovie,
                parameterList[0],
                parameterList[1],
                parameterList[2],
                parameterList[3],
                parameterList[4],
                parameterList[5],
                parameterList[6],
                parameterList[8],
                parameterList[9],
                when (parameterList[7]) {
                    10 -> 10
                    0 -> 0
                    else -> 5
                }
            )
        )

        parameterListDouble.clear()
        parameterListString.clear()
        parameterList.clear()

    }
}