package net.codysehl.www.reader.Promise

class Deferred<V, E> {
    val promise = MutablePromise<V, E>()

    fun resolve(value: V) {
        promise.resolve(value)
    }

    fun reject(error: E) {
        promise.reject(error)
    }

    interface Promise<V, E> {
        val isResolved: Boolean
        val isRejected: Boolean
        fun onSuccess(callback: (V) -> Unit): Promise<V, E>
        fun onFailure(callback: (E) -> Unit): Promise<V, E>
    }
}