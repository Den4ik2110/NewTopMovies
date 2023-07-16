package ru.netology.newtopmovies.util



import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.databinding.SheetBottomRatingBinding
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomRating(
    private val movie: Movie
) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var binding1: SheetBottomRatingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomRatingBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            binding1 = binding

            chain(movie)

            movementSeekBar(binding.humor2, binding.valueHumor2)
            movementSeekBar(binding.dinamic2, binding.valueDinamic2)
            movementSeekBar(binding.story2, binding.valueStory)
            movementSeekBar(binding.heroes2, binding.valueHeroes)
            movementSeekBar(binding.antiheroes2, binding.valueAntiheroes)
            movementSeekBar(binding.drama2, binding.valueDrama2)
            movementSeekBar(binding.musik2, binding.valueMusik2)
            movementSeekBar(binding.image2, binding.valueImage2)
            movementSeekBar(binding.dialog2, binding.valueDialog2)

            binding.buttonSaveAll2.setOnClickListener {
                viewModel.editMovie(
                    Movie(
                        movie.nameRu,
                        binding.valueHumor2.text.toString().toInt(),
                        binding.valueMusik2.text.toString().toInt(),
                        binding.valueDinamic2.text.toString().toInt(),
                        binding.valueImage2.text.toString().toInt(),
                        binding.valueDialog2.text.toString().toInt(),
                        binding.valueHeroes.text.toString().toInt(),
                        binding.valueAntiheroes.text.toString().toInt(),
                        binding.valueStory.text.toString().toInt(),
                        binding.valueDrama2.text.toString().toInt(),
                        when (binding.repeatAddMovie3.progress) {
                            0 -> 0
                            2 -> 10
                            else -> 5
                        },
                        movie.idMovie,
                        movie.rating,
                        movie.isClicked,
                        movie.posterUrlPreview,
                        movie.genres,
                        movie.year,
                        movie.franchise,
                        movie.kinopoiskId,
                        movie.nameOriginal,
                        movie.posterUrl,
                        movie.coverUrl,
                        movie.logoUrl,
                        movie.ratingKinopoisk,
                        movie.ratingKinopoiskVoteCount,
                        movie.ratingImdb,
                        movie.ratingImdbVoteCount,
                        movie.webUrl,
                        movie.filmLength,
                        movie.description,
                        movie.ratingAgeLimits,
                        movie.countries
                    )
                )
                closeSheetBottom()
            }

        }.root

    companion object {
        const val TAG = "SheetBottomRating"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
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

    private fun chain(editMovie: Movie) {
        with(binding1) {
            humor2.progress = editMovie.humor
            dinamic2.progress = editMovie.dynamic
            story2.progress = editMovie.story
            heroes2.progress = editMovie.heroes
            antiheroes2.progress = editMovie.antiheroes
            drama2.progress = editMovie.drama
            musik2.progress = editMovie.music
            image2.progress = editMovie.image
            dialog2.progress = editMovie.dialogs
            repeatAddMovie3.progress = when (editMovie.repeat) {
                0 -> 0
                10 -> 2
                else -> 1
            }

            valueHumor2.text = editMovie.humor.toString()
            valueDinamic2.text = editMovie.dynamic.toString()
            valueStory.text = editMovie.story.toString()
            valueHeroes.text = editMovie.heroes.toString()
            valueAntiheroes.text = editMovie.antiheroes.toString()
            valueDrama2.text = editMovie.drama.toString()
            valueMusik2.text = editMovie.music.toString()
            valueImage2.text = editMovie.image.toString()
            valueDialog2.text = editMovie.dialogs.toString()
        }
    }
}