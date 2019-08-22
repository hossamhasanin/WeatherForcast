package com.example.weatherforcast.Externals

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}


// In this function basically we override the class called Task and add the function call asDeferred to it
// this one of beautiful kotlin features
fun <T> Task<T>.asDeferred(): Deferred<T>{
    var deferred = CompletableDeferred<T>()

    this.addOnSuccessListener {
        deferred.complete(it)
    }

    this.addOnFailureListener{
        deferred.completeExceptionally(it)
    }

    return deferred
}