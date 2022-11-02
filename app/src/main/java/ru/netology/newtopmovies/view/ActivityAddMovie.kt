package ru.netology.newtopmovies.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.WishMovie
import ru.netology.newtopmovies.databinding.ActivityAddMovieBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.viewModel.MovieViewModel
import java.util.ArrayList

class ActivityAddMovie : AppCompatActivity(), PickiTCallbacks {
    private lateinit var binding: ActivityAddMovieBinding
    private var uriImage: String? = null
    private val viewModel by viewModels<MovieViewModel>()
    private var wishMovie: WishMovie? = null
    private lateinit var pickit: PickiT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pickit = PickiT(this, this, this)

        if (intent.getSerializableExtra("keyWishMovieAdd") != null) {
            wishMovie = intent.getSerializableExtra("keyWishMovieAdd") as WishMovie
            binding.editTextPersonName.setText(wishMovie?.title)
            binding.year.setText(wishMovie!!.year.toString())
        }

        binding.bKriterii.setOnClickListener {
            if (binding.editTextPersonName.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Сперва введите название фильма",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.groupVisInvis.visibility = View.VISIBLE
                binding.buttonSaveAll.visibility = View.VISIBLE
                binding.bKriterii.visibility = View.GONE
                binding.buttomNotSave.visibility = View.VISIBLE

                movementSeekBar(binding.Humor, binding.valueHumor)
                movementSeekBar(binding.Dinamic, binding.valueDinamic)
                movementSeekBar(binding.story, binding.ValueStory)
                movementSeekBar(binding.heroes, binding.ValueHeroes)
                movementSeekBar(binding.antiheroes, binding.ValueAntiheroes)
                movementSeekBar(binding.drama, binding.valueDrama)
                movementSeekBar(binding.Musik, binding.ValueMusik)
                movementSeekBar(binding.Image, binding.valueImage)
                movementSeekBar(binding.dialog, binding.valueDialog)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onResume() {
        super.onResume()

        viewModel.editUrlImage.observe(this) { url ->
            uriImage = url
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_no_image)
                .into(binding.imageAddMovie)
        }

        binding.buttonSaveAll.setOnClickListener {
            if (wishMovie != null) viewModel.deleteWishMovie(wishMovie!!)
            viewModel.addMovie(
                Movie(
                    binding.editTextPersonName.text.toString(),
                    binding.valueHumor.text.toString().toInt(),
                    binding.ValueMusik.text.toString().toInt(),
                    binding.valueDinamic.text.toString().toInt(),
                    binding.valueImage.text.toString().toInt(),
                    binding.valueDialog.text.toString().toInt(),
                    binding.ValueHeroes.text.toString().toInt(),
                    binding.ValueAntiheroes.text.toString().toInt(),
                    binding.ValueStory.text.toString().toInt(),
                    binding.valueDrama.text.toString().toInt(),
                    when (binding.repeatAddMovie.progress) {
                        0 -> 0
                        2 -> 10
                        else -> 5
                    },
                    urlImage = if (uriImage != null) uriImage else null,
                    genre = binding.genre.text.toString(),
                    year = binding.year.text.toString()
                )
            )
            finish()
        }

        binding.buttomNotSave.setOnClickListener {
            Toast.makeText(applicationContext, "Фильм не сохранен", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        binding.imageAddMovie.setOnClickListener {
            val downloadImageFragment = DownloadImageFragment(viewModel)
            downloadImageFragment.show(supportFragmentManager, "DownloadImageFranchise")
        }

        viewModel.editImageMovie.observe(this) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }

            if (Environment.isExternalStorageManager()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_PICK
                    type = "image/*"
                }
                val shareIntent = Intent.createChooser(intent, getString(android.R.string.search_go))
                startActivityForResult(shareIntent, Constants.INTENT_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constants.INTENT_PHOTO) {
            pickit.getPath(data?.data, Build.VERSION.SDK_INT)
        }
    }

    private fun movementSeekBar(seekBar: SeekBar, valueSeekBar: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                valueSeekBar.text = p1.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    override fun PickiTonUriReturned() {}

    override fun PickiTonStartListener() {}

    override fun PickiTonProgressUpdate(progress: Int) {}

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        if (path != null) {
            uriImage = path
        }

        Glide.with(this)
            .load(uriImage)
            .override(500, 700)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_no_image)
            .into(binding.imageAddMovie)
    }

    override fun PickiTonMultipleCompleteListener(
        paths: ArrayList<String>?,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }

}