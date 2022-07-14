package top.memore.droid_jotter

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import top.memore.droid_jotter.locally.AppDatabase
import top.memore.droid_jotter.locally.LiteAccessible
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

//    @Provides
//    @Singleton
//    fun provideParentaline(): Parentaline = Parentaline()

//    @Provides
//    @Singleton
//    fun provideCloudAccessible(cloudeal: Cloudeal): CloudAccessible = CloudataAccessor(cloudeal)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getWorkDatabase(context)

    @Provides
    @Singleton
    fun provideLocalAccessible(db: AppDatabase): LiteAccessible = db.getAccessor()

}