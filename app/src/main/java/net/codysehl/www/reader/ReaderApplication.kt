package net.codysehl.www.reader

import android.app.Application
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.global
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Reducer
import net.codysehl.www.reader.ReduxLike.Store

class ReaderApplication: Application() {
    init {
        Kodein.global.addConfig { KodeinModule.module }
    }
}