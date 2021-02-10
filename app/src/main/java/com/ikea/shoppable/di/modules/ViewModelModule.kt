package com.ikea.shoppable.di.modules

import androidx.lifecycle.ViewModel
import com.ikea.shoppable.view.AppBarViewModel
import com.ikea.shoppable.view.details.ProductViewModel
import com.ikea.shoppable.view.list.ListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindsProductViewModel(viewModel: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppBarViewModel::class)
    abstract fun bindsAppBarViewModel(viewModel: AppBarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindsListViewModel(viewModel: ListViewModel): ViewModel
}