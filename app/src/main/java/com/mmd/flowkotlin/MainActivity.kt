package com.mmd.flowkotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

const val TAG = "FSFSFSFS"

class MainActivity : AppCompatActivity() {

    lateinit var flow: Flow<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFlow()
        setupClicks()

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
        }.flowOn(Dispatchers.Main)
    }

    private fun setupClicks() {
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flow.collect {
                    Log.d(TAG, "Receiving $it")
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

// flow will not executed till we collect it. means --> we must call flow.collect to receive the streamed values ;)

/// reference
/// https://blog.mindorks.com/what-is-flow-in-kotlin-and-how-to-use-it-in-android-project