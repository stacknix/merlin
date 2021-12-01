package io.stacknix.merlin.android.demo.samples

import com.fabric.io.jsonrpc2.JsonRPCClient
import java.util.HashMap

class AuthUtilSample {

    companion object {
        fun getClient(): JsonRPCClient {
            val url = "http://192.168.1.73:9000/api/v1/jsonrpc"
            val headers: MutableMap<String, String> = HashMap()
            headers["Authorization"] = "Bearer f04b2d14-2d36-44e7-8a2f-b7fa7e4fbe26"
            return JsonRPCClient(url, headers)
        }
    }
}