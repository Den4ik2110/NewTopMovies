package ru.netology.newtopmovies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.newtopmovies.database.DataBaseManager
import ru.netology.newtopmovies.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MovieAdapter.Listener {
    private lateinit var myDataBaseManager: DataBaseManager
    private lateinit var binding: ActivityMainBinding
    private var adapter = MovieAdapter(this)
    private lateinit var movieNow: Movie
    private lateinit var itemNow: View
    private var itemClickSave = mutableListOf<View>()
    private var movieClickSave = mutableListOf<Movie>()
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBaseManager = DataBaseManager(this)
        toolbar =
            binding.toolbarMy                                 // Нашел мой новый тулбар на экране
        setSupportActionBar(toolbar)                                // Задал его в качестве основного тулбара (стоковый скрыт)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           // Вывел на экран кнопку Home
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_filter)     // Поменял иконку кнопки Home

        clickNavigationMenu()


    }

    override fun onResume() {
        super.onResume()
        binding.drawer.closeDrawer(GravityCompat.START)
        myDataBaseManager.openDb()
        adapter.setMoviesList(myDataBaseManager.getFromDataBase()) // Передаю в адаптер список фильмов, загруженных из БД
        init()
        menuSelector(R.menu.toolbar_menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDataBaseManager.closeDataBase()
    }

    // Слушатель для всех кнопок из навигационного меню (слева которое)
    private fun clickNavigationMenu() {
        binding.apply {
            navigMenu.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_bar_switch_a_z -> adapter.sortTitleAtoZ()
                    R.id.nav_bar_switch_z_a -> adapter.sortTitleZtoA()
                    R.id.nav_bar_switch_min_max -> adapter.sortRatingMinToMax()
                    R.id.nav_bar_switch_max_min -> adapter.sortRatingMaxToMin()
                    R.id.nav_bar_switch_time_add -> adapter.sortTimeAdd()
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
            R.id.toolbar_add ->
                startActivity(Intent(this@MainActivity, ActivityAddMovie::class.java))
            R.id.toolbar_edit -> {
                val intent = Intent(this, ActivityEditMovie::class.java)
                intent.putExtra("keyEdit", movieNow)
                startActivity(intent)
            }
            R.id.toolbar_delete -> {
                myDataBaseManager.deleteMovie(movieNow.idMovie)
                adapter.delete(movieNow)
                binding.toolbarMy.apply {
                    menu.clear()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    inflateMenu(R.menu.toolbar_menu)
                    setNavigationIcon(R.drawable.ic_toolbar_filter)
                    itemNow.findViewById<View>(R.id.click_rate)
                        .setBackgroundResource(R.color.grey_background_card)
                }
            }
        }
        return true
    }

    // Метод добавляет меню на Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // Метод выбора расположения элементов в рецикл вью и инициализация адаптера
    private fun init() {
        binding.apply {
            recyclerMovie.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerMovie.adapter = adapter
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(movie: Movie, item: View, movies: MutableList<Movie>) {
        movieNow = movie
        itemNow = item

        if (!movie.isClicked) {
            menuSelector(R.menu.toolbar_menu_second)
            item.findViewById<View>(R.id.click_rate).setBackgroundResource(R.color.teal_700)
            itemClickSave.add(item)
            movieClickSave.add(movie)
            movie.isClicked = !movie.isClicked
        } else {
            menuSelector(R.menu.toolbar_menu)
            item.findViewById<View>(R.id.click_rate)
                .setBackgroundResource(R.color.grey_background_card)
            itemClickSave.remove(item)
            movieClickSave.remove(movie)
            movie.isClicked = !movie.isClicked
        }
        if (itemClickSave.isNotEmpty() && movieClickSave.isNotEmpty()) {

            if (itemClickSave[0] != item) {
                itemClickSave[0].findViewById<View>(R.id.click_rate)
                    .setBackgroundResource(R.color.grey_background_card)
                itemClickSave.remove(itemClickSave[0])
            }

            if (movieClickSave[0] != movie) {
                movieClickSave[0].isClicked = !movieClickSave[0].isClicked
                movieClickSave.remove(movieClickSave[0])
            }
        }
    }

    private fun menuSelector(menuId: Int) {
        binding.toolbarMy.apply {
            menu.clear()
            inflateMenu(menuId)
        }
    }

}