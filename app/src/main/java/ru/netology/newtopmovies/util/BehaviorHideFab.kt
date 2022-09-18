package ru.netology.newtopmovies.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.netology.newtopmovies.R
import java.util.*
import kotlin.concurrent.schedule


class BehaviorHideFab(context: Context?, attrs: AttributeSet?) :
    HideBottomViewOnScrollBehavior<FloatingActionButton>(context, attrs) {

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed > 10) {
            slideDown(child)
        } else if (dyConsumed < -20) {
            child.visibility = View.VISIBLE
            slideUp(child)
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        type: Int
    ) {
        child.setOnClickListener {
            val recyclerView = coordinatorLayout.findViewById<RecyclerView>(R.id.recycle_movies)
            recyclerView.scrollToPosition(20)
            Handler(Looper.getMainLooper()).postDelayed({
                recyclerView.smoothScrollToPosition(0)
            }, 10)
            slideDown(child)
        }
    }
}


