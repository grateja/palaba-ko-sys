package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityDiscount
import com.csi.palabakosys.room.repository.DiscountsRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteDiscountViewModel
@Inject
constructor(
    repository: DiscountsRepository
) : DeleteViewModel<EntityDiscount>(repository)