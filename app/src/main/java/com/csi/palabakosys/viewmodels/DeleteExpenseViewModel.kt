package com.csi.palabakosys.viewmodels

import com.csi.palabakosys.room.entities.EntityExpense
import com.csi.palabakosys.room.repository.ExpensesRepository
import com.csi.palabakosys.util.DeleteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteExpenseViewModel
@Inject
constructor(
    repository: ExpensesRepository
) : DeleteViewModel<EntityExpense>(repository)
{
}