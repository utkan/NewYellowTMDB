package io.github.utkan.ui.screen.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.github.utkan.R
import io.github.utkan.ui.MovieViewItem
import javax.inject.Inject

@AndroidEntryPoint
class ItemDetailFragment : Fragment(R.layout.item_detail) {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var fragmentView: ItemDetailFragmentView

    private val item: MovieViewItem? by lazy {
        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                it.getParcelable(ARG_ITEM) as MovieViewItem?
            } else {
                null
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView.bind(view, item)
    }

    companion object {
        const val ARG_ITEM = "item"
        fun newInstance(item: MovieViewItem): ItemDetailFragment {
            return ItemDetailFragment().apply {
                arguments = bundleOf(ARG_ITEM to item)
            }
        }
    }
}
