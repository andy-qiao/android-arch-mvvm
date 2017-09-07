package xyz.tangram.arch;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:32
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

