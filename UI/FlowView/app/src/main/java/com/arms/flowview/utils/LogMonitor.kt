package com.arms.flowview.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Printer
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.concurrent.atomic.AtomicBoolean


/**
 *    author : heyueyang
 *    time   : 2022/02/25
 *    desc   : 整个BlockCanary的实现原理就是在一个app中的界面都是通过Handle来触发的，handler的loop方法中，在进行消息分发的前后都有一个日志打印，
 *    所以我们可以任务我们的代码是执行在这两个日志打印的中间的，所以实现整个Printer，记录两次日志之间的时间间隔，判断是是否超过阙值发生了卡顿
 *    version: 1.0
 */
class LogMonitor() : Printer {

    private var mStackSample: StackSampler
    private var mPrintingStarted = false
    private var mStartTimestamp = 0L
    //阙值
    private val mBlockThresholdMillis = 3000
    //采样频率
    private val mSampleInterval = 1000L
    //打印log的Handler
    private var mLogHandler: Handler

    init {
        mStackSample = StackSampler(mSampleInterval)
        val handlerThread = HandlerThread("block-canary-io")
        handlerThread.start()
        mLogHandler = Handler(handlerThread.looper)
    }

    /**
     * 在分发消息之前会调用一次整个，当分发消息结束后会再调用这个方法
     *
     */
    override fun println(x: String?) {
        if (!mPrintingStarted) {
            mStartTimestamp = System.currentTimeMillis()
            mPrintingStarted = true
            mStackSample.startDump()
        } else {
            val endTime = System.currentTimeMillis()
            mPrintingStarted = false
            //如果发现卡顿
            if (isBlock(endTime)) {
                notifyBlockEvent(endTime)
            }
            mStackSample.stopDump()
        }
    }

    private fun notifyBlockEvent(endTime: Long) {
        mLogHandler.post {
            mStackSample.getStacks(mStartTimestamp, endTime).forEach {
                Log.e("block-canary", it)
            }
        }
    }

    private fun isBlock(endTime: Long): Boolean {
        return endTime - mStartTimestamp > mBlockThresholdMillis
    }

}

class StackSampler(sampleInterval: Long) {

    private val SEPARATOR = "\r\n"
    private val TIEM_DORMATTER = SimpleDateFormat("MM-dd HH:mm:ss.SSS")

    private var mHandler: Handler
    private val mStackMap = LinkedHashMap<Long, String>()
    //最多存储100条
    private val mMaxCount = 100
    //采样率，多长时间采集一次主线程的堆栈信息
    private var mSampleInterval: Long = sampleInterval
    //cas
    private val mShouldSample = AtomicBoolean(false)
    private lateinit var mRunnable: Runnable

    init {
        val handlerThread = HandlerThread("block-canary-sampler")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
        mRunnable = Runnable() {
            val sb = StringBuilder()
            //存储的只主线程的堆栈信息
            val stackTrace = Looper.getMainLooper().thread.stackTrace
            stackTrace.forEach {
                sb.append(it.toString()).append("\n")
            }
            synchronized(mStackMap) {
                if (mStackMap.size == mMaxCount) {
                    mStackMap.remove(mStackMap.keys.iterator().next())
                }
                mStackMap.put(System.currentTimeMillis(), sb.toString())
            }
            if (mShouldSample.get()) {
                mHandler.postDelayed(mRunnable, mSampleInterval)
            }
        }
    }

    fun startDump() {
        if (mShouldSample.get()) {
            return
        }
        mShouldSample.set(true)
        mHandler.removeCallbacks(mRunnable)
        mHandler.postDelayed(mRunnable, mSampleInterval)
    }

    fun stopDump() {
        if (!mShouldSample.get()) {
            return
        }
        mShouldSample.set(false)
        mHandler.removeCallbacks(mRunnable)
    }

    /**
     * 获取栈信息
     */
    fun getStacks(startTime: Long, endTime: Long): ArrayList<String> {
        val result = arrayListOf<String>()
        synchronized(mStackMap) {
            mStackMap.keys.forEach {
                mStackMap[it]?.let { content -> Logger.e(content) }
                if (it in (startTime + 1) until endTime) {
                    result.add(
                        TIEM_DORMATTER.format(it)
                                + SEPARATOR
                                + SEPARATOR
                                + mStackMap[it]
                    )
                }
            }
        }
        return result
    }
}
