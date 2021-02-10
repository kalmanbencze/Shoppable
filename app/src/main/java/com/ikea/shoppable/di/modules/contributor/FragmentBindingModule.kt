package com.ikea.shoppable.di.modules.contributor

import com.ikea.shoppable.di.modules.viewmodel.ViewModelModule
import com.ikea.shoppable.view.cart.CartFragment
import com.ikea.shoppable.view.details.ProductFragment
import com.ikea.shoppable.view.list.ListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * class responsible for generating the android injectors for the activities, using dagger
 */
@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeForProductsList(): ListFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeForProductPage(): ProductFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeForCart(): CartFragment
}