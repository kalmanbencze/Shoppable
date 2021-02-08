package com.ikea.shoppable.di.modules

import com.ikea.shoppable.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * class responsible for generating the android injectors for the activities, using dagger
 *
 * All the
 */
@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}