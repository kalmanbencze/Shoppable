package com.ikea.shoppable.di

import com.ikea.shoppable.ShoppableApp
import com.ikea.shoppable.di.modules.ActivityBindingModule
import com.ikea.shoppable.di.modules.AppModule
import com.ikea.shoppable.di.modules.CacheModule
import com.ikea.shoppable.di.modules.FragmentBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = arrayOf(
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        CacheModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
    ))
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: ShoppableApp)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: ShoppableApp): Builder

        fun build(): AppComponent
    }
}