package xyz.tangram.arch;

/**
 * Module异步回调结果封装
 * @param <T>
 */
public class ModuleResult<T> {
    private Throwable mThrowable;
    private T mData;

    void throwable(Throwable throwable) {
        this.mThrowable = throwable;
    }

    public Throwable throwable() {
        return mThrowable;
    }

    void data(T data) {
        this.mData = data;
    }

    public T data() {
        return mData;
    }

}

