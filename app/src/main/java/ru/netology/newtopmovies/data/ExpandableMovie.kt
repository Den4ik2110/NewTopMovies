package ru.netology.newtopmovies.data

import android.view.View
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.ExpandableMovieBinding

class ExpandableMovie(
    private val name: String,
    private val countMovies: Int,
    private val childMovieList: List<Movie>
) : BindableItem<ExpandableMovieBinding>(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun bind(viewBinding: ExpandableMovieBinding, position: Int) {
        if (expandableGroup.isExpanded) viewBinding.iconExpandable.setImageResource(R.drawable.angle_up) else viewBinding.iconExpandable.setImageResource(
            R.drawable.angle_down
        )

        viewBinding.apply {
            countMovie.text = "- $countMovies"
            nameGroupMovie.text = name
            overallRating.text = "- ${overallRating()}"
            root.setOnClickListener {
                expandableGroup.onToggleExpanded()
                if (expandableGroup.isExpanded) viewBinding.iconExpandable.setImageResource(R.drawable.angle_up) else viewBinding.iconExpandable.setImageResource(
                    R.drawable.angle_down
                )
            }
        }
    }

    override fun getLayout(): Int = R.layout.expandable_movie

    override fun initializeViewBinding(view: View): ExpandableMovieBinding =
        ExpandableMovieBinding.bind(view)

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other

    fun overallRating() : String {
        var rating = 0
        childMovieList.forEach { movie ->
            rating += movie.rating
        }
        val result = rating / childMovieList.size
        return (result / 10).toString() + "," + (result % 10).toString()
    }
}