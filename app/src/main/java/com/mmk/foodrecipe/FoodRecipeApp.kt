package com.mmk.foodrecipe

import android.app.Application
import com.mmk.foodrecipe.di.appModule
import com.mmk.foodrecipe.di.dataModule
import com.mmk.foodrecipe.di.domainModule
import com.mmk.foodrecipe.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class FoodRecipeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG) // Use Level.INFO or Level.NONE in release
            androidContext(this@FoodRecipeApp)
            modules(listOf(appModule, dataModule, domainModule, viewModelModule))
        }
    }
}