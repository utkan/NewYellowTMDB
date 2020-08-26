package io.github.utkan.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import io.github.utkan.R
import io.github.utkan.ui.MovieViewItem

//TODO: extract view
class ItemDetailFragment : Fragment() {

    private val item: MovieViewItem? by lazy {
        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                it.getParcelable(ARG_ITEM) as MovieViewItem?
            } else {
                null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item?.let {
            view.findViewById<TextView>(R.id.item_title).text = it.originalTitle
            view.findViewById<TextView>(R.id.item_detail).text = it.overview
            view.findViewById<TextView>(R.id.item_rating).text = it.voteCount.toString()
        }
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
