package ru.netology.newtopmovies.view

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.ActivityBottomNavigationBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SheetBottomEditFranchise
import ru.netology.newtopmovies.util.SheetBottomWishMovieAdd
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.ArrayList
import kotlin.system.exitProcess


class BottomNavigationActivity : AppCompatActivity(), PickiTCallbacks {

    private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var arrayForAdapterSearch: MutableList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var pickit: PickiT
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

        pickit = PickiT(this, this, this)

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
                menu?.findItem(R.id.export)?.isVisible = false
                menu?.findItem(R.id.non_export)?.isVisible = false
            }
            Constants.FRAGMENT_WISHLIST -> {
                menu?.findItem(R.id.app_bar_search)?.isVisible = false
                menu?.findItem(R.id.share_all)?.isVisible = false
                menu?.findItem(R.id.edit_franchise)?.isVisible = false
                menu?.findItem(R.id.export)?.isVisible = false
                menu?.findItem(R.id.non_export)?.isVisible = false
            }
            Constants.FRAGMENT_MOVIE -> {
                menu?.findItem(R.id.edit_franchise)?.isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.R)
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
                        sheetBottomWishMovieAdd.show(
                            supportFragmentManager,
                            SheetBottomWishMovieAdd.TAG
                        )
                    }
                }
            }
            R.id.share_all -> viewModel.shareAllMovie()
            R.id.edit_franchise -> {
                val sheetBottomEditFranchise = SheetBottomEditFranchise()
                sheetBottomEditFranchise.show(supportFragmentManager, SheetBottomEditFranchise.TAG)
            }
            R.id.export -> exportDataBase()
            R.id.non_export -> importDataBase()
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

    @RequiresApi(Build.VERSION_CODES.R)
    private fun exportDataBase() {

        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }

        if (Environment.isExternalStorageManager()) {
            val sourcePath = File(filesDir.parent.toString() + getString(R.string.path_source))
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


    @RequiresApi(Build.VERSION_CODES.R)
    private fun importDataBase() {

        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        }

        if (Environment.isExternalStorageManager()) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/octet-stream"
            }
            startActivityForResult(intent, Constants.INTENT_IMPORT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.INTENT_IMPORT) {
            if (resultCode == Activity.RESULT_OK) {
                pickit.getPath(data?.data, Build.VERSION.SDK_INT)
            }
        }
    }

    override fun PickiTonUriReturned() {
    }

    override fun PickiTonStartListener() {
    }

    override fun PickiTonProgressUpdate(progress: Int) {
    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {


        if (path != null) {
            val pathDeleteAndImport =
                File(filesDir.parent.toString() + getString(R.string.path_source))
            val importFile = File(path)
            val deleteAndImportFileDB = File(pathDeleteAndImport, "data_base_movie.db")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.deleteIfExists(deleteAndImportFileDB.toPath())
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("MyLog", "${importFile.toPath()}")
                Log.d("MyLog", "${deleteAndImportFileDB.toPath()}")
            }

            try {
                importFile.setWritable(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.copy(
                        importFile.toPath(),
                        deleteAndImportFileDB.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
                Toast.makeText(this, "База данных импортирована", Toast.LENGTH_SHORT).show()
                exitProcess(-1)
            } catch (ex: IOException) {
                ex.printStackTrace()
                Toast.makeText(this, "База данных не импортирована", Toast.LENGTH_SHORT).show()
                Log.d("MyLog", "$ex")
            }
        }
    }

    override fun PickiTonMultipleCompleteListener(
        paths: ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }
}