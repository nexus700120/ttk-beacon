package ru.ttk.beacon.ui.common

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class RxViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    protected fun Disposable.bindToLifeCycle(): Disposable = apply {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}