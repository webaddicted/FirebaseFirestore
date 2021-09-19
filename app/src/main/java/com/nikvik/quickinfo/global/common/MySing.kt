package com.nikvik.quickinfo.global.common

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MySing private constructor(context: Context) {
    private var requestQueue: RequestQueue?
    private val ctx: Context = context
    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>?) {
        getRequestQueue()?.add(req)
    }

    companion object {
        private var instance: MySing? = null

        @Synchronized
        fun getInstance(context: Context): MySing? {
            if (instance == null) {
                instance = MySing(context)
            }
            return instance
        }
    }

    init {
        requestQueue = getRequestQueue()
    }
}