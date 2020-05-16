package ru.ttk.beacon.di

import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import ru.ttk.beacon.ui.navigation.RouterType

private val fullScreenQualifier = RouterType.FULL_SCREEN.qualifier

val navModule = module {
    single(fullScreenQualifier) { Cicerone.create() }
    single(fullScreenQualifier) { get<Cicerone<Router>>(fullScreenQualifier).router }
    single(fullScreenQualifier) { get<Cicerone<Router>>(fullScreenQualifier).navigatorHolder }
}