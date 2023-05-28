package com.developersmarket.componentscompose.retrofit.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersmarket.componentscompose.retrofit.MainRepository
import com.developersmarket.componentscompose.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val response: MutableState<ApiState> = mutableStateOf(ApiState.Empty)
     private set
//To use mutableState with viewmodel, define mutableState with private setter inside viewmodel
// By doing above we can read the mutable state from outside of viewmodel but not update it. To update create a public function inside viewmodel
    init {
        getPost()
    }
    fun getPost() =
        viewModelScope.launch {
            mainRepository.getPost().onStart {
                response.value= ApiState.Loading
            }.catch {
                response.value= ApiState.Failure(it)
            }.collect {
                response.value=ApiState.Success(it)
            }
        }
}
