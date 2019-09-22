package com.sorrowblue.koup

abstract class KoupInfo<T : Any>(val def: T, private val key: String = "") {
	abstract val prefix: String
	val name: String
		get() = "${if (prefix.isNotBlank()) "$prefix-" else ""}${if (key.isNotBlank()) key else javaClass.simpleName}"
}
