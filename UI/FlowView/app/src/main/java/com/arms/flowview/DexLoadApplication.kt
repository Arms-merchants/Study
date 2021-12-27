package com.tojoy.app.kpi.application

import android.app.Application
import android.content.Context
import android.os.Build
import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/12/22
 *    desc   : 一个在项目启动时进行dex加载的Application
 *    version: 1.0
 */
abstract class DexLoadApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //获取到PathClassLoader
        classLoader
        val apkFile = File(applicationInfo.sourceDir)
        //data/data/包名/files/dex/
        val dexFile = getDir("dex", MODE_PRIVATE)
        val list = arrayListOf<File>()
        //获取所有的dex文件
        dexFile.listFiles().forEach { file ->
            if (file.endsWith(".dex")) {
                list.add(file)
            }
        }
        if (list.isNotEmpty()) {
            try {
                V19.install(classLoader, list, dexFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        @Throws(NoSuchFieldException::class)
        fun findField(instance: Any, name: String): Field {
            var clazz: Class<*>? = instance.javaClass
            while (clazz != null) {
                try {
                    val e = clazz.getDeclaredField(name)
                    if (!e.isAccessible) {
                        e.isAccessible = true
                    }
                    return e
                } catch (var4: NoSuchFieldException) {
                    clazz = clazz.superclass
                }
            }
            throw NoSuchFieldException("Field " + name + " not found in " + instance.javaClass)
        }

        @Throws(NoSuchMethodException::class)
        fun findMethod(
            instance: Any,
            name: String,
            vararg parameterTypes: Class<*>
        ): Method? {
            var clazz: Class<*>? = instance.javaClass
            //        Method[] declaredMethods = clazz.getDeclaredMethods();
//        System.out.println("  findMethod ");
//        for (Method m : declaredMethods) {
//            System.out.print(m.getName() + "  : ");
//            Class<?>[] parameterTypes1 = m.getParameterTypes();
//            for (Class clazz1 : parameterTypes1) {
//                System.out.print(clazz1.getName() + " ");
//            }
//            System.out.println("");
//        }
            while (clazz != null) {
                try {
                    val e = clazz.getDeclaredMethod(name, *parameterTypes)
                    if (!e.isAccessible) {
                        e.isAccessible = true
                    }
                    return e
                } catch (var5: NoSuchMethodException) {
                    clazz = clazz.superclass
                }
            }
            throw NoSuchMethodException("Method " + name + " with parameters " + Arrays.asList(*parameterTypes) + " not found in " + instance.javaClass)
        }

        /**
         * @param instance      DexPathList对象
         * @param fieldName     "dexElements"
         * @param extraElements 上一步自己的dex专为的elememts
         * @throws NoSuchFieldException
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         */
        @Throws(
            NoSuchFieldException::class,
            java.lang.IllegalArgumentException::class,
            IllegalAccessException::class
        )
        fun expandFieldArray(instance: Any, fieldName: String, extraElements: Array<Any>) {
            val jlrField = findField(instance, fieldName)
            //获取原dex数组
            val original = jlrField[instance] as Array<Any?>
            //构建一个和原dex数组一样属性的长度为 原dex的长度加上我们需要加载的dex长度总和的数组
            val combined = java.lang.reflect.Array.newInstance(
                original.javaClass.componentType, original.size + extraElements.size
            ) as Array<Any?>
            //将自己的dex对象列表和原dex数组合并到这个combined数组中
            System.arraycopy(original, 0, combined, 0, original.size)
            System.arraycopy(extraElements, 0, combined, original.size, extraElements.size)
            //将新的dex数组的值设置回属性对象
            jlrField[instance] = combined
        }
    }

    private object V19 {

        /**
         * @param loader                     classLoader
         * @param additionalClassPathEntries 解密后的dex也就是需要classloader去加载的dex
         * @param optimizedDirectory         目录 //data/data/包名/files/fake_apk/
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws NoSuchFieldException
         * @throws InvocationTargetException
         * @throws NoSuchMethodException
         */
        @Throws(
            IllegalArgumentException::class,
            IllegalAccessException::class,
            NoSuchFieldException::class,
            InvocationTargetException::class,
            NoSuchMethodException::class
        )
        fun install(
            loader: ClassLoader, additionalClassPathEntries: List<File?>,
            optimizedDirectory: File
        ) {
            //这里获取的是loader中pathList这个属性
            val pathListField: Field = DexLoadApplication.findField(loader, "pathList")
            //而这里获取到的是pathList这里属性的具体值
            val dexPathList = pathListField[loader]
            val suppressedExceptions: ArrayList<IOException> = ArrayList<IOException>()
            if (Build.VERSION.SDK_INT >= 23) {
                val pathElements = makePathElements(
                    dexPathList,
                    ArrayList<File?>(additionalClassPathEntries),
                    optimizedDirectory,
                    suppressedExceptions
                )
                pathElements?.let {
                    DexLoadApplication.expandFieldArray(
                        dexPathList, "dexElements", it
                    )
                }
            } else {
                DexLoadApplication.expandFieldArray(
                    dexPathList, "dexElements", makeDexElements(
                        dexPathList,
                        ArrayList<File?>(additionalClassPathEntries),
                        optimizedDirectory,
                        suppressedExceptions
                    )
                )
            }
            //处理异常
            if (suppressedExceptions.size > 0) {
                val suppressedExceptionsField: Iterator<*> = suppressedExceptions.iterator()
                while (suppressedExceptionsField.hasNext()) {
                    val dexElementsSuppressedExceptions =
                        suppressedExceptionsField.next() as IOException
                    Logger.e("Exception in makeDexElement:${dexElementsSuppressedExceptions.message}")
                }
                val suppressedExceptionsField1: Field =
                    DexLoadApplication.findField(
                        loader,
                        "dexElementsSuppressedExceptions"
                    )
                var dexElementsSuppressedExceptions1 =
                    suppressedExceptionsField1[loader] as Array<IOException?>
                dexElementsSuppressedExceptions1 = run {
                    val combined = arrayOfNulls<IOException>(
                        suppressedExceptions.size +
                                dexElementsSuppressedExceptions1.size
                    )
                    suppressedExceptions.toArray(combined)
                    System.arraycopy(
                        dexElementsSuppressedExceptions1, 0, combined,
                        suppressedExceptions.size, dexElementsSuppressedExceptions1.size
                    )
                    combined
                }
                suppressedExceptionsField1[loader] = dexElementsSuppressedExceptions1
            }
        }

        @Throws(
            IllegalAccessException::class,
            InvocationTargetException::class,
            NoSuchMethodException::class
        )
        fun makeDexElements(
            dexPathList: Any,
            files: ArrayList<File?>, optimizedDirectory: File,
            suppressedExceptions: ArrayList<IOException>
        ): Array<Any> {
            val makeDexElements: Method? = findMethod(
                dexPathList, "makeDexElements", *arrayOf<Class<*>>(
                    ArrayList::class.java,
                    File::class.java,
                    ArrayList::class.java
                )
            )
            return makeDexElements?.invoke(
                dexPathList, *arrayOf<Any>(
                    files,
                    optimizedDirectory, suppressedExceptions
                )
            ) as Array<Any>
        }

        /**
         *
         * 通过反射DexPathList中的makePathElements方法来实现
         * 加载自己的dex，并转化为dexElements
         * 到这里hook classLoader修dex的操作已经完成了
         * @param dexPathList          DexPathList对象
         * @param files                解密后的dex
         * @param optimizedDirectory   目录
         * @param suppressedExceptions 异常处理？
         * A wrapper around
         * `private static final dalvik.system
         * .DexPathList#makePathElements`.
         */
        @Throws(
            IllegalAccessException::class,
            InvocationTargetException::class,
            NoSuchMethodException::class
        )
        open fun makePathElements(
            dexPathList: Any, files: ArrayList<File?>, optimizedDirectory: File,
            suppressedExceptions: ArrayList<IOException>
        ): Array<Any>? {
            val makePathElements: Method? = try {
                findMethod(
                    dexPathList, "makePathElements",
                    MutableList::class.java,
                    File::class.java,
                    MutableList::class.java
                )
            } catch (e: NoSuchMethodException) {
                Logger.e("NoSuchMethodException: makePathElements(List,File,List) failure")
                try {
                    findMethod(
                        dexPathList, "makePathElements",
                        ArrayList::class.java,
                        File::class.java,
                        ArrayList::class.java
                    )
                } catch (e1: NoSuchMethodException) {
                    Logger.e(
                        "NoSuchMethodException: makeDexElements(ArrayList,File,ArrayList) " +
                                "failure"
                    )
                    return try {
                        Logger.e("NoSuchMethodException: try use v19 instead")
                        V19.makeDexElements(
                            dexPathList, files, optimizedDirectory,
                            suppressedExceptions
                        )
                    } catch (e2: NoSuchMethodException) {
                        Logger.e("NoSuchMethodException: makeDexElements(List,File,List) failure")
                        throw e2
                    }
                }
            }
            return makePathElements?.invoke(
                dexPathList, files, optimizedDirectory,
                suppressedExceptions
            ) as Array<Any>
        }
    }

}