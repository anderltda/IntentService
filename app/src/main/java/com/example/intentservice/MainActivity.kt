package com.example.intentservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity() {

    var receiver = ResponseReceiver()
    var receiverUBS = ResponseUSBReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MinhaIntentService::class.java)
        intent.putExtra(MinhaIntentService.PARAM_ENTRADA, "AGORA É:")
        startService(intent)
        registrarReceiver()
        registrarUSBReceiver()
    }

    private fun registrarReceiver() {
        val filter = IntentFilter(MinhaIntentService.ACTION)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(receiver, filter)
    }

    private fun registrarUSBReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(receiverUBS, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    inner class ResponseReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            tvResultado.text = intent?.getStringExtra(MinhaIntentService.PARAM_SAIDA)
        }
    }

    inner class ResponseUSBReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action.equals(Intent.ACTION_POWER_CONNECTED)) {
                Toast.makeText(this@MainActivity, "Conectado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Desconectado", Toast.LENGTH_LONG).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Status) {

        when(event) {

            Status.SUCESS -> {
                containerLoading.visibility = View.GONE
            }

            Status.ERROR -> {
                containerLoading.visibility = View.GONE
            }

            Status.LOADING -> {
                containerLoading.visibility = View.VISIBLE
            }

        }

    }
}
