package io.github.utkan.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import io.github.utkan.databinding.ActivityItemListBinding
import io.github.utkan.ui.screen.ItemListActivityView

@InstallIn(ActivityComponent::class)
@Module
abstract class ActivityBindingModule {
//    @Binds
//    abstract fun bindItemListActivityView(impl: ItemListActivityView.Impl): ItemListActivityView
}

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
    fun providePicasso(@ActivityContext context: Context): Picasso {
        return Picasso.Builder(context)
            .build()
    }
}
