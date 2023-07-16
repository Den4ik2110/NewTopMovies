package ru.netology.newtopmovies.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.apiSetting.App
import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.SequelsAndPrequels
import ru.netology.newtopmovies.databinding.SheetBottomMenuAndInfoMovieBinding
import ru.netology.newtopmovies.view.InputIdKinopoisk
import ru.netology.newtopmovies.view.InputNameFranchise
import ru.netology.newtopmovies.viewModel.MovieViewModel

class SheetBottomMenuAndInfo(
    private val movie: Movie,
    private val contextActivity: Context

) : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MovieViewModel>()
    private val apiToken = "587ee233-7fbc-4e45-b1f2-8af58dda6335"
    private var sequelsAndPrequels: List<SequelsAndPrequels>? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = SheetBottomMenuAndInfoMovieBinding.inflate(layoutInflater, container, false)
        .also { binding ->
            binding.apply{
                sheetBottomTitle.text = movie.nameRu

                Glide.with(context as Context)
                    .load(movie.posterUrlPreview)
                    .placeholder(R.drawable.ic_no_image)
                    .into(imageView9)

                textView20.text = "${movie.nameOriginal}, ${movie.year}"

                rootId.setOnClickListener {
                    val alertDialog = InputIdKinopoisk(viewModel, movie, contextActivity)
                    alertDialog.show(parentFragmentManager, "keyOne")
                    closeSheetBottom()
                }

                rootFranchise.setOnClickListener {
                    val alertDialog = InputNameFranchise(viewModel, movie, contextActivity)
                    alertDialog.show(parentFragmentManager, "keyOne")
                    closeSheetBottom()
                }

                rootRepeat.setOnClickListener {
                    if (movie.kinopoiskId != null) {
                        App.getApi().getDataId(movie.kinopoiskId, apiToken)
                            .enqueue(object : Callback<Movie?> {
                                override fun onResponse(
                                    call: Call<Movie?>?,
                                    response: Response<Movie?>
                                ) {
                                    val newDataMovie = response.body() as Movie
                                    viewModel.editMovie(
                                        Movie(
                                            newDataMovie.nameRu,
                                            movie.humor,
                                            movie.music,
                                            movie.dynamic,
                                            movie.image,
                                            movie.dialogs,
                                            movie.heroes,
                                            movie.antiheroes,
                                            movie.story,
                                            movie.drama,
                                            movie.repeat,
                                            movie.idMovie,
                                            movie.rating,
                                            newDataMovie.isClicked,
                                            newDataMovie.posterUrlPreview,
                                            newDataMovie.genres,
                                            newDataMovie.year,
                                            movie.franchise,
                                            newDataMovie.kinopoiskId,
                                            newDataMovie.nameOriginal,
                                            newDataMovie.posterUrl,
                                            newDataMovie.coverUrl,
                                            newDataMovie.logoUrl,
                                            newDataMovie.ratingKinopoisk,
                                            newDataMovie.ratingKinopoiskVoteCount,
                                            newDataMovie.ratingImdb,
                                            newDataMovie.ratingImdbVoteCount,
                                            newDataMovie.webUrl,
                                            newDataMovie.filmLength,
                                            newDataMovie.description,
                                            newDataMovie.ratingAgeLimits,
                                            newDataMovie.countries,
                                            sequelsAndPrequels
                                        )
                                    )
                                    Toast.makeText(
                                        contextActivity,
                                        "Данные успешно обновлены!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d("MyLog", response.body().toString())
                                }
                                override fun onFailure(call: Call<Movie?>, t: Throwable?) {
                                    Log.d("MyLog", t.toString())
                                    Toast.makeText(
                                        contextActivity,
                                        "Ошибка загрузки данных о фильме!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    closeSheetBottom()
                                }
                            })



                        App.getApi().getSequelsAndPrequels(movie.kinopoiskId, apiToken)
                            .enqueue(object : Callback<List<SequelsAndPrequels>?> {
                                override fun onResponse(
                                    call: Call<List<SequelsAndPrequels>?>,
                                    response: Response<List<SequelsAndPrequels>?>
                                ) {
                                    sequelsAndPrequels = response.body()
                                    viewModel.updateSequelsAndPrequels(sequelsAndPrequels, movie.kinopoiskId)
                                    viewModel.searchDataSingle.observe(viewLifecycleOwner) { listMovie ->
                                        val listSequelsAndPrequelsString = sequelsAndPrequels.toListString()
                                        listMovie.forEach { movieSearch ->
                                            if (listSequelsAndPrequelsString?.contains(movieSearch.kinopoiskId.toString()) == true) {
                                                viewModel.updateSequelsAndPrequels(
                                                    listSequelsAndPrequelsString.joinToString(", ")
                                                        .replace(
                                                            movieSearch.kinopoiskId.toString(),
                                                            movie.kinopoiskId.toString(),
                                                            true
                                                        ).toSequelsAndPrequels(),
                                                    movieSearch.kinopoiskId
                                                )
                                            }
                                        }

                                    }
                                    Log.d("MyLog", response.body().toString())
                                    closeSheetBottom()
                                }

                                override fun onFailure(call: Call<List<SequelsAndPrequels>?>, t: Throwable?) {
                                    Log.d("MyLog", t.toString())
                                    Toast.makeText(
                                        contextActivity,
                                        "Ошибка загрузки сиквелов и приквелов!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    closeSheetBottom()
                                }
                            })
                    } else {
                        Toast.makeText(
                            contextActivity,
                            "Сперва задайте ID Кинопоиска",
                            Toast.LENGTH_LONG
                        ).show()
                        closeSheetBottom()
                    }
                }
            }
        }.root

    companion object {
        const val TAG = "SheetBottomMenuAndInfo"
    }

    private fun closeSheetBottom() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setSearchQuery(null)
    }
}