package com.ikea.shoppable.persistence.db

import androidx.room.*
import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.model.Price
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface CartDao {

    @Transaction
    @Query("SELECT * FROM Products WHERE id = :id")
    fun getByProductId(id: String): Observable<CartItemProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: CartItem): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categories: List<CartItem>): Completable

    @Query("DELETE FROM Cart WHERE id IN (SELECT id FROM Cart WHERE productId = :id LIMIT 1)")
    fun remove(id: String): Completable

    @Query("DELETE FROM Cart WHERE productId = :id")
    fun removeAll(id: String): Completable

    @Transaction
    @Query("SELECT * FROM Products")
    fun getAll(): Observable<List<CartItemProduct>>

    @Query("SELECT COALESCE(SUM(count), 0) AS val FROM Cart")
    fun getSize(): Observable<Int>

    @Query("SELECT COALESCE(SUM(count), 0) AS val FROM Cart WHERE productId = :id")
    fun getProductCount(id: String): Observable<Int>

    @Query("DELETE FROM Cart")
    fun deleteAll(): Completable

    @Query(
        """
        SELECT COALESCE(SUM(Cart.count * Products.value), 0) 
            AS value, Products.currency 
        FROM Products LEFT JOIN Cart 
            ON Products.id = Cart.productId 
        """
    )
    fun getTotalValue(): Observable<Price>

}
