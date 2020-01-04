package cn.yfengtech.launcheroverlay

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import cn.yfengtech.launcheroverlay.support.LauncherClient
import cn.yfengtech.launcheroverlay.support.LauncherClientCallbacks
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "LauncherTag"

class Launcher : AppCompatActivity() {

    private val client by lazy {
        LauncherClient(
            this,
            object :
                LauncherClientCallbacks {
                override fun onOverlayScrollChanged(progress: Float) {
                    Log.d(
                        TAG,
                        "onOverlayScrollChanged progress :$progress"
                    )
                }

                override fun onServiceStateChanged(
                    overlayAttached: Boolean,
                    hotwordActive: Boolean
                ) {
                    Log.d(
                        TAG,
                        "onServiceStateChanged"
                    )
                }
            },
            true
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment =
                DemoFragment.newInstance(
                    position
                )


            override fun getCount(): Int = 2
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset > 0) {
                    Log.d(TAG, "onPageScrolled positionOffset : $positionOffset")
                    client.updateMove(1 - positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })
        viewPager.currentItem = 1
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        client.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        client.onDetachedFromWindow()
    }


    override fun onPause() {
        super.onPause()
        client.onPause()
    }

    override fun onResume() {
        super.onResume()
        client.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        client.onDestroy()
    }
}
