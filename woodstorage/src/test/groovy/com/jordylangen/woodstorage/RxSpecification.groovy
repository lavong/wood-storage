package com.jordylangen.woodstorage

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import spock.lang.Specification

import java.util.concurrent.Executor

class RxSpecification extends Specification {

    def setup() {
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.single()
            }
        })

        // https://github.com/ReactiveX/RxAndroid/blob/2.x/rxandroid/src/test/java/io/reactivex/android/schedulers/AndroidSchedulersTest.java
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.from(new Executor() {
                    @Override
                    void execute(Runnable command) {
                        command.run()
                    }
                })
            }
        })
    }
}