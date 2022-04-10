package com.yyide.chatim.activity.face

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyide.chatim.base.SingleLiveEvent
import com.yyide.chatim.kotlin.network.NetworkApi
import com.yyide.chatim.model.FaceStudentBean
import kotlinx.coroutines.launch

class FaceViewModel : ViewModel() {

    var faceStudentListViewModel = SingleLiveEvent<List<FaceStudentBean>>()

    fun getFaceList(identityId: String) {
        viewModelScope.launch {
            val map = mutableMapOf<String, Any>()
            val result = NetworkApi.getFaceList(identityId)
            if (result.isSuccess) {
                faceStudentListViewModel.value = result.getOrNull()
            }
        }
    }
}