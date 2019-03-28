package com.example.intentservice

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.SystemClock
import android.text.format.DateFormat
import org.greenrobot.eventbus.EventBus


class MinhaIntentService : IntentService("MinhaIntentService") {

    override fun onHandleIntent(intent: Intent?) {

        EventBus.getDefault().post(Status.LOADING)

        val msg = intent?.getStringExtra(MinhaIntentService.PARAM_ENTRADA)
        SystemClock.sleep(4000)
        val resultado = "$msg ${DateFormat.format("dd/MM/yyyy hh:mm:ss aa", System.currentTimeMillis())}"

        val intent = Intent()
        intent.action = ACTION
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(PARAM_SAIDA, resultado)
        sendBroadcast(intent)

        EventBus.getDefault().post(Status.SUCESS)
    }

    companion object {

        val PARAM_ENTRADA = "entrada"
        val PARAM_SAIDA = "saida"
        val ACTION = "com.example.intentservice.action.RESPONSE"

    }
}
