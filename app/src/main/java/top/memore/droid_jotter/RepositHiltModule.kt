package top.memore.droid_jotter

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import top.memore.droid_jotter.datany.*

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositHiltModule {
//    @Binds
//    abstract fun bindCategoriesRepository(
//        categoriesRepositImpl: CategoriesRepositImpl
//    ): Repository<Category>
//
//    @Binds
//    abstract fun bindPlainotesRepository(
//        plainotesRepositImpl: PlainotesRepositImpl
//    ): Repository<Plainote>

    @Binds
    abstract fun bindLocalRepository(
        localRepositImpl: LocalRepositImpl
    ): LocalRepository
}
