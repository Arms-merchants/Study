package com.arms.flowview.ktx

import androidx.lifecycle.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    version: 1.0
 */
class TestViewModel : ViewModel() {

    /**
     * 一般的使用场景，避免UI层拿到数据去做更新
     */
    private val _testData = MutableLiveData<String>()
    val testData: LiveData<String> = _testData

    /**
     * Transformation 工作在主线程
     * 例如当我们需要从repository层拿到的数据进行处理，例如从数据库中拿到UserList，然后根据id获取某个User
     */
    val viewModleResult = Transformations.map(testData) {
        //这里执行在主线程
        //map和switchMap内部都是使用MediatorLiveData#addSource()方法实现，而该方法会在主线程调用，使用不当会有性能问题
    }

    val result: LiveData<String> = liveData {
        Logger.e("当前执行线程${Thread.currentThread().name}")
        val data = someSuspendingFunction()
        emit(data)
    }

    suspend fun doRequest() {
        viewModelScope.launch() {
            launch(Dispatchers.IO) {
                Logger.e("doRequest 当前执行线程${Thread.currentThread().name}")
                val url = URL("https://www.mxnzp.com/api/weather/current/北京")
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {
                    try {
                        val testIn: InputStream = BufferedInputStream(urlConnection.inputStream)
                        val br = BufferedReader(InputStreamReader(testIn))
                        val ret = StringBuffer()
                        var line = ""
                        br.use {
                            line = it.readLine()
                            line?.let { value ->
                                ret.append(value)
                            }
                        }
                        _testData.postValue(ret.toString())
                        br.close()
                        testIn.close()
                    } finally {
                        urlConnection.disconnect()
                    }
                }
            }
        }
    }

    private suspend fun someSuspendingFunction(): String {
        var test = "测试1"
        viewModelScope.launch(Dispatchers.IO) {
            Logger.e("someSuspendingFunction 当前执行线程${Thread.currentThread().name}")
            test = "测试2"
        }.join()
        Logger.e("当前的 test:${test}")
        return test
    }

    /** ---  Flow -- */
    fun dataStream(): Flow<String> {
        return flow<String> {

        }
    }

    fun test() {
        viewModelScope.launch {
            dataStream().collect {

            }
        }
    }

    val aModel = CounterModel()
    val bModel = CounterModel()
    val sumFlow: Flow<Int> = aModel.counter.combine(bModel.counter) { a, b -> a + b }

    val testStateFlow = MutableStateFlow("")

    suspend fun test2() {
        testStateFlow.emit("")
    }

    class CounterModel {
        private val _counter = MutableStateFlow(1) // private mutable state flow
        val counter = _counter.asStateFlow() // publicly exposed as read-only state flow

        fun inc() {
            _counter.value++
        }
    }

    /**
     * stateFlow 和 shareFlow的区别
     * statFlow
     * 提供「可读可写」和「仅可读」两个版本（StateFlow，MutableStateFlow）
     * 它的值是唯一的
     * 它允许被多个观察者共用 （因此是共享的数据流）
     * 它永远只会把最新的值重现给订阅者，这与活跃观察者的数量是无关的
     * 支持 DataBinding
     * 必须配置初始值
     * value 空安全
     * 防抖
     * MutableStateFlow 构造方法强制赋值一个非空的数据，而且 value 也是非空的。这意味着 StateFlow 永远有值
     * StateFlow 的 emit() 和 tryEmit() 方法内部实现是一样的，都是调用 setValue()
     * StateFlow 默认是防抖的，在更新数据时，会判断当前值与新值是否相同，如果相同则不更新数据。
     *
     * shareFlow
     * 与 SateFlow 一样，SharedFlow 也有两个版本：SharedFlow 与 MutableSharedFlow。
     * MutableSharedFlow 没有起始值
     * SharedFlow 可以保留历史数据
     * MutableSharedFlow 发射值需要调用 emit()/tryEmit() 方法，没有 setValue() 方法
     */

    /** ---  测试相关的方法-- */

    /**
     * 模拟登录测试，这里将登录成功作为一个状态来进行监听，这里出现的问题就是当发生横竖屏切换时，事件又会被接收到，粘性事件问题
     */
    val navigationState = MutableStateFlow<NavigationState>(NavigationState.EmptyNavigationState)

    fun login() {
        viewModelScope.launch {
            navigationState.emit(NavigationState.ViewNavigationState)
        }
    }

    /**
     * 修改登录作为事件处理
     */

    val navigationEvnet = MutableSharedFlow<NavigationState>()

    fun login2() {
        viewModelScope.launch {
            navigationEvnet.emit(NavigationState.ViewNavigationState)
        }
    }


}