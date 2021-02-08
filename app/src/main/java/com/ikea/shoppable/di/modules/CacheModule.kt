package com.ikea.shoppable.di.modules

import android.content.Context
import com.ikea.shoppable.persistence.ProductCache
import com.ikea.shoppable.persistence.ProductCacheImplementation
import com.ikea.shoppable.persistence.db.CacheDatabase
import com.ikea.shoppable.persistence.db.ProductDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CacheModule {
    @Provides
    @Singleton
    fun providesProductCache(productDao: ProductDao): ProductCache {
        return ProductCacheImplementation(productDao)
    }

    @Provides
    fun providesProductDao(db: CacheDatabase): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun providesCacheDB(context: Context): CacheDatabase {
        return CacheDatabase.getInstance(context)
    }
}