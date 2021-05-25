package com.mmd.flowkotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val TAG = "FSFSFSFS"

class MainActivity : AppCompatActivity() {

    private lateinit var flow: Flow<Int>

    private lateinit var flowBuilder1: Flow<Int>
    private lateinit var flowBuilder2: Flow<Int>
    private lateinit var flowBuilder4: Flow<Int>

    private lateinit var flowOne: Flow<String>
    private lateinit var flowTwo: Flow<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFlow()
        setupFlowClicks()

        setupFlowBuilders()
        setupFlowBuilderClicks()

        setupFlowOperators()
        setupFlowOperatorsClicks()
    }

    private fun setupFlow() {
        flow = flow {
            Log.d(TAG, "Start flow")
            (0..10).forEach {
                // Emit items with 500 milliseconds delay
                delay(500)
                Log.d(TAG, "Emitting $it")
                emit(it)

            }
        }
            .map { it * it }
            .flowOn(Dispatchers.Main)
    }

    private fun setupFlowClicks() {
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flow.collect {
                    Log.d(TAG, "Receiving $it")
                }
            }
        }
    }

    private fun setupFlowBuilders() {
        // way 1
        flowBuilder1 = flowOf(4, 2, 5, 1, 7).onEach { delay(400) }.flowOn(Dispatchers.Default)
        // way 2
        flowBuilder2 = (1..5).asFlow().onEach { delay(300) }.flowOn(Dispatchers.Default)
        // way 3 -->
        /** flow{} as we used in function [setupFlow] */
        // way 4 --> still not get the trick [misunderstand something]
        flowBuilder4 = channelFlow {
            (0..10).forEach {
                send(it)
            }
        }.flowOn(Dispatchers.Default)

    }

    private fun setupFlowBuilderClicks() {
        findViewById<Button>(R.id.flow_builder_btn).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flowBuilder1.collect {
                    Log.d(TAG, "Receiving Builder 1: $it")
                }
                flowBuilder2.collect {
                    Log.d(TAG, "Receiving Builder 2: $it")
                }
                flowBuilder4.collect {
                    Log.d(TAG, "Receiving Builder 4: $it")
                }
            }
        }
    }

    private fun setupFlowOperators() {
        flowOne = flowOf("Himanshu", "Amit", "Janishar").flowOn(Dispatchers.Default)
        flowTwo = flowOf("Singh", "Shekhar", "Ali").flowOn(Dispatchers.Default)
    }

    private fun setupFlowOperatorsClicks() {
        // Consider if both flows doesn't have the same number of item, then the flow will stop as soon as one of the flow completes.
        findViewById<Button>(R.id.flow_operator_btn).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flowOne.zip(flowTwo) { firstString, secondString ->
                    "$firstString, $secondString"
                }.collect {
                    Log.d(TAG, "Zipping $it")
                }
            }
        }
    }
}

// With Flow in Kotlin now you can handle a stream of data that emits values sequentially.
// We will cover the following topics,

/**
 * What is Flow APIs in Kotlin Coroutines?
 * Start Integrating Flow APIs in your project
 * Builders in Flows
 * Few examples using Flow Operators.
 **/

// Flow will not executed till we collect it. means --> we must call flow.collect to receive the streamed values ;)
// Anything, written above flowOn will run in background thread.
// flowOn() is like subscribeOn() in RxJava

/// reference
/// https://blog.mindorks.com/what-is-flow-in-kotlin-and-how-to-use-it-in-android-project