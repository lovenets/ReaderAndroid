package net.codysehl.www.reader

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

object SchedulerFactory {
    fun createMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}