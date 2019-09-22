package com.sorrowblue.koup

interface KoupProperty<T : Any> {
	val name: String
	val koupInfo: KoupInfo<T>
	val isCommit: Boolean
}
