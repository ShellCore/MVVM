package page.shellcore.tech.android.mvvmkotlin.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import page.shellcore.tech.android.mvvmkotlin.ui.repo.RepoFragment
import page.shellcore.tech.android.mvvmkotlin.ui.search.SearchFragment
import page.shellcore.tech.android.mvvmkotlin.ui.user.UserFragment

@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeRepoFragment(): RepoFragment

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

}