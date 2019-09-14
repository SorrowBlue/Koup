package com.sorrowblue.preflin

abstract class PreflinInfo<T : Any>(val def: T, private val keyName: String? = null) {
    abstract val prefix: String
    val name get() = "${prefix}-${keyName ?: javaClass.simpleName}"
}
