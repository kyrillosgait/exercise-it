package com.kyril.gymondotest.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.kyril.gymondotest.api.WgerService
import com.kyril.gymondotest.data.WgerRepository
import com.kyril.gymondotest.db.AppDatabase
import com.kyril.gymondotest.db.WgerLocalCache
import com.kyril.gymondotest.ui.main.ViewModelFactory
import java.util.concurrent.Executors


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [GithubLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): WgerLocalCache {
        val database = AppDatabase.getInstance(context)
        return WgerLocalCache(database.exerciseDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideWgerRepository(context: Context): WgerRepository {
        return WgerRepository(WgerService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideWgerRepository(context))
    }
}
