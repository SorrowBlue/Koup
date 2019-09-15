@file:Suppress("unused")

package com.sorrowblue.koup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun <T : Any> Fragment.koup(enumValue: KoupInfo<T>, isCommit: Boolean = false) =
	FragmentKoup("", enumValue, isCommit)

fun <T : Any> Fragment.koup(name: String, enumValue: KoupInfo<T>, isCommit: Boolean = false) =
	FragmentKoup(name, enumValue, isCommit)

fun <T : Any> FragmentActivity.koup(enumValue: KoupInfo<T>, isCommit: Boolean = false) =
	ActivityKoup("", enumValue, isCommit)

fun <T : Any> FragmentActivity.koup(name: String, enumValue: KoupInfo<T>, isCommit: Boolean = false) =
	ActivityKoup(name, enumValue, isCommit)
