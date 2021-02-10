package com.ikea.shoppable.di.modules

import androidx.lifecycle.ViewModelProvider
import com.ikea.shoppable.view.common.ViewModelProviderFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}