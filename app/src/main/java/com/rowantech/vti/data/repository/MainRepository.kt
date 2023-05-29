package com.rowantech.vti.data.repository

import androidx.lifecycle.LiveData
import com.rowantech.vti.api.VTIService
import com.rowantech.vti.data.AppExecutors
import com.rowantech.vti.data.Resource
import com.rowantech.vti.data.db.AuthDao
import com.rowantech.vti.data.db.Authorization
import com.rowantech.vti.data.db.VTIDb
import com.rowantech.vti.data.remote.*
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.response.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: VTIDb,
    private val authDao: AuthDao,
    private val service: VTIService
) {

    fun loginPage(loginRequest: LoginRequest): LiveData<Resource<String>> {
        val loginPageTask = LoginPageTask(
            loginRequest = loginRequest,
            db = db,
            service = service
        )
        appExecutors.networkIO().execute(loginPageTask)
        return loginPageTask.liveData
    }

    fun logoutTask(accessToken: String): LiveData<Resource<String>> {
        val logoutTask = LogoutTask(
            accessToken = accessToken,
            service = service
        )
        appExecutors.networkIO().execute(logoutTask)
        return logoutTask.liveData
    }


    fun paramTask(accessToken :String ,  url :String ): LiveData<Resource<String>> {
        val task = GetParamTask(
                accessToken = accessToken,
                url = url,
                service = service
        )
        appExecutors.networkIO().execute(task)
        return task.liveData
    }

    fun paramWithBodyTask(authorization :String ,  url :String, request :String ): LiveData<Resource<String>> {
        val task = PostParamWithBody(
            authorization = authorization,
            url = url,
            request = request,
            service = service
        )
        appExecutors.networkIO().execute(task)
        return task.liveData
    }


    fun paramPOST(accessToken :String ,  url :String): LiveData<Resource<String>> {
        val task = ParamPostTask(
                accessToken = accessToken,
                url = url,
                service = service
        )
        appExecutors.networkIO().execute(task)
        return task.liveData
    }

    fun paramWithId(accessToken :String ,  url :String): LiveData<Resource<String>> {
        val task = GetParamId(
                accessToken = accessToken,
                url = url,
                service = service
        )
        appExecutors.networkIO().execute(task)
        return task.liveData
    }


    fun getAuth(username: String):LiveData<Authorization>{
        return authDao.loadAuth()
    }

}