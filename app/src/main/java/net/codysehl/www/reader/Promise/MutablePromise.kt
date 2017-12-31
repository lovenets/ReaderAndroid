package net.codysehl.www.reader.Promise

import net.codysehl.www.reader.Promise.Deferred.Promise

class MutablePromise<V, E> : Promise<V, E> {
    var successCallback: (V) -> Unit = {}
    override fun onSuccess(callback: (V) -> Unit): Promise<V, E> {
        successCallback = callback
        return this
    }

    var failureCallback: (E) -> Unit = {}
    override fun onFailure(callback: (E) -> Unit): Promise<V, E> {
        failureCallback = callback
        return this
    }

    override var isResolved = false
    fun resolve(value: V) {
        successCallback(value)
        isResolved = true
    }

    override var isRejected = false
    fun reject(error: E) {
        failureCallback(error)
        isRejected = true
    }
}