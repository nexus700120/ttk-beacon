package ru.ttk.beacon.ui.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import ru.ttk.beacon.ui.common.LifecycleViewModel
import kotlin.reflect.KClass

@MainThread
inline fun <reified VM : LifecycleViewModel> Fragment.lifecycleViewModel(
    noinline vmBuilder: (() -> VM)? = null
): Lazy<VM> = LifeCycleViewModelLazy(
    lifecycle = lifecycle,
    viewModelClass = VM::class,
    storeProducer = { viewModelStore },
    factoryProducer = {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                vmBuilder?.invoke() as T ?: defaultViewModelProviderFactory.create(VM::class.java) as T
        }
    }
)

class LifeCycleViewModelLazy<VM : LifecycleViewModel>(
    private val lifecycle: Lifecycle,
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {

    private var cached: VM? = null

    override val value: VM
        get() = cached ?: ViewModelProvider(storeProducer(), factoryProducer())
            .get(viewModelClass.java).also {
                cached = it
                lifecycle.addObserver(it)
            }

    override fun isInitialized() = cached != null
}