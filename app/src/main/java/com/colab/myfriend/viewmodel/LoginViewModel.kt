package com.colab.myfriend.viewmodel

import androidx.lifecycle.viewModelScope
import com.colab.myfriend.Api.ApiAuthService
import com.colab.myfriend.Api.UserResponse
import com.colab.myfriend.activity.LoginActivity
import com.colab.myfriend.adapter.UserDao
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val session: CoreSession,
    private val apiAuthService: ApiAuthService,
    private val userDao: UserDao
) : CoreViewModel() {
    override fun apiLogout() {}

    override fun apiRenewToken() {}

    fun login(email: String, password: String) = viewModelScope.launch {
        _apiResponse.emit(ApiResponse().responseLoading())
        ApiObserver.run(
            { apiAuthService.login(email, password) },
            false,
            object : ApiObserver.ModelResponseListener<UserResponse> {
                override suspend fun onSuccess(response: UserResponse) {
                    session.setValue(LoginActivity.EMAIL, email)
                    session.setValue(LoginActivity.PASS, password)
                    session.setValue(CoreSession.PREF_UID, response.token ?: "")
                    userDao.insert(response.user.copy(idDb = 1))
                    _apiResponse.emit(ApiResponse().responseSuccess())
                }

                override suspend fun onError(response: UserResponse) {
                    _apiResponse.emit(ApiResponse().responseError())
                }
            })
    }

}