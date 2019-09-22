package com.sorrowblue.koup.example.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sorrowblue.koup.example.R
import com.sorrowblue.koup.koup

//import com.sorrowblue.koup.example.preferences.Koup_UserPreference

class MainFragment : Fragment() {

	companion object {
		fun newInstance() = MainFragment()
	}

	private lateinit var viewModel: MainViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return inflater.inflate(R.layout.main_fragment, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
	}

}
