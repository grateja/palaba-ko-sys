package com.csi.palabakosys.app.gallery.picture_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csi.palabakosys.room.repository.JobOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PicturePreviewViewModel

@Inject
constructor(
    val jobOrderRepository: JobOrderRepository
) : ViewModel() {
    private val _dataState = MutableLiveData<DataState>()
    val dataState: LiveData<DataState> = _dataState

    private val _uriId = MutableLiveData<List<String>>()
    val uriIds: LiveData<List<String>> = _uriId

    fun setUriIds(id: List<String>) {
        _uriId.value = id
    }

    fun deletePicture(uriId: String) {
        viewModelScope.launch {
            jobOrderRepository.removePicture(UUID.fromString(uriId))
            _dataState.value = DataState.RemovePicture(uriId)
        }
    }

    fun resetState() {
        _dataState.value = DataState.StateLess
    }

    sealed class DataState {
        object StateLess: DataState()
        data class RemovePicture(val uriId: String): DataState()
    }
}