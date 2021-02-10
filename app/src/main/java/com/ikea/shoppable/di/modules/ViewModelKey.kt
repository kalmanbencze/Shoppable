package com.ikea.shoppable.di.modules

import androidx.lifecycle.ViewModel

import dagger.MapKey
import java.lang.annotation.Documented
import kotlin.reflect.KClass


@Documented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)