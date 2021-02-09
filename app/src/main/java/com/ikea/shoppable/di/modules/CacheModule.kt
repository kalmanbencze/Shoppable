package com.ikea.shoppable.di.modules

import android.content.Context
import com.ikea.shoppable.persistence.CartRepository
import com.ikea.shoppable.persistence.CartRepositoryImpl
import com.ikea.shoppable.persistence.ProductRepository
import com.ikea.shoppable.persistence.ProductRepositoryImpl
import com.ikea.shoppable.persistence.db.CacheDatabase
import com.ikea.shoppable.persistence.db.CartDao
import com.ikea.shoppable.persistence.db.ProductDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CacheModule {
    @Provides
    @Singleton
    fun providesCartRepository(cartDao: CartDao): CartRepository {
        return CartRepositoryImpl(cartDao)
    }

    @Provides
    fun providesCartDao(db: CacheDatabase): CartDao {
        return db.cartDao()
    }

    @Provides
    @Singleton
    fun providesProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(productDao)
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