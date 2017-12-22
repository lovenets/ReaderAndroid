package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.conf.global
import com.github.salomonbrys.kodein.instance
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Action
import net.codysehl.www.reader.ReduxLike.Store

class SearchPresenter {

    val store: Store<ApplicationState> = Kodein.global.instance()

    // Render when given props
    fun render(props: ApplicationState) {

    }

    // User actions

    fun textEntered(text: String) {
        store.dispatch(Action.SearchTextEntered(text))
    }

    fun enterPressed() {

    }

    // Interface

    interface View {
        fun setSearchBarText(text: String)
        fun setSearchResults(searchResults: List<String>)
    }
}