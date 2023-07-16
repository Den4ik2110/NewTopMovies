package ru.netology.newtopmovies.data





import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.OneGenreBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.singular


class OneGenre(
    private val id: Long,
    private val name: String
) : BindableItem<OneGenreBinding>() {

    override fun bind(viewBinding: OneGenreBinding, position: Int) {


        viewBinding.textView11.text = name

        viewBinding.root.setOnClickListener {
            val bundle = bundleOf("keyType" to Constants.KEY_GENRE, "keyMovies" to name.singular())
            it.findNavController().navigate(R.id.fragmentAllMovies, bundle)
        }
    }

    override fun getLayout() = R.layout.one_genre

    override fun initializeViewBinding(view: View): OneGenreBinding {
        return OneGenreBinding.bind(view)
    }

    override fun getId(): Long = id

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other
}