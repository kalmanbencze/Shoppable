package com.ikea.shoppable.di.modules

import android.content.Context
import com.ikea.shoppable.ShoppableApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun providesContext(app: ShoppableApp): Context {
        return app
    }
}