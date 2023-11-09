package com.csi.palabakosys.model

import android.os.Parcelable
import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.util.EnumSortDirection
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseFilterParams(
    var orderBy: String = "Date Created",
    var sortDirection: EnumSortDirection = EnumSortDirection.DESC,
    open var dateFilter: DateFilter? = null
) : Parcelable
