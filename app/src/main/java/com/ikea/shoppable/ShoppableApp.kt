package com.ikea.shoppable

import com.ikea.shoppable.di.AppComponent
import com.ikea.shoppable.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ShoppableApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}