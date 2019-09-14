package com.sorrowblue.preflin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


@Suppress("unused")
fun <T : Any> FragmentActivity.preflin(enumValue: PreflinInfo<T>, isCommit: Boolean = false) =
    ActivityPreflin(enumValue, isCommit)

@Suppress("unused")
fun <T : Any> Fragment.preflin(enumValue: PreflinInfo<T>, isCommit: Boolean = false) =
    FragmentPreflin(enumValue, isCommit)
