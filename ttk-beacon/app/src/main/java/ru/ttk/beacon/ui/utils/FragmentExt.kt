package ru.ttk.beacon.ui.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ttk.beacon.ui.common.LifecycleViewModel

@MainThread
inline fun <reified VM : LifecycleViewModel> Fragment.lifecycleViewModel(
    noinline vmCreator: (() -> VM)? = null
): Lazy<VM> = lazy {
    ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return (vmCreator?.invoke() ?: ViewModelProvider.NewInstanceFactory().create(modelClass)) as T
        }
    }).get(VM::class.java).also { lifecycle.addObserver(it) }
}