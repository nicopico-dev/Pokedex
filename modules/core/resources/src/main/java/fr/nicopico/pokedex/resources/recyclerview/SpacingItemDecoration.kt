package fr.nicopico.pokedex.resources.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * Add spacing between each item of the RecyclerView
 */
class SpacingItemDecoration constructor(
    private val layoutManager: RecyclerView.LayoutManager,
    @field:Px private val spaceSize: Int,
    private val excludeFirstItemDecoration: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val halfSize: Int = spaceSize / 2
    private val columnCount =
        if (layoutManager is GridLayoutManager) layoutManager.spanCount else 1

    private val spanSizeLookup: SpanSizeLookup? by lazy {
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup
                .apply {
                    // Enable spanIndexCache to improve performance
                    isSpanIndexCacheEnabled = true
                }
        } else {
            null
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 && excludeFirstItemDecoration) {
            return
        }

        val firstColumn: Boolean
        val lastColumn: Boolean
        if (spanSizeLookup != null) {
            // Use spanSizeLookup as some items may span multiple columns
            val startSpanIndex = spanSizeLookup!!.getSpanIndex(position, columnCount)
            val endSpanIndex = startSpanIndex + spanSizeLookup!!.getSpanSize(position)
            firstColumn = startSpanIndex == 0
            lastColumn = endSpanIndex == columnCount
        } else {
            val column = position % columnCount
            firstColumn = column == 0
            lastColumn = column == columnCount - 1
        }

        // Spread horizontal spacing to keep the same width for each columns
        //
        // On a single column layout manager like LinearLayoutManager, both [firstColumn]
        // and [lastColumn] will be true
        outRect.set(
            if (firstColumn) 0 else halfSize,
            0,
            if (lastColumn) 0 else halfSize,
            spaceSize
        )
    }
}
