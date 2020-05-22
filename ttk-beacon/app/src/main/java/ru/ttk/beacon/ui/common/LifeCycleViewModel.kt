package ru.ttk.beacon.ui.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel

open class LifecycleViewModel : ViewModel(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onPause() = Unit
}