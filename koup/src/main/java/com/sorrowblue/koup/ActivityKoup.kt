package com.sorrowblue.koup

import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class ActivityKoup<T : Any> internal constructor(
	override val name: String,
	override val koupInfo: KoupInfo<T>,
	override val isCommit: Boolean
) : ReadWriteProperty<FragmentActivity, T>, KoupProperty<T> {

	override fun setValue(thisRef: FragmentActivity, property: KProperty<*>, value: T) =
		thisRef.putKoup(koupInfo, value, isCommit, name)

	override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T =
		thisRef.getKoup(koupInfo, name)
}


fun <T : Any> FragmentActivity.koup(
	enumValue: KoupInfo<T>,
	isCommit: Boolean = false,
	name: String = ""
) =
	ActivityKoup(name, enumValue, isCommit)


fun <T : Any> FragmentActivity.getKoup(enumValue: KoupInfo<T>, name: String = "") =
	sharedPreferences(this, name).getValue(enumValue.name, enumValue.def)

fun <T : Any> FragmentActivity.putKoup(
	enumValue: KoupInfo<T>,
	value: T,
	isCommit: Boolean = false,
	name: String = ""
) = sharedPreferences(this, name).putValue(enumValue.name, value, isCommit)



