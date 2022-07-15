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
import ru.netology.newtopmovies.viewModel.MovieViewModel


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

        }

        clickNavigationMenu()
    }

    override fun onResume() {
        super.onResume()
        binding.drawer.closeDrawer(GravityCompat.START)
        toolbar.title = "Всего фильмов - 10000"
    }

    // Слушатель для всех кнопок из навигационного меню (слева которое)
    private fun clickNavigationMenu() {

        binding.apply {
            navigMenu.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_bar_switch_a_z -> {viewModel.sortMovie("A_Z")}
                    R.id.nav_bar_switch_min_max -> {viewModel.sortMovie("Min_Max")}
                    R.id.nav_bar_switch_max_min -> {viewModel.sortMovie("Max_Min")}
                    R.id.nav_bar_switch_old_new -> {viewModel.sortMovie("Old_New")}
                    R.id.nav_bar_switch_new_old -> {viewModel.sortMovie("New_Old")}
                    R.id.nav_bar_repeat -> {viewModel.sortMovie("Repeat")}
                    R.id.nav_bar_download -> startActivity(
                        Intent(
                            this@MainActivity,
                            ActivityDownloadMovie::class.java
                        )
                    )
                    R.id.nav_bar_delete -> viewModel.deleteAll()
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
            R.id.toolbar_add -> startActivity(Intent(this@MainActivity, ActivityAddMovie::class.java))
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
        alertDialog.show(supportFragmentManager, "key")
    }

    private fun toolbarReplace() {
        toolbar = binding.toolbarMy
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_filter)
    }
}