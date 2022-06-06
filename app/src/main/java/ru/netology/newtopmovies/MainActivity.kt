package ru.netology.newtopmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.newtopmovies.database.DataBaseManager
import ru.netology.newtopmovies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MovieAdapter.Listener {
    lateinit var myDataBaseManager: DataBaseManager
/*    var moviesMain = mutableListOf(
        Movie("Я легенда", 10, 9, 9, 9, 9, 9, 9, 9, 9, 0, R.drawable.ic_no_image),
        Movie("Гадкий Я", 4, 5, 2, 3, 8, 10, 9, 10, 4, 1, R.drawable.ic_no_image),
        Movie("Крестный отец", 8, 5, 6, 4, 9, 2, 5, 7, 9, 0, R.drawable.ic_no_image),
        Movie("Интерстеллар", 4, 10, 7, 1, 8, 7, 9, 10, 3, 1, R.drawable.ic_no_image),
        Movie("Зверополис", 1, 7, 6, 4, 4, 2, 9, 1, 9, 0, R.drawable.ic_no_image),
        Movie("Человек-паук 2", 4, 7, 6, 5, 4, 2, 9, 10, 9, 1, R.drawable.ic_no_image),
        Movie("Мстители", 9, 7, 9, 4, 9, 2, 9, 9, 9, 0, R.drawable.ic_no_image),
        Movie("Гадкий Я", 4, 5, 2, 3, 8, 10, 9, 10, 4, 1, R.drawable.ic_no_image),
        Movie("Крестный отец", 8, 5, 6, 4, 9, 2, 5, 7, 9, 0, R.drawable.ic_no_image),
        Movie("Интерстеллар", 4, 10, 7, 1, 8, 7, 9, 10, 3, 0, R.drawable.ic_no_image),
        Movie("Зверополис", 1, 7, 6, 4, 4, 2, 9, 1, 9, 0, R.drawable.ic_no_image),
        Movie("Человек-паук 2", 4, 7, 6, 5, 4, 2, 9, 10, 9, 0, R.drawable.ic_no_image),
        Movie("Мстители", 9, 7, 9, 4, 9, 2, 9, 9, 9, 0, R.drawable.ic_no_image),
        Movie("Гадкий Я", 4, 5, 2, 3, 8, 10, 9, 10, 4, 1, R.drawable.ic_no_image),
        Movie("Крестный отец", 8, 5, 6, 4, 9, 2, 5, 7, 9, 0, R.drawable.ic_no_image),
        Movie("Интерстеллар", 4, 10, 7, 1, 8, 7, 9, 10, 3, 1, R.drawable.ic_no_image),
        Movie("Зверополис", 1, 7, 6, 4, 4, 2, 9, 1, 9, 1, R.drawable.ic_no_image),
        Movie("Человек-паук 2", 4, 7, 6, 5, 4, 2, 9, 10, 9, 1, R.drawable.ic_no_image),
        Movie("Мстители", 9, 7, 9, 4, 9, 2, 9, 9, 9, 0, R.drawable.ic_no_image),
    )*/


    private lateinit var binding: ActivityMainBinding
    private var adapter = MovieAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBaseManager = DataBaseManager(this)


        val toolbar = binding.toolbarMy                      // Нашел мой новый тулбар на экране
        setSupportActionBar(toolbar)                         // Задал его в качестве основного тулбара (стоковый скрыт)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)    // Вывел на экран кнопку Home
        toolbar.setNavigationIcon(R.drawable.ic_menu_open)   // Поменял иконку кнопки Home


        // Слушатель для всех кнопок из навигационного меню (слева которое)
        binding.apply {
            navigMenu.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_bar_search -> {}
                    R.id.nav_bar_add_movie -> {}
                    R.id.nav_bar_switch_max_min -> {}
                    R.id.nav_bar_switch_min_max -> {}
                    R.id.nav_bar_switch_watch_again -> {}
                }
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myDataBaseManager.openDb()
        adapter.setMoviesList(myDataBaseManager.getFromDataBase()) // Передаю в адаптер список фильмов, загруженных из БД
        myDataBaseManager.apply {
            insertToDataBase("Я легенда", 10, 9, 9, 9, 9, 9, 9, 9, 9, 0, R.drawable.ic_no_image)
            insertToDataBase("Гадкий Я", 4, 5, 2, 3, 8, 10, 9, 10, 4, 1, R.drawable.ic_no_image)
            insertToDataBase("Крестный отец", 8, 5, 6, 4, 9, 2, 5, 7, 9, 0, R.drawable.ic_no_image)
            insertToDataBase("Интерстеллар", 4, 10, 7, 1, 8, 7, 9, 10, 3, 1, R.drawable.ic_no_image)
            insertToDataBase("Человек-паук 2", 4, 7, 6, 5, 4, 2, 9, 10, 9, 1, R.drawable.ic_no_image)
            insertToDataBase("Зверополис", 1, 7, 6, 4, 4, 2, 9, 1, 9, 0, R.drawable.ic_no_image)
            insertToDataBase("Мстители", 9, 7, 9, 4, 9, 2, 9, 9, 9, 0, R.drawable.ic_no_image)
        }
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDataBaseManager.closeDataBase()
    }

    // Метод - слушатель для кнопок на Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawer.openDrawer(GravityCompat.START)
            R.id.repeat -> {
                adapter.repeatMoviesList()
                Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show()
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

    override fun onClick(movie: Movie) {
        Toast.makeText(this, "Переходим на карточку ${movie.title}", Toast.LENGTH_LONG).show()
    }

    override fun onLongClick(view: View) {
        registerForContextMenu(view)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.popup_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.context_change -> Toast.makeText(
                this,
                "Открыть меню изменения данных",
                Toast.LENGTH_SHORT
            ).show()
            R.id.context_delete -> Toast.makeText(
                this,
                "Удалить фильм из базы и обновить список",
                Toast.LENGTH_SHORT
            ).show()
            else -> return super.onContextItemSelected(item)
        }
        return true
    }
}