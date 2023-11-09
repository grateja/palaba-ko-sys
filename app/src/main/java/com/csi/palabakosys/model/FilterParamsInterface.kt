package com.csi.palabakosys.model

import com.csi.palabakosys.app.dashboard.data.DateFilter
import com.csi.palabakosys.util.EnumSortDirection

interface FilterParamsInterface {
    var orderBy: String
    var sortDirection: EnumSortDirection
    var dateFilter: DateFilter?
}