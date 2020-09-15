package io.github.utkan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.github.utkan.R
import io.github.utkan.databinding.MovieItemBinding
import io.github.utkan.ui.MovieViewItem
import io.github.utkan.ui.screen.list.MovieModel

typealias OnMovieClickListener = (MovieViewItem) -> Unit
typealias ImageUrlGenerator = (String?) -> String

class MovieAdapter constructor(
    private val picasso: Picasso,
    private val onMovieClickListener: OnMovieClickListener,
    private val imageUrlGenerator:ImageUrlGenerator
) : PagingDataAdapter<MovieModel, MovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movieModel: MovieModel? = getItem(position)

        (movieModel as? MovieModel.MovieItem)?.let { item ->
            // TODO: fetch configuration and choose size in respect to device resolution and
            //  portrait or landscape, can also be loaded progressively
            //  base image url by configuration
            val movie = item.movie
            holder.bind(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            viewBinding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            picasso = picasso,
            onMovieClickListener = onMovieClickListener,
            imageUrlGenerator = imageUrlGenerator
        )
    }

    class MovieViewHolder(
        private val viewBinding: MovieItemBinding,
        private val picasso: Picasso,
        private val onMovieClickListener: OnMovieClickListener,
        private val imageUrlGenerator: ImageUrlGenerator
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(movie: MovieViewItem) {
            viewBinding.apply {
                picasso
                    .load(imageUrlGenerator(movie.backdropUrl))
                    .into(movieImage)
                movieTitle.text = movie.originalTitle
                movieVoteCount.text = root.resources.getString(R.string.vote_count, movie.voteCount.toString())
                root.setOnClickListener {
                    onMovieClickListener(movie)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
                return (oldItem is MovieModel.MovieItem && newItem is MovieModel.MovieItem &&
                        oldItem.movie.id == newItem.movie.id)
            }

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem == newItem
        }
    }
}
