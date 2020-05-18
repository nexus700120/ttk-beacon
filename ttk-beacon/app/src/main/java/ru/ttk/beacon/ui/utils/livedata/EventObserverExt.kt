package ru.ttk.beacon.ui.utils.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner, EventObserver { onEventUnhandledContent(it) })
}

inline fun LiveData<UnitEvent>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: () -> Unit) {
    observe(owner, EventObserver { onEventUnhandledContent() })
}