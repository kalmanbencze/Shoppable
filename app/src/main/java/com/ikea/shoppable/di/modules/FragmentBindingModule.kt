package com.ikea.shoppable.di.modules

import com.ikea.shoppable.view.cart.CartFragment
import com.ikea.shoppable.view.list.ListFragment
import com.ikea.shoppable.view.details.ProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * class responsible for generating the android injectors for the activities, using dagger
 */
@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector
    abstract fun contributeForProductsList(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributeForProductPage(): ProductFragment

    @ContributesAndroidInjector
    abstract fun contributeForCart(): CartFragment
}