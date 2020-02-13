package page.shellcore.tech.android.mvvmkotlin.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import page.shellcore.tech.android.mvvmkotlin.MainActivity

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity
}