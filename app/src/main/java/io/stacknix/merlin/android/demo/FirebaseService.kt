package io.stacknix.merlin.android.demo

import android.provider.Settings
import android.util.Log
import com.fabric.io.jsonrpc2.JsonRPCClient
import com.fabric.io.jsonrpc2.models.ModelRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.stacknix.merlin.db.MerlinObject
import io.stacknix.merlin.db.android.Logging
import io.stacknix.merlin.db.commons.MappingUtil
import io.stacknix.merlin.db.openes.OpenES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class FirebaseService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FirebaseService"

        private fun findServiceToken(callback: (token: String) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    task.result?.let {
                        callback(it)
                    }
                }
            )
        }

        fun registerFirebaseDevice(client: JsonRPCClient){
            findServiceToken {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val map = mapOf(
                            "device_id" to Settings.Secure.ANDROID_ID,
                            "device_type" to "android",
                            "token" to it,
                        )
                        val request = ModelRequest(client, "firebase.device")
                        request.create(map, listOf("id"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "Message Received")
        remoteMessage.data["data"]?.let {
            OpenES(MappingUtil.jsonToMap(it)).commit()
        }
    }
}
