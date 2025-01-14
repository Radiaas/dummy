package com.colab.myfriend.database

import android.content.Context
import com.colab.myfriend.Api.ApiService
import com.colab.myfriend.ApiServiceProduct
import com.colab.myfriend.adapter.UserDao
import com.colab.myfriend.repository.DataProductsRepo
import com.colab.myfriend.repository.ImplDataProductRepo
import com.crocodic.core.helper.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }



    @Provides
    @Singleton
    fun provideDataProductsRepo(apiServiceProduct: ApiServiceProduct): DataProductsRepo {
        return ImplDataProductRepo(apiServiceProduct)
    }

    @Singleton
    @Provides
    fun provideApiServiceProduct(): ApiServiceProduct {
        return NetworkHelper.provideApiService(
            baseUrl = "https://dummyjson.com/",
            okHttpClient = NetworkHelper.provideOkHttpClient(),
            converterFactory = listOf(GsonConverterFactory.create())
        )
    }

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://neptune74.crocodic.net/myfriend-kelasindustri/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}