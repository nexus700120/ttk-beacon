package ru.ttk.beacon.ui.common

import androidx.annotation.CallSuper
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class RxViewModel : LifecycleViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var foregroundDisposable = CompositeDisposable()

    private var isResumeCalled = false

    @CallSuper
    override fun onResume() {
        if (foregroundDisposable.isDisposed) {
            foregroundDisposable = CompositeDisposable()
        }
        isResumeCalled = true
    }

    @CallSuper
    override fun onPause() {
        isResumeCalled = false
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
        require(isResumeCalled) { "Method can only be called after onResume" }
        compositeDisposable.add(this)
    }
}