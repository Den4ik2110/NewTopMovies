package ru.netology.newtopmovies.data

import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.view.View
import androidx.fragment.app.FragmentManager
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.HeaderFranchiseBinding
import ru.netology.newtopmovies.view.AddFranchiseFragment
import ru.netology.newtopmovies.view.DeleteMovieDialogFragment
import ru.netology.newtopmovies.viewModel.MovieViewModel

class HeaderFranchise(
    private val viewModel: MovieViewModel,
    private val fragmentManager: FragmentManager
) : BindableItem<HeaderFranchiseBinding>() {

    override fun bind(viewBinding: HeaderFranchiseBinding, position: Int) {

        viewBinding.root.setOnClickListener {
            val dialog = AddFranchiseFragment(viewModel)
            dialog.show(fragmentManager, "keyThree")
        }
    }

    override fun getLayout(): Int = R.layout.header_franchise

    override fun initializeViewBinding(view: View): HeaderFranchiseBinding {
        return HeaderFranchiseBinding.bind(view)
    }
}