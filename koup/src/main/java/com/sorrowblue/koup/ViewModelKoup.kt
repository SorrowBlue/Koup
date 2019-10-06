package com.sorrowblue.koup

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewModelKoup<T : Any> internal constructor(
	override val name: String,
	override val koupInfo: KoupInfo<T>,
	override val isCommit: Boolean
) : ReadWriteProperty<AndroidViewModel, T>, KoupProperty<T> {
	override fun getValue(thisRef: AndroidViewModel, property: KProperty<*>): T =
		thisRef.getKoup(koupInfo, name)

	override fun setValue(thisRef: AndroidViewModel, property: KProperty<*>, value: T) =
		thisRef.putKoup(koupInfo, value, isCommit, name)
}

fun <T : Any> ViewModel.koup(enumValue: KoupInfo<T>, isCommit: Boolean = false, name: String = "") =
	ViewModelKoup(name, enumValue, isCommit)

fun <T : Any> AndroidViewModel.getKoup(enumValue: KoupInfo<T>, name: String = "") =
	sharedPreferences(getApplication(), name).getValue(enumValue.name, enumValue.def)

fun <T : Any> AndroidViewModel.putKoup(
	enumValue: KoupInfo<T>,
	value: T,
	isCommit: Boolean = false,
	name: String = ""
) = sharedPreferences(getApplication(), name).putValue(enumValue.name, value, isCommit)

