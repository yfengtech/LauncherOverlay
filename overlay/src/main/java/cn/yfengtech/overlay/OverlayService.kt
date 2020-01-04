package cn.yfengtech.overlay

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.*
import com.google.android.ILauncherOverlay
import com.google.android.ILauncherOverlayCallback
import com.yf.overlay.R

private const val TAG = "OverlayService"

/**
 * 模拟launcher中负一屏设计，提供service，绑定后可以提供负一屏功能，本身独立存在
 *
 * 内部UI实际是一个Window，通过宿主window的token，实现共同显示的功能
 *
 * Created by yf.
 * @date 2019-12-22
 */
class OverlayService : Service() {

    private val WIDTH by lazy { Resources.getSystem().displayMetrics.widthPixels.toFloat() }

    private val mContext = this

    /**
     * 管理上层window
     */
    private var windowManager: WindowManager? = null

    lateinit var mWindow: Window

    private var container: View? = null

    private val mHandler = Handler(Looper.getMainLooper())


    override fun onBind(intent: Intent?): IBinder? {
        return mRemote
    }

    /**
     * clear this window's focus
     */
    fun clearFocus() {
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        val params = mWindow.attributes
        windowManager?.updateViewLayout(container, params)
    }


    /**
     * request this window's focus
     */
    fun requestFocus() {
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        val params = mWindow.attributes
        windowManager?.updateViewLayout(container, params)
    }


    private val mRemote = object : ILauncherOverlay.Stub() {

        private var callbacks: ILauncherOverlayCallback? = null
        override fun closeOverlay(options: Int) {
        }

        override fun endScroll() {
        }

        override fun onPause() {
        }

        override fun onResume() {
        }

        override fun onScroll(progress: Float) {
            Log.d(TAG, "onScroll: $progress")
            mHandler.post {
                container?.translationX = (progress - 1) * WIDTH
                // callback to launcher
                callbacks?.overlayScrollChanged(progress)
            }

        }

        override fun openOverlay(options: Int) {

        }

        override fun startScroll() {

        }

        override fun windowAttached(
            attrs: WindowManager.LayoutParams,
            callbacks: ILauncherOverlayCallback,
            options: Int
        ) {
            mHandler.post {
                val window = Dialog(applicationContext).window!!
                window.setWindowManager(null, attrs.token, "overlay", true)
                windowManager = window.windowManager

                if (windowManager != null) {

                    val params = attrs.apply {
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.MATCH_PARENT
                        flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        gravity = Gravity.START
                        type = WindowManager.LayoutParams.TYPE_APPLICATION
                        format = PixelFormat.TRANSLUCENT
//                        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        window.attributes = this
                    }

                    container =
                        LayoutInflater.from(mContext).inflate(R.layout.layout_overlay, null)

                    windowManager?.addView(container, params)
                    container?.translationX = WIDTH * -1

                    mWindow = window

                    callbacks.overlayStatusChanged(1)
                }
            }
        }

        override fun windowDetached(isChangingConfigurations: Boolean) {

        }

        override fun asBinder(): IBinder {
            return this
        }
    }
}