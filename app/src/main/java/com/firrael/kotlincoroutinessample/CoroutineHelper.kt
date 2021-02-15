package com.firrael.kotlincoroutinessample

import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

fun launchFirstCoroutine(delayMs: Long) {
    println("launchFirstCoroutine")
    GlobalScope.launch {
        delay(delayMs)
        println("launchFirstCoroutine delayed")
    }
}

fun asyncCoroutine() {
    println("start asyncCoroutine")

//    val deferred = (1..1_000_000).map { n ->
    val deferred = (1..100).map { n ->
        GlobalScope.async {
            n
        }
    }

    runBlocking {
        val sum = deferred.sumOf { it.await().toLong() }
        println("Sum: $sum")
    }
}

fun callSuspendFunctionAsync(): Deferred<Int> {
    return GlobalScope.async {
        suspendFunction(5)
    }
}

suspend fun suspendFunction(n: Int): Int {
    delay(1000)
    return n
}

fun postItem(item: String) {
    println("postItem: $item")

    GlobalScope.launch {
        val token = preparePost()
        val post = "post1"
        println("Token: $token")
        println("Post: $post")
    }
}

suspend fun preparePost(): String {
    // makes a request and suspends the coroutine
    return suspendCoroutine { /* ... */ }
}

// Job

fun jobExample(): Job {
    val scope = CoroutineScope(Job())

    val job = scope.launch {
        println("jobTest")
    }

    return job
}





