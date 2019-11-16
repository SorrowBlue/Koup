package com.sorrowblue.koup.example.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProviders
import com.sorrowblue.koup.example.R
import com.sorrowblue.koup.example.databinding.MainFragmentBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MainFragment : Fragment() {

	companion object {
		fun newInstance() = MainFragment()
	}

	private lateinit var viewModel: MainViewModel
	private val binding by BindingDelegate()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
		binding.viewModel = viewModel
	}
}

class BindingDelegate : ReadOnlyProperty<MainFragment, MainFragmentBinding> {
	private var binding: MainFragmentBinding? = null
	override fun getValue(thisRef: MainFragment, property: KProperty<*>): MainFragmentBinding {
		return binding
			?: MainFragmentBinding.inflate(LayoutInflater.from(thisRef.requireContext())).also {
				binding = it
				it.lifecycleOwner =  thisRef.viewLifecycleOwner
				thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
					@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
					fun onDestroy() {
						thisRef.viewLifecycleOwner.lifecycle.removeObserver(this)
						binding = null
					}
				})
			}
	}

}
