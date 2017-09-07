package xyz.tangram.arch;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Module异步方法调用返回值封装，可进行取消操作和设置回调
 * 调用其enqueue方法，才真正触发任务
 */
public class ModuleCall<T> {
    private Object mObservable;
    private ModuleCallback<T> mModuleCallback;
    private Object mCancelHandle;
    private volatile boolean mDone = false;
    private volatile boolean mCanceled = false;
    private boolean mExecuted = false;
    private ModuleResult<T> mResult = new ModuleResult<>();

    void setObservable(Object observable) {
        mObservable = observable;
    }

    public void cancel() {
        mCanceled = true;
        if (mCancelHandle instanceof Disposable) {
            ((Disposable) mCancelHandle).dispose();
        } else if (mCancelHandle instanceof Subscription) {
            ((Subscription) mCancelHandle).cancel();
        }
    }

    public boolean isDone() {
        return mDone || mCanceled;
    }

    public boolean isCanceled() {
        return mCanceled;
    }

    public void enqueue(final ModuleCallback<T> callback) {
        synchronized (this) {
            if (mExecuted) {
                throw new IllegalStateException("每个ModuleCall只能enqueue一次");
            }
            mExecuted = true;
        }
        if (mCanceled || mDone) {
            return;
        }
        mModuleCallback = callback;

        if (mObservable instanceof Observable) {
            subscribeObservable((Observable<T>) mObservable);
        } else if (mObservable instanceof Single) {
            subscribeSingle((Single<T>) mObservable);
        } else if (mObservable instanceof Flowable) {
            subscribeFlowable((Flowable<T>) mObservable);
        } else {
            subscribeMaybe((Maybe<T>) mObservable);
        }
    }

    private void subscribeObservable(Observable<T> observable) {
        observable.subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCancelHandle = d;
            }

            @Override
            public void onNext(@NonNull T t) {
                mResult.data(t);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mResult.throwable(e);
                done();
            }

            @Override
            public void onComplete() {
                done();
            }
        });
    }

    private void subscribeSingle(Single<T> single) {
        single.subscribe(new SingleObserver<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCancelHandle = d;
            }

            @Override
            public void onSuccess(@NonNull T t) {
                mResult.data(t);
                done();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mResult.throwable(e);
                done();
            }
        });
    }

    private void subscribeFlowable(Flowable<T> flowable) {
        flowable.subscribe(new FlowableSubscriber<T>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                mCancelHandle = s;
            }

            @Override
            public void onNext(T t) {
                mResult.data(t);
            }

            @Override
            public void onError(Throwable t) {
                mResult.throwable(t);
                done();
            }

            @Override
            public void onComplete() {
                done();
            }
        });
    }

    private void subscribeMaybe(Maybe<T> maybe) {
        maybe.subscribe(new MaybeObserver<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mCancelHandle = d;
            }

            @Override
            public void onSuccess(@NonNull T t) {
                mResult.data(t);
                done();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mResult.throwable(e);
                done();
            }

            @Override
            public void onComplete() {
                done();
            }
        });
    }

    private void done() {
        mDone = true;
        if (mModuleCallback == null || mCanceled) {
            return;
        }
        mModuleCallback.onModuleCallback(mResult);
    }

}
