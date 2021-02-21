package com.firrael.kotlincoroutinessample

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.suspendCoroutine

// Sample #1
fun launchFirstCoroutine(delayMs: Long) {
    println("Sample #1: launchFirstCoroutine with delay $delayMs started..")
    GlobalScope.launch {
        delay(delayMs)
        println("Sample #1: launchFirstCoroutine with $delayMs delay finished")
    }
}

// Sample #2
fun asyncCoroutine() {
    println("Sample #2: start asyncCoroutine")

    val deferred = (1..100).map { n ->
        GlobalScope.async {
            n
        }
    }

    runBlocking {
        val sum = deferred.sumOf { it.await().toLong() }
        println("Sample #2: asyncCoroutine sum: $sum")
    }
}

// Sample #3
fun callSuspendFunctionAsync(): Deferred<Int> {
    return GlobalScope.async {
        suspendFunction(123)
    }
}

suspend fun suspendFunction(n: Int): Int {
    delay(1000)
    return n
}

// Sample #4
fun postItem(item: String) {
    println("Sample #4: postItem: $item")

    runBlocking {
        val token = preparePost()
        val post = "post1"
        println("Sample #4: Token: $token")
        println("Sample #4: Post: $post")
    }
}

suspend fun preparePost(): String {
    // makes a request and suspends the coroutine
    return suspendCoroutine {
        println("Sample #4: inside suspend coroutine ")
        it.resumeWith(Result.success("test result"))
        // Error version:
        //it.resumeWithException(IllegalArgumentException())
    }
}

// Sample #5
fun jobExample(): Job {
    val scope = CoroutineScope(Job())

    val job = scope.launch {
        println("Sample #5: jobTest")
    }

    return job
}

// Sample #8
suspend fun doSomethingUsefulOne(): Int {
    delay(1000L)
    return 123
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L)
    return 456
}

suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

// Sample #11
fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
}

// Sample #12
fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500)
    emit("$i: Second")
}


