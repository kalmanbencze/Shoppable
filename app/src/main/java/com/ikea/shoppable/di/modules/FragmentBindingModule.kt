package com.ikea.shoppable.di.modules

import com.ikea.shoppable.ListFragment
import com.ikea.shoppable.ProductFragment
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
}