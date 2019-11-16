package com.sorrowblue.koup.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sorrowblue.android.kotpref.putSharedPreference
import com.sorrowblue.android.kotpref.sharedPreference
import com.sorrowblue.koup.example.ui.main.MainFragment

class MainActivity : AppCompatActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
		if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
