package net.codysehl.www.reader.Promise

import net.codysehl.www.reader.Promise.Deferred.Promise

class MutablePromise<V, E> : Promise<V, E> {
    var successCallback: (V) -> Unit = {}
    override fun onSuccess(callback: (V) -> Unit): Promise<V, E> {
        successCallback = callback
        if(isResolved) {
            successCallback(resolutionValue!!)
        }
        return this
    }

    var failureCallback: (E) -> Unit = {}
    override fun onFailure(callback: (E) -> Unit): Promise<V, E> {
        failureCallback = callback
        if(isRejected) {
            failureCallback(rejectionValue!!)
        }
        return this
    }


    var resolutionValue: V? = null
    override var isResolved = false
        get() = resolutionValue != null

    fun resolve(value: V) {
        successCallback(value)
        resolutionValue = value
    }


    var rejectionValue: E? = null
    override var isRejected = false
        get() = rejectionValue != null

    fun reject(error: E) {
        failureCallback(error)
        rejectionValue = error
    }
}