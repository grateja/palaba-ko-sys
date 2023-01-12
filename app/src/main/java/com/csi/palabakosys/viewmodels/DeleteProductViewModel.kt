package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityProduct
import com.csi.palabakosys.room.repository.ProductRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteProductViewModel
@Inject
constructor(
    repository: ProductRepository
) : DeleteViewModel<EntityProduct>(repository)