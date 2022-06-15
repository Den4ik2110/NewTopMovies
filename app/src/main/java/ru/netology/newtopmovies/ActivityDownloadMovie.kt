package ru.netology.newtopmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ru.netology.newtopmovies.database.DataBaseManager
import ru.netology.newtopmovies.databinding.ActivityDownloadMovieBinding

class ActivityDownloadMovie : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadMovieBinding
    private lateinit var myDataBaseManager: DataBaseManager
    private lateinit var nameMovie: String
    private var parameterList = mutableListOf<Int>()
    private var parameterListDouble = mutableListOf<Double>()
    private var parameterListString = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBaseManager = DataBaseManager(this)


    }

    override fun onResume() {
        super.onResume()
        myDataBaseManager.openDb()
        binding.buttonSaveAll.setOnClickListener {
            val inputText = binding.editTextPersonName.text.toString()
            val i = inputText.split("/10")
            for (movie in i) {
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

    override fun onDestroy() {
        super.onDestroy()
        myDataBaseManager.closeDataBase()
    }

    private fun addOneMovie(inputOneMovie:String) {
        val modText = inputOneMovie.split("\n")

        val mutableModText = mutableListOf<String>()
        for (string in modText) mutableModText.add(string)
        mutableModText.removeAt(0)
        mutableModText.removeAt(0)
        mutableModText.removeAt(mutableModText.size - 1)
        nameMovie = mutableModText[0]
        mutableModText.removeAt(0)
        Log.d("MyLog", "$mutableModText ${mutableModText.size} $nameMovie")

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
        Log.d("MyLog", "$parameterList")

        myDataBaseManager.insertToDataBase(
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
            },
            R.drawable.ic_no_image
        )

        parameterListDouble.clear()
        parameterListString.clear()
        parameterList.clear()

    }
}