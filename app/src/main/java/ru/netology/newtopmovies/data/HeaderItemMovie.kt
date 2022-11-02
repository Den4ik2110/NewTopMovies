package ru.netology.newtopmovies.data

import android.view.View
import androidx.fragment.app.FragmentManager
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import ru.netology.newtopmovies.R
import ru.netology.newtopmovies.databinding.HeaderItemBinding
import ru.netology.newtopmovies.util.Constants
import ru.netology.newtopmovies.util.SheetBottomMenuAndInfo
import ru.netology.newtopmovies.util.SheetBottomSort

class HeaderItemMovie(
    private val key: String,
    private val fragmentManager: FragmentManager
) : BindableItem<HeaderItemBinding>() {

    override fun bind(viewBinding: HeaderItemBinding, position: Int) {
        viewBinding.sortType.text = when (key) {
            Constants.MAX_MIN -> "По убыванию рейтинга"
            Constants.MIN_MAX -> "По возрастанию рейтинга"
            Constants.OLD_NEW -> "От старых до новых"
            Constants.REPEAT -> "По пересмотру"
            Constants.ALPHABET -> "По алфавиту"
            else -> "От новых до старых"
        }

        viewBinding.sortType.setOnClickListener {
            val sheetBottomSort = SheetBottomSort()
            sheetBottomSort.show(fragmentManager, SheetBottomMenuAndInfo.TAG)
        }
    }

    override fun getLayout(): Int = R.layout.header_item

    override fun initializeViewBinding(view: View): HeaderItemBinding {
        return HeaderItemBinding.bind(view)
    }
}