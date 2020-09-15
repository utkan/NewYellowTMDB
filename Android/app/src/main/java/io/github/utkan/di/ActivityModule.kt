package io.github.utkan.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import io.github.utkan.common.Mapper
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.databinding.ActivityItemDetailBinding
import io.github.utkan.databinding.ActivityItemListBinding
import io.github.utkan.ui.MovieViewItem
import io.github.utkan.ui.screen.detail.ItemDetailActivityView
import io.github.utkan.ui.screen.detail.ItemDetailFragmentView
import io.github.utkan.ui.screen.list.ItemListActivityView
import io.github.utkan.ui.screen.list.SearchUseCase

@InstallIn(ActivityComponent::class)
@Module
object ActivityProvideModule {

    @Provides
    fun provideItemListActivityView(
        @ActivityContext context: Context,
        picasso: Picasso
    ): ItemListActivityView {
        val activity = context as FragmentActivity
        return ItemListActivityView.Impl(
            activity = activity,
            viewBinding = ActivityItemListBinding.inflate(activity.layoutInflater),
            picasso = picasso
        )
    }

    @Provides
    fun provideSearchUseCase(
        pagingConfig: PagingConfig,
        @MovieSearchSource pagingSearchSource: PagingSource<Int, Movie>,
        @MovieItemMapper mapper: Mapper<Movie, MovieViewItem>,
    ): SearchUseCase {
        return SearchUseCase.Impl(
            pagingConfig = pagingConfig,
            pagingSearchSource = pagingSearchSource,
            mapper = mapper
        )
    }

    @Provides
    fun provideItemDetailActivityView(
        @ActivityContext context: Context,
        picasso: Picasso
    ): ItemDetailActivityView {
        val activity = context as FragmentActivity
        return ItemDetailActivityView.Impl(
            activity = activity,
            viewBinding = ActivityItemDetailBinding.inflate(activity.layoutInflater),
            picasso = picasso
        )
    }

    @Provides
    fun provideItemDetailFragmentView(
        picasso: Picasso
    ): ItemDetailFragmentView {
        return ItemDetailFragmentView.Impl(picasso)
    }
}
