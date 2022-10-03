package ru.netology.newtopmovies.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.ActivityBottomNavigationBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SheetBottomEditFranchise
import ru.netology.newtopmovies.util.SheetBottomFranchise
import ru.netology.newtopmovies.util.SheetBottomWishMovieAdd
import ru.netology.newtopmovies.viewModel.MovieViewModel

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var arrayForAdapterSearch: MutableList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var keyFragment = Constants.FRAGMENT_MOVIE
    private val viewModel by viewModels<MovieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        arrayForAdapterSearch = mutableListOf()
        arrayAdapter = ArrayAdapter(this, R.layout.suggetion, arrayForAdapterSearch)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        viewModel.arrayForAdapterSearch.observe(this) { array ->
            arrayAdapter = ArrayAdapter(this, R.layout.suggetion, array)
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
                        append("$index. \"${movie.title}\" - ${movie.rating / 10}.${movie.rating % 10}/10; \n")
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

        viewModel.hideToolbar.observe(this) { key ->
            keyFragment = key
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

        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            binding.toolbar.menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val searchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as AutoCompleteTextView
        searchAutoComplete.apply {
            setAdapter(arrayAdapter)
            threshold = 2
            setOnItemClickListener { _, view, _, _ ->
                val text = view.findViewById<TextView>(R.id.suggetion_text).text
                searchView.setQuery(text, false)
            }
        }
        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
            .setOnClickListener {
                viewModel.setSearchQuery(null)
                searchView.apply {
                    setQuery("", false)
                    clearFocus()
                    onActionViewCollapsed()
                }
                findViewById<RecyclerView>(R.id.recycle_movies).scrollToPosition(0)
            }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (keyFragment) {
            Constants.FRAGMENT_FRANCHISE -> {
                menu?.findItem(R.id.app_bar_search)?.isVisible = false
                menu?.findItem(R.id.share_all)?.isVisible = false
                menu?.findItem(R.id.toolbar_add)?.isVisible = false
            }
            Constants.FRAGMENT_WISHLIST -> {
                menu?.findItem(R.id.app_bar_search)?.isVisible = false
                menu?.findItem(R.id.share_all)?.isVisible = false
                menu?.findItem(R.id.edit_franchise)?.isVisible = false
            }
            Constants.FRAGMENT_MOVIE -> {
                menu?.findItem(R.id.edit_franchise)?.isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_add -> {
                when (keyFragment) {
                    Constants.FRAGMENT_MOVIE -> {
                        startActivity(
                            Intent(
                                this,
                                ActivityAddMovie::class.java
                            )
                        )
                    }
                    Constants.FRAGMENT_WISHLIST -> {
                        val sheetBottomWishMovieAdd = SheetBottomWishMovieAdd()
                        sheetBottomWishMovieAdd.show(supportFragmentManager, SheetBottomWishMovieAdd.TAG)
                    }
                }
            }
            R.id.share_all -> viewModel.shareAllMovie()
            R.id.edit_franchise -> {
                val sheetBottomEditFranchise = SheetBottomEditFranchise()
                sheetBottomEditFranchise.show(supportFragmentManager, SheetBottomEditFranchise.TAG)
            }
        }
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