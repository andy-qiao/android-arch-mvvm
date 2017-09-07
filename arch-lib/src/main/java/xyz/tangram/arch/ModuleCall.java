package xyz.tangram.arch;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:31
 */

public class ModuleCall<T> {
    private Observable<T> mObservable;
    private ModuleCallback<T> mModuleCallback;
    private Disposable mDisposable;
    private volatile boolean mDone = false;
    private volatile boolean mCanceled = false;
    private boolean mExecuted = false;

    void setObservable(Observable<T> observable) {
        mObservable = observable;
    }

    public void cancel() {
        mCanceled = true;
        if (mDisposable != null) {
            mDisposable.dispose();
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
        final ModuleResult<T> result = new ModuleResult<>();
        mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull T t) {
                result.data(t);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                result.throwable(e);
                done();
            }

            @Override
            public void onComplete() {
                done();
            }

            private void done() {
                mDone = true;
                if (mModuleCallback == null || mCanceled) {
                    return;
                }
                mModuleCallback.onModuleCallback(result);
            }
        });
    }


}
