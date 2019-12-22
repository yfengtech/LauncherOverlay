package com.yf.launcheroverlay.support;

/**
 * Created by yf.
 *
 * @date 2019-12-22
 */
public interface LauncherClientCallbacks {
    void onOverlayScrollChanged(float progress);

    void onServiceStateChanged(boolean overlayAttached, boolean hotwordActive);

}
