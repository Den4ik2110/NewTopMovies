package ru.netology.newtopmovies.data

import android.view.View
import androidx.fragment.app.FragmentManager
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.WishMovieItemBinding
import ru.netology.newtopmovies.util.SheetBottomWishMovieEdit

class WishMovieItem(
    private val wishMovie: WishMovie,
    private val fragmentManager: FragmentManager
) : BindableItem<WishMovieItemBinding>() {
    override fun bind(viewBinding: WishMovieItemBinding, position: Int) {
        viewBinding.wishMovieTitle.text = wishMovie.title
        viewBinding.wishMovieYear.text = "${wishMovie.year} г."

        viewBinding.root.setOnClickListener {
            val sheetBottomWishMovieEdit = SheetBottomWishMovieEdit(wishMovie)
            sheetBottomWishMovieEdit.show(fragmentManager, SheetBottomWishMovieEdit.TAG)
        }
    }

    override fun getLayout(): Int = R.layout.wish_movie_item

    override fun initializeViewBinding(view: View): WishMovieItemBinding {
        return WishMovieItemBinding.bind(view)
    }

    override fun getId(): Long = wishMovie.id

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other
}