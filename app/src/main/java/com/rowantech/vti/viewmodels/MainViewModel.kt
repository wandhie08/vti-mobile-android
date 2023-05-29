package com.rowantech.vti.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rowantech.vti.data.Resource
import com.rowantech.vti.data.Status
import com.rowantech.vti.data.db.Authorization
import com.rowantech.vti.data.model.request.LoginRequest
import com.rowantech.vti.data.model.response.LoginResponse
import com.rowantech.vti.data.repository.MainRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val loginHandler = MainHandler(repository)

    var data: LiveData<Resource<String>>? = null
        private set

    var authDB: LiveData<Authorization>? = null
        private set

    fun login(loginRequest: LoginRequest) {
        data = loginHandler.loginPage(loginRequest)
    }

    fun logout(accessToken: String) {
        data = loginHandler.logoutTask(accessToken)
    }

    fun paramTask(accessToken: String, url: String) {
        data = loginHandler.getParam(accessToken, url)
    }

    fun paramWithBody(authorization: String, url: String, request: String) {
        data = loginHandler.paramWithBody(authorization,  url, request)
    }

    fun paramWithId(accessToken: String, url: String) {
        data = loginHandler.paramWithId(accessToken, url)
    }

    fun paramPOST(accessToken: String, url: String) {
        data = loginHandler.paramPOST(accessToken, url)
    }

    fun getAllAuth(username: String) {
        authDB = loginHandler.getAuthFromDB(username)
    }

    
    class MainHandler(private val repository: MainRepository) : Observer<Resource<Boolean>> {
        private var loginPageLiveData: LiveData<Resource<String>>? = null
        private var loginRequest: LoginRequest? = null
        private var expiry: String? = null
        private var uid: String ? = null
        private var accessToken: String? = null
        private var client: String? = null
        private var url: String? = null
        private var authLiveData: LiveData<Authorization>? = null
        private var request: String? = null
        private var id: Int? = null
        private var data: String? = null
        private var authorization: String? = null

        init {
            reset()
        }

        fun loginPage(loginRequest: LoginRequest): LiveData<Resource<String>>? {
            unregister()
            this.loginRequest = loginRequest
            loginPageLiveData = repository.loginPage(loginRequest)
            return loginPageLiveData
        }

        fun logoutTask(accessToken: String): LiveData<Resource<String>>? {
            unregister()
            this.accessToken = accessToken
            loginPageLiveData = repository.logoutTask(accessToken)
            return loginPageLiveData
        }


        fun getParam(accessToken :String, url :String): LiveData<Resource<String>>? {
            unregister()
            this.expiry = expiry
            this.uid = uid
            this.accessToken = accessToken
            this.client = client
            this.url = url

            loginPageLiveData = repository.paramTask(accessToken  , url )
            return loginPageLiveData
        }



        fun paramWithBody(authorization :String ,  url :String, request :String): LiveData<Resource<String>>? {
            unregister()
            this.authorization = authorization
            this.uid = uid
            this.accessToken = accessToken
            this.client = client
            this.url = url
            this.request = request

            loginPageLiveData = repository.paramWithBodyTask(authorization , url ,request)
            return loginPageLiveData
        }

        fun paramPOST(accessToken :String, url :String): LiveData<Resource<String>>? {
            unregister()
            this.expiry = expiry
            this.uid = uid
            this.accessToken = accessToken
            this.client = client
            this.url = url

            loginPageLiveData = repository.paramPOST(accessToken  , url )
            return loginPageLiveData
        }

        fun paramWithId(accessToken :String, url :String): LiveData<Resource<String>>? {
            unregister()
            this.accessToken = accessToken
            this.url = url

            loginPageLiveData = repository.paramWithId(accessToken  , url)
            return loginPageLiveData
        }

        fun getAuthFromDB(username : String) : LiveData<Authorization>?{
            unregister()
            authLiveData = repository.getAuth(username)
            return authLiveData
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        unregister()

                    }
                    Status.ERROR -> {
                        unregister()

                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            loginPageLiveData = null
            loginRequest = null
            data=null
        }

        fun reset() {
            unregister()
        }
    }
}