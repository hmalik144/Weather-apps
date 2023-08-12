package com.appttude.h_mal.atlas_weather.helper

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

object GenericsHelper {
    @Suppress("UNCHECKED_CAST")
    fun <CLASS : Any> Any.getGenericClassAt(position: Int): KClass<CLASS> =
        ((javaClass.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments?.getOrNull(position) as? Class<CLASS>)
            ?.kotlin
            ?: throw IllegalStateException("Can not find class from generic argument")

//    @Suppress("UNCHECKED_CAST")
//    fun <CLASS : Any> Any.getGenericClassInMethod(position: Int): KClass<CLASS> =
//        ((javaClass.methods as? ParameterizedType)
//            ?.actualTypeArguments?.getOrNull(position) as? Class<CLASS>)
//            ?.kotlin
//            ?: throw IllegalStateException("Can not find class from generic argument")

//    /**
//     * Create a view binding out of the the generic [VB]
//     *
//     * @sample inflateBindingByType(getGenericClassAt(0), layoutInflater)
//     */
//    fun <VB: ViewBinding> inflateBindingByType(
//        genericClassAt: KClass<VB>,
//        layoutInflater: LayoutInflater
//    ): VB = try {
//        @Suppress("UNCHECKED_CAST")
//
//        genericClassAt.java.methods.first { viewBinding ->
//            viewBinding.parameterTypes.size == 1
//                    && viewBinding.parameterTypes.getOrNull(0) == LayoutInflater::class.java
//        }.invoke(null, layoutInflater) as VB
//    } catch (exception: Exception) {
//        println ("generic class failed at = $genericClassAt")
//        exception.printStackTrace()
//        throw IllegalStateException("Can not inflate binding from generic")
//    }
//
//    fun <VB: ViewBinding> LayoutInflater.inflateBindingByType(
//        container: ViewGroup?,
//        genericClassAt: KClass<VB>
//    ): VB = try {
//        @Suppress("UNCHECKED_CAST")
//        genericClassAt.java.methods.first { inflateFun ->
//            inflateFun.parameterTypes.size == 3
//                    && inflateFun.parameterTypes.getOrNull(0) == LayoutInflater::class.java
//                    && inflateFun.parameterTypes.getOrNull(1) == ViewGroup::class.java
//                    && inflateFun.parameterTypes.getOrNull(2) == Boolean::class.java
//        }.invoke(null, this, container, false) as VB
//    } catch (exception: Exception) {
//        throw IllegalStateException("Can not inflate binding from generic")
//    }
}