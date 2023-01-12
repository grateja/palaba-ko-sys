package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityServiceWash
import com.csi.palabakosys.room.repository.WashServiceRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteWashServiceViewModel
@Inject
constructor(
    repository: WashServiceRepository
) : DeleteViewModel<EntityServiceWash>(repository)