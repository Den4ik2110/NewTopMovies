package ru.netology.newtopmovies.data



import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.OneFranchiseBinding
import ru.netology.newtopmovies.util.SheetBottomFranchise
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FranchiseOnFromList(
    private val franchise: Franchise,
    private val viewModel: MovieViewModel,
    private val movie: Movie,
    private val fragmentManager: FragmentManager,
    private val fragment: SheetBottomFranchise
) : BindableItem<OneFranchiseBinding>() {

    override fun bind(viewBinding: OneFranchiseBinding, position: Int) {
        viewBinding.franchiseName.text = franchise.title
        viewBinding.root.setOnClickListener {
            viewModel.addToFranchise(franchise, movie)
            fragmentManager.beginTransaction().remove(fragment).commit()
            Toast.makeText(fragment.requireContext(), "Фильм добавлен к франшизе \"${franchise.title}\"", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayout() = R.layout.one_franchise

    override fun initializeViewBinding(view: View): OneFranchiseBinding {
        return OneFranchiseBinding.bind(view)
    }

    override fun getId(): Long = franchise.id

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other
}