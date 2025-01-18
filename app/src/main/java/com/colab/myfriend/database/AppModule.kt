package com.colab.myfriend.database

import android.content.Context
import androidx.room.Room
import com.colab.myfriend.Api.ApiAuthService
import com.colab.myfriend.ApiServiceProduct
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.NetworkHelper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoreSession(@ApplicationContext context: Context): CoreSession {
        return CoreSession(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideApiServiceProduct(): ApiServiceProduct {
        return NetworkHelper.provideApiService(
            baseUrl = "https://dummyjson.com/",
            okHttpClient = NetworkHelper.provideOkHttpClient(),
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(session: CoreSession): OkHttpClient {
        return NetworkHelper.provideOkHttpClient().newBuilder().apply {
            addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)

                val token = session.getString(CoreSession.PREF_UID)
                if (token.isNotEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }

                val request = requestBuilder.build()
                it.proceed(request)
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiAuthService(okHttpClient: OkHttpClient): ApiAuthService {
        return NetworkHelper.provideApiService(
            baseUrl = "https://kelas-industri.crocodic.net/rubben/Shoppku/public/api/v1/",
            okHttpClient = okHttpClient,
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }
}
