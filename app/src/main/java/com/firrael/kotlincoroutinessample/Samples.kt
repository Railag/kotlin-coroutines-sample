package com.firrael.kotlincoroutinessample

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach
import kotlin.system.measureTimeMillis

fun sample1() {
    println("Sample #1: Launch 3 simple coroutines with delays")

    launchFirstCoroutine(5000)
    launchFirstCoroutine(1000)
    launchFirstCoroutine(2000)
}

fun sample2() {
    println("Sample #2: Async,  deferred, runBlocking, await")

    asyncCoroutine()
}

fun sample3() {
    println("Sample #3: Suspend function")

    val deferredResult = callSuspendFunctionAsync()
    runBlocking {
        val result = deferredResult.await()
        println("Sample #3: runBlocking deferred result: $result")
    }
}

fun sample4() {
    println("Sample #4: Suspend coroutine")

    postItem("item1")
}

fun sample5() {
    println("Sample #5: Job, CoroutineScope creation")

    runBlocking {
        val result = async { jobExample() }
        println("Sample #5: Created job object: $result")
    }
}

fun sample6() {
    println("Sample #6: CoroutineScope block, runBlocking & launch & coroutineScope & delay combination")

    runBlocking {
        launch {
            delay(200L)
            println("Sample #6: Task from runBlocking")
        }

        coroutineScope {
            launch {
                delay(500L)
                println("Sample #6: Task from nested launch")
            }

            delay(100L)
            println("Sample #6: Task from coroutine scope")
        }

        println("Sample #6: Coroutine scope is over")
    }
}

fun sample7() {
    println("Sample #7: Coroutine cancellation sample with cancel() and join()")

    runBlocking {
        val job = launch {
            repeat(50) { i ->
                println("Sample #7: repeat $i ...")
                delay(500L)
            }
        }

        delay(1500L)
        println("Sample #7: After global delay")
        job.cancel()
        job.join()
        println("Sample #7: After job's cancel and join")
    }
}

fun sample8() {
    println("Sample #8: Combining two async functions")

    runBlocking {
        val time = measureTimeMillis {
            println("Sample #8: Sum is ${concurrentSum()}")
        }

        println("Sample #8: Completed in $time ms")
    }
}

@ObsoleteCoroutinesApi
fun sample9() {
    println("Sample #9: Different Dispatchers")

    runBlocking {
        launch {
            println("Sample #9: main runBlocking      : thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("Sample #9: Unconfined            : thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
            println("Sample #9: Default               : thread ${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
            println("Sample #9: newSingleThreadContext: thread ${Thread.currentThread().name}")
        }
    }
}

fun sample10() {
    println("Sample #10: Coroutine advanced declaration")

    runBlocking {
        launch(Dispatchers.Default + CoroutineName("test sample11")) {
            println("Sample #10: Thread ${Thread.currentThread().name}")
        }
    }
}

@InternalCoroutinesApi
fun sample11() {
    println("Sample #11: Simple Flow")

    runBlocking {
        launch {
            for (k in 1..3) {
                println("Sample #11: not blocked $k")
                delay(100)
            }
        }

        simpleFlow().collect(object : FlowCollector<Int> {
            override suspend fun emit(value: Int) {
                println("Sample #11: flow emit value: $value")
            }

        })
    }
}


@FlowPreview
@InternalCoroutinesApi
fun sample12() {
    println("Sample #12: asFlow(), flatMapConcat() operators")

    runBlocking {
        val startTime = System.currentTimeMillis()

        (1..3).asFlow().onEach { delay(100) }
            .flatMapConcat { requestFlow(it) } // similar to RxJava
            .collect(object : FlowCollector<String> {
                override suspend fun emit(value: String) {
                    println("Sample #12: $value at ${System.currentTimeMillis() - startTime} ms from start")
                }
            })
    }
}

fun sample13() {
    println("Sample #13: CoroutineExceptionHandler")

    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Sample #13: CoroutineExceptionHandler got $exception")
        }

        val job = GlobalScope.launch(handler) { // root coroutine, running in GlobalScope
            throw AssertionError()
        }

        val deferred = GlobalScope.async(handler) { // also root, but async instead of launch
            throw ArithmeticException() // Nothing will be printed, relying on user to call deferred.await()
        }

        joinAll(job, deferred)
    }
}

