package net.codysehl.www.reader.ReduxLike

data class ApplicationState(
        val searchText: String = "",
        val searchPending: Boolean = false
)