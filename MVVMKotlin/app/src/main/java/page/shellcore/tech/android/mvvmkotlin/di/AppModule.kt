package page.shellcore.tech.android.mvvmkotlin.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import page.shellcore.tech.android.mvvmkotlin.api.GithubApi
import page.shellcore.tech.android.mvvmkotlin.db.GithubDB
import page.shellcore.tech.android.mvvmkotlin.db.RepoDao
import page.shellcore.tech.android.mvvmkotlin.db.UserDao
import page.shellcore.tech.android.mvvmkotlin.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }

    @Singleton
    @Provides
    fun provideGithubApi(): GithubApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
        .create(GithubApi::class.java)

    @Singleton
    @Provides
    fun providesDb(app: Application): GithubDB =
        Room.databaseBuilder(app, GithubDB::class.java, "github.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesUserDao(db: GithubDB): UserDao = db.userDao()

    @Singleton
    @Provides
    fun providesRepoDao(db: GithubDB): RepoDao = db.repoDao()
}