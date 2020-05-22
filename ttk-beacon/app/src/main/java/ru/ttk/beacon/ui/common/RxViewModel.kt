package ru.ttk.beacon.ui.common

import androidx.annotation.CallSuper
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class RxViewModel : LifecycleViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var foregroundDisposable = CompositeDisposable()

    @CallSuper
    override fun onResume() {
        if (foregroundDisposable.isDisposed) {
            foregroundDisposable = CompositeDisposable()
        }
    }

    @CallSuper
    override fun onPause() {
        foregroundDisposable.dispose()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun Disposable.unsubscribeOnCleared(): Disposable = apply {
        compositeDisposable.add(this)
    }

    protected fun Disposable.unsubscribeOnPause(): Disposable = apply {
        compositeDisposable.add(this)
    }
}