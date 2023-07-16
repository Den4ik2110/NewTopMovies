package ru.netology.newtopmovies.data



import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.OneFranchiseBinding
import ru.netology.newtopmovies.util.Constants

class FranchiseOnFromList(
    private val id: Long,
    private val name: String
) : BindableItem<OneFranchiseBinding>() {

    override fun bind(viewBinding: OneFranchiseBinding, position: Int) {
        viewBinding.textView10.text = name

        viewBinding.root.setOnClickListener {
            val bundle = bundleOf("keyType" to Constants.KEY_FRANCHISE, "keyMovies" to name)
            it.findNavController().navigate(R.id.fragmentAllMovies, bundle)
        }
    }

    override fun getLayout() = R.layout.one_franchise

    override fun initializeViewBinding(view: View): OneFranchiseBinding {
        return OneFranchiseBinding.bind(view)
    }

    override fun getId(): Long = id

    override fun isSameAs(other: Item<*>): Boolean = viewType == other.viewType

    override fun hasSameContentAs(other: Item<*>): Boolean = this == other
}