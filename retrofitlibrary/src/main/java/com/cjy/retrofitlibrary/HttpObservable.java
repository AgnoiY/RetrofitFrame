package com.cjy.retrofitlibrary;

import com.cjy.retrofitlibrary.model.DownloadModel;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 适用Retrofit网络请求Observable(被监听者)
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
class HttpObservable {

    /*LifecycleProvider*/
    private LifecycleProvider lifecycle;
    /*ActivityEvent*/
    private ActivityEvent activityEvent;
    /*FragmentEvent*/
    private FragmentEvent fragmentEvent;
    /*BaseObserver*/
    private BaseObserver observer;
    /*Observable<JsonElement> apiObservable*/
    private Observable apiObservable;
    /*Download*/
    DownloadModel downloadModel;

    /*构造函数*/
    private HttpObservable(Builder builder) {
        this.lifecycle = builder.lifecycle;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.observer = builder.observer;
        this.apiObservable = builder.apiObservable;
        this.downloadModel = builder.downloadModel;
    }


    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * 备注:这是方法原型，调用顺序严格，必须拆分
     * <p>
     * 1.管理取消订阅 避免内存泄漏
     * 备注:需要继承 RxActivity... / RxFragment...
     * 1.1. 传入 LifecycleProvider 自动管理生命周期  onCreate->onStop / onResume->onPause
     * 1.2. 传入 ActivityEvent 手动管理生命周期  ActivityEvent.STOP / ActivityEvent.DESTROY
     * 1.3  传入 FragmentEvent 手动管理生命周期  FragmentEvent.DESTROY_VIEW / FragmentEvent.DESTROY
     */
    /*map*/
    private Observable map() {
        return downloadModel != null ? apiObservable.map(new DownloadFunction(downloadModel)) :
                apiObservable.map(new ServerResultFunction());
    }

    /* compose 操作符 介于 map onErrorResumeNext */
    private Observable compose() {
        Observable observable = map();
        if (lifecycle != null) {
            if (activityEvent != null || fragmentEvent != null) {
                //两个同时存在,以 activity 为准
                if (activityEvent != null && fragmentEvent != null) {
                    return map().compose(lifecycle.bindUntilEvent(activityEvent));
                }
                if (activityEvent != null) {
                    return map().compose(lifecycle.bindUntilEvent(activityEvent));
                }

                return map().compose(lifecycle.bindUntilEvent(fragmentEvent));

            } else {
                return map().compose(lifecycle.bindToLifecycle());
            }
        }
        return observable;
    }

    /*onErrorResumeNext*/
    private Observable onErrorResumeNext() {
        return compose().onErrorResumeNext(new HttpResultFunction<>());
    }

    /*doOnDispose*/
    private Observable doOnDispose() {
        if (observer != null) {
            return onErrorResumeNext().doOnDispose(() -> observer.onCanceled());
        }
        return onErrorResumeNext();
    }

    /*线程设置*/
    public Observable observe() {
        return doOnDispose().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Builder
     * 构造Observable所需参数，按需设置
     */
    public static final class Builder {

        /*LifecycleProvider*/
        private LifecycleProvider lifecycle;
        /*ActivityEvent*/
        private ActivityEvent activityEvent;
        /*FragmentEvent*/
        private FragmentEvent fragmentEvent;
        /*BaseObserver*/
        private BaseObserver observer;
        /*Observable<Response> apiObservable*/
        private Observable apiObservable;
        /*Download*/
        private DownloadModel downloadModel;

        public Builder(Observable apiObservable) {
            this.apiObservable = apiObservable;
        }

        public HttpObservable.Builder baseObserver(BaseObserver observer) {
            this.observer = observer;
            return this;
        }

        public HttpObservable.Builder lifecycleProvider(LifecycleProvider lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        public HttpObservable.Builder activityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        public HttpObservable.Builder fragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        public Builder downloadModel(DownloadModel downloadModel) {
            this.downloadModel = downloadModel;
            return this;
        }

        public HttpObservable build() {
            return new HttpObservable(this);
        }
    }


}
