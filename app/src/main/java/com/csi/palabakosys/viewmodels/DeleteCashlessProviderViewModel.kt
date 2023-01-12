package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityCashlessProvider
import com.csi.palabakosys.room.repository.CashlessProvidersRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteCashlessProviderViewModel
@Inject
constructor(
    repository: CashlessProvidersRepository
) : DeleteViewModel<EntityCashlessProvider>(repository)