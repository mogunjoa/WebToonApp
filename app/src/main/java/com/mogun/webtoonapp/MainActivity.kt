package com.mogun.webtoonapp

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.mogun.webtoonapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnTabLayoutNameChanged {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(WebViewFragment.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val tab0 = sharedPreferences.getString("tab0_name", "월요웹툰")
        val tab1 = sharedPreferences.getString("tab1_name", "화요웹툰")
        val tab2 = sharedPreferences.getString("tab2_name", "수요웹툰")

        // 뷰페이저에 어댑터 연결
        binding.viewPager.adapter = ViewPagerAdapter(this)

        // 뷰페이저에 탭 레이아웃 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            run {
                tab.text = when(position) {
                    0 -> tab0
                    1 -> tab1
                    else -> tab2
                }
            }
        }.attach()

        // 뒤로가기 동작 제어
        onBackCallBack()
    }

    private fun onBackCallBack() {
        this@MainActivity.onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                val currentFragment = supportFragmentManager.fragments[binding.viewPager.currentItem]
                if (currentFragment is WebViewFragment) {
                    if (currentFragment.canGoBack()) {
                        currentFragment.goBack()
                    } else {
                        isEnabled = false
                    }
                }
            } else {
                isEnabled = false
            }
        }
    }

    override fun nameChanged(position: Int, name: String) {
        val tab = binding.tabLayout.getTabAt(position)
        tab?.text = name
    }
}