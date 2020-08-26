package io.github.utkan.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.utkan.databinding.LoadStateViewBinding

class MovieLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MovieLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            viewBinding = LoadStateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            retry = retry
        )
    }

    class LoadStateViewHolder(
        private val viewBinding: LoadStateViewBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(loadState: LoadState) {
            val isLoading = loadState is LoadState.Loading
            viewBinding.apply {
                loadStateProgress.isVisible = isLoading
                loadStateErrorMessage.apply {
                    isVisible = isLoading.not()
                    if (loadState is LoadState.Error) {
                        text = loadState.error.localizedMessage
                    }
                }
                loadStateRetry.apply {
                    isVisible = isLoading.not()
                    setOnClickListener {
                        retry()
                    }
                }
            }
        }
    }
}
