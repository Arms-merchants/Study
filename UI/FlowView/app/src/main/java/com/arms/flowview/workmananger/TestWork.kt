package com.arms.flowview.workmananger

import android.content.Context
import androidx.work.*

/**
 *    author : heyueyang
 *    time   : 2022/02/28
 *    desc   :
 *    version: 1.0
 */

class Test {

    fun initWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)//wifi
            .setRequiresCharging(true)//在设备充电时运行
            .setRequiresBatteryNotLow(true)//电量不足不会运行
            .build()

        val uploadWorkRequest = OneTimeWorkRequest.Builder(TestWork::class.java)
            .setConstraints(constraints)
            .build()
        /**
         * 参数说明：
         * 1：work的名称
         * 2：在发生冲突的情况下，唯一 OneTimeWorkRequests 可用的冲突解决策略的枚举。
         * 3：构建的work
         */
        WorkManager.getInstance(context)
            .enqueueUniqueWork("testWork", ExistingWorkPolicy.KEEP, uploadWorkRequest)

    }

}

class TestWork(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        //执行后台任务
        return Result.success()
    }

}