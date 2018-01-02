package net.codysehl.www.reader

import com.github.salomonbrys.kodein.*
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

fun SchedulerModule(overrides: Boolean = false): Kodein.Module {
    return Kodein.Module {
        bind<Scheduler>(tag = KodeinTag.MAIN_THREAD, overrides = overrides) with provider { Schedulers.trampoline() }
    }
}