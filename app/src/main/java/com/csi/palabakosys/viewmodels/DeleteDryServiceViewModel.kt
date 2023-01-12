package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityServiceDry
import com.csi.palabakosys.room.repository.DryServiceRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteDryServiceViewModel
@Inject
constructor(
    repository: DryServiceRepository
) : DeleteViewModel<EntityServiceDry>(repository)
{
}