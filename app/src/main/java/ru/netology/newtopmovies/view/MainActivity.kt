package ru.netology.newtopmovies.view


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.ActivityMainBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity(), MovieAdapter.ShowDialog {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var adapter: MovieAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarReplace()

        adapter = MovieAdapter(this@MainActivity, this)
        binding.recyclerMovie.adapter = adapter

        viewModel.data.observe(this) { movieFromData ->
            adapter.submitList(movieFromData)
            toolbar.title =
                if (movieFromData.isEmpty()) "Добавь новый фильм ->" else "Всего фильмов - ${movieFromData.size}"
            Timer().schedule(750) {
                binding.recyclerMovie.smoothScrollToPosition(0)
            }
        }

        viewModel.keySort.observe(this) { key ->
            when (key) {
                "A_Z" -> isCheckedMenuItem(key)
                "Min_Max" -> isCheckedMenuItem(key)
                "Max_Min" -> isCheckedMenuItem(key)
                "Old_New" -> isCheckedMenuItem(key)
                "Repeat" -> isCheckedMenuItem(key)
                "New_Old" -> isCheckedMenuItem(key)
            }
        }
        clickNavigationMenu()

        viewModel.shareMovie.observe(this) { movie ->
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getStringShare(movie))
                type = "text/plain"
            }.also { intent ->
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.text_share_message))
                startActivity(shareIntent)
            }

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
                        append("$index. \"${movie.title}\" - ${movie.rating}/100; \n")
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

    }

    private fun getStringShare(movie: Movie): String {
        return "Посмотрел недавно фильм \"${movie.title}\". \n" +
                "\n" +
                "Я оценил его в ${movie.rating} ${
                    rounding(
                        movie.rating,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 100 \n" +
                "\n" +
                "Вот все критерии, по которым я оценивал:\n" +
                "Юмор - ${movie.humor} ${
                    rounding(
                        movie.humor,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Музыка - ${movie.music} ${
                    rounding(
                        movie.music,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Динамика - ${movie.dynamic} ${
                    rounding(
                        movie.dynamic,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Картинка - ${movie.image} ${
                    rounding(
                        movie.image,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Диалоги - ${movie.dialogs} ${
                    rounding(
                        movie.dialogs,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Герои - ${movie.heroes} ${
                    rounding(
                        movie.heroes,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Злодеи - ${movie.antiheroes} ${
                    rounding(
                        movie.antiheroes,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "История - ${movie.story} ${
                    rounding(
                        movie.story,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "Драма - ${movie.drama} ${
                    rounding(
                        movie.drama,
                        Constants.KEY_ROUNDING_SHARE_MOVIE
                    )
                } из 10 \n" +
                "\n" +
                "Если тебе интересно мое мнение, тогда знай: ${isRepeat(movie.repeat)}"
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

    private fun isRepeat(repeat: Int): String {
        return when (repeat) {
            0 -> "я пересматривать этот фильм не собираюсь!"
            10 -> "я с радостью посмотрю его еще ни один раз!"
            else -> "В рамках общей истории посмотрю, но сам по себе фильм - ни рыба, ни мясо!"
        }
    }

    override fun onResume() {
        super.onResume()
        binding.drawer.closeDrawer(GravityCompat.START)
    }

    // Слушатель для всех кнопок из навигационного меню (слева которое)
    private fun clickNavigationMenu() {

        binding.apply {
            navigMenu.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_bar_switch_a_z -> {
                        viewModel.sortMovie("A_Z")
                    }
                    R.id.nav_bar_switch_min_max -> {
                        viewModel.sortMovie("Min_Max")
                    }
                    R.id.nav_bar_switch_max_min -> {
                        viewModel.sortMovie("Max_Min")
                    }
                    R.id.nav_bar_switch_old_new -> {
                        viewModel.sortMovie("Old_New")
                    }
                    R.id.nav_bar_switch_new_old -> {
                        viewModel.sortMovie("New_Old")
                    }
                    R.id.nav_bar_repeat -> {
                        viewModel.sortMovie("Repeat")
                    }
                    R.id.nav_bar_share_all -> {
                        viewModel.shareAllMovie()
                    }
                    R.id.nav_bar_download -> startActivity(
                        Intent(
                            this@MainActivity,
                            ActivityDownloadMovie::class.java
                        )
                    )
                    R.id.nav_bar_delete -> {
                        val alertDialog = DeleteAllMovieDialogFragment(viewModel)
                        alertDialog.show(supportFragmentManager, "keyAll")
                    }
                }
                binding.drawer.closeDrawer(GravityCompat.START)
                true
            }
        }
    }

    // Метод - слушатель для кнопок на Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawer.openDrawer(GravityCompat.START)
            R.id.toolbar_add -> startActivity(
                Intent(
                    this@MainActivity,
                    ActivityAddMovie::class.java
                )
            )
        }
        return true
    }

    // Метод добавляет меню на Toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun showDialog(movie: Movie) {
        val alertDialog = DeleteMovieDialogFragment(viewModel, movie)
        alertDialog.show(supportFragmentManager, "keyOne")
    }

    override fun shareMovie(movie: Movie) {
        viewModel.shareMovie(movie)
    }

    private fun toolbarReplace() {
        toolbar = binding.toolbarMy
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_filter)
        toolbar.elevation = 20F
    }

    private fun isCheckedMenuItem(key: String) {
        binding.navigMenu.menu.apply {
            findItem(R.id.nav_bar_switch_a_z).isChecked = key == "A_Z"
            findItem(R.id.nav_bar_switch_min_max).isChecked = key == "Min_Max"
            findItem(R.id.nav_bar_switch_max_min).isChecked = key == "Max_Min"
            findItem(R.id.nav_bar_switch_old_new).isChecked = key == "Old_New"
            findItem(R.id.nav_bar_repeat).isChecked = key == "Repeat"
            findItem(R.id.nav_bar_switch_new_old).isChecked = key == "New_Old"
        }
    }


}