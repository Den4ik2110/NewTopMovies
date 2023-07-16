package ru.netology.newtopmovies.view

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.ActivityBottomNavigationBinding
import ru.netology.newtopmovies.util.ArrayAdapterCustom
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var arrayForAdapterSearch: MutableList<Movie?>
    private lateinit var arrayAdapter: ArrayAdapterCustom
    private var keyFragment = Constants.FRAGMENT_MOVIE
    private val viewModel by viewModels<MovieViewModel>()

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        arrayForAdapterSearch = mutableListOf()
        arrayAdapter = ArrayAdapterCustom(this, R.layout.array_adapter, arrayForAdapterSearch, binding.fragmentContainer, viewModel)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        viewModel.arrayForAdapterSearch.observe(this) { array ->
            arrayAdapter = ArrayAdapterCustom(this, R.layout.array_adapter, array, binding.fragmentContainer, viewModel)
        }

        viewModel.shareAllMovie.observe(this) { allMovie ->
            Intent().apply {
                action = Intent.ACTION_SEND
                val intentString = buildString {
                    append("Смотри! Вся моя коллекция!\n")
                    append(
                        "Она насчитывает ${allMovie.size} ${
                            rounding(
                                allMovie.size,
                                Constants.KEY_ROUNDING_SHARE_ALL_MOVIE
                            )
                        }:\n"
                    )
                    var index = 1
                    allMovie.forEach { movie ->
                        append("$index. \"${movie.nameRu}\" - ${movie.rating / 10}.${movie.rating % 10}/10; \n")
                        index++
                    }
                }
                putExtra(Intent.EXTRA_TEXT, intentString)
                type = "text/plain"
            }.also { intent ->
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.text_share_message))
                startActivity(shareIntent)
            }
        }

        viewModel.hideToolbar.observe(this) { map ->
            keyFragment = map["type"] as String
            val value = map["text"]
            if (keyFragment == Constants.FRAGMENT_SEARCH) {
                binding.searchViewToolbar.apply {
                    setQuery("", false)
                    clearFocus()
                }
            }

            val toolbarTextView = binding.toolbar.findViewById<TextView>(R.id.toolbar_text)
            val toolbarSearchView = binding.toolbar.findViewById<SearchView>(R.id.searchViewToolbar)
            val toolbarButtomBack = binding.toolbar.findViewById<ImageView>(R.id.buttomBackToolbar)
            val toolbarRefresh = binding.toolbar.findViewById<ImageView>(R.id.toolbar_refresh)
            when (keyFragment) {
                Constants.FRAGMENT_SEARCH -> {
                    toolbarTextView.visibility = View.GONE
                    toolbarSearchView.visibility = View.VISIBLE
                    toolbarButtomBack.visibility = View.GONE
                    toolbarRefresh.visibility = View.GONE
                }
                Constants.FRAGMENT_PROFILE -> {
                    toolbarSearchView.visibility = View.GONE
                    toolbarTextView.visibility = View.VISIBLE
                    toolbarTextView.text = value
                    toolbarButtomBack.visibility = View.GONE
                    toolbarRefresh.visibility = View.GONE
                }
                Constants.FRAGMENT_MOVIE -> {
                    toolbarSearchView.visibility = View.GONE
                    toolbarTextView.visibility = View.VISIBLE
                    toolbarTextView.text = value
                    toolbarButtomBack.visibility = View.GONE
                    toolbarRefresh.visibility = View.GONE
                }
                Constants.FRAGMENT_ALL_MOVIES -> {
                    toolbarSearchView.visibility = View.GONE
                    toolbarTextView.visibility = View.VISIBLE
                    toolbarTextView.text = value
                    toolbarButtomBack.visibility = View.VISIBLE
                    toolbarRefresh.visibility = View.GONE
                }
                Constants.FRAGMENT_MOVIE_INFO -> {
                    toolbarSearchView.visibility = View.GONE
                    toolbarTextView.visibility = View.GONE
                    toolbarButtomBack.visibility = View.VISIBLE
                    toolbarRefresh.visibility = View.GONE
                }
            }
        }

        viewModel.writeAllMovieInTextFile.observe(this) { listMovies ->
            try {
                val savePath = File(
                    Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
                        .toString()
                )
                val newFile = File(savePath, "ListMovies.txt")
                if (newFile.exists()) newFile.delete()
                    newFile.bufferedWriter().use { file ->
                    for (movie in listMovies) {
                        file.write(
                            "${movie.nameRu} \n" +
                                    //"Жанр - ${if (movie.genre == null) "не указан" else movie.genre} \n" +
                                    "Год выпуска - ${if (movie.year == null) "не указан" else movie.year} \n" +
                                    "Юмор - ${movie.humor / 10}" + "," + "${movie.humor % 10} \n" +
                                    "Музыка - ${movie.music / 10}" + "," + "${movie.music % 10} \n" +
                                    "Динамика - ${movie.dynamic / 10}" + "," + "${movie.dynamic % 10} \n" +
                                    "Картинка - ${movie.image / 10}" + "," + "${movie.image % 10} \n" +
                                    "Диалоги - ${movie.dialogs / 10}" + "," + "${movie.dialogs % 10} \n" +
                                    "Герои - ${movie.heroes / 10}" + "," + "${movie.heroes % 10} \n" +
                                    "Злодеи - ${movie.antiheroes / 10}" + "," + "${movie.antiheroes % 10} \n" +
                                    "Сюжет - ${movie.story / 10}" + "," + "${movie.story % 10} \n" +
                                    "Драма - ${movie.drama / 10}" + "," + "${movie.drama % 10} \n" +
                                    "Повтор - ${movie.repeat / 10}" + "," + "${movie.repeat % 10} \n" +
                                    "Суммарный рейтинг - ${movie.rating / 10}" + "," + "${movie.rating % 10} \n\n"
                        )
                    }
                }
                Toast.makeText(this, "Файл сохранен в папку Загрузки", Toast.LENGTH_SHORT).show()
            } catch (ex: IOException) {
                ex.printStackTrace()
                Toast.makeText(this, "Файл не сохранен", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.writeAllMovieInDataBaseFile.observe(this) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }

            if (Environment.isExternalStorageManager()) {
                val sourcePath = File(filesDir.parent!!.toString() + getString(R.string.path_source))
                val savePath = File(
                    Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
                        .toString()
                )

                val sourceFile = File(sourcePath, "data_base_movie.db")
                val saveFile = File(savePath, "data_base_movie.db")

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Files.copy(
                            sourceFile.toPath(),
                            saveFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                        )
                    }
                    Toast.makeText(this, "Файл сохранен в папку Загрузки", Toast.LENGTH_SHORT).show()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    Toast.makeText(this, "Файл не сохранен", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttomBackToolbar.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }


    private fun rounding(amount: Int, key: String): String = if (amount in 11..14) {
        if (key == "movie") "баллов" else "фильмов"
    } else {
        when (amount % 10) {
            1 -> if (key == "movie") "балл" else "фильм"
            in 2..4 -> if (key == "movie") "балла" else "фильма"
            else -> if (key == "movie") "баллов" else "фильмов"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchViewToolbar
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val searchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
        searchAutoComplete.apply {
            setAdapter(arrayAdapter)
            threshold = 2
            setOnItemClickListener { _, view, _, _ ->
                val text = view.findViewById<TextView>(R.id.textView3).text
                searchView.setQuery(text, false)
            }
        }
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
            .setOnClickListener {
                viewModel.setSearchQuery(null)
                searchView.apply {
                    setQuery("", false)
                    clearFocus()
                }
            }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            if (Intent.ACTION_SEARCH == intent.action) {
                intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                    viewModel.setSearchQuery(query)
                }
            }
        }
    }
}