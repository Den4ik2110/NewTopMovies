package ru.netology.newtopmovies.data



import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.OneFranchiseBinding
import ru.netology.newtopmovies.view.ActionFranchiseFragment
import ru.netology.newtopmovies.view.AddFranchiseFragment
import ru.netology.newtopmovies.viewModel.MovieViewModel

class FranchiseOnFromList(
    private val franchise: Franchise,
    private val viewModel: MovieViewModel,
    private val fragmentManager: FragmentManager,
    private val movie: Movie,
    private val fragment: Fragment
) : BindableItem<OneFranchiseBinding>() {

    override fun bind(viewBinding: OneFranchiseBinding, position: Int) {
        viewBinding.franchiseName.text = franchise.title
        viewBinding.root.setOnClickListener {
            val dialog = ActionFranchiseFragment(viewModel, franchise, movie, fragment)
            dialog.show(fragmentManager, "keyFour")
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