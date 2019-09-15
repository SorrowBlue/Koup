package com.sorrowblue.koup

abstract class KoupInfo<T : Any>(val def: T, private val key: String? = null) {
    abstract val prefix: String
    val name get() = "${prefix}-${key ?: javaClass.simpleName}"
}
