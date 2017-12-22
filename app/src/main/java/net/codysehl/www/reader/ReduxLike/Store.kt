package net.codysehl.www.reader.ReduxLike

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class Store<T>(val reducer: (T, Action) -> T, var state: T) {
    val observable: Observable<T>
        get() = publishableObservable

    private val publishableObservable: PublishSubject<T> = PublishSubject.create()
    fun dispatch(action: Action) {
        state = reducer(state, action)
        publishableObservable.onNext(state)
    }
}