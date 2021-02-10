package com.ikea.shoppable.di.modules.contributor

import com.ikea.shoppable.di.modules.viewmodel.ViewModelModule
import com.ikea.shoppable.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * class responsible for generating the android injectors for the activities, using dagger
 *
 * All the
 */
@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainActivity(): MainActivity
}