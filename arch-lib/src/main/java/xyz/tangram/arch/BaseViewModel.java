package xyz.tangram.arch;

import android.arch.lifecycle.ViewModel;

/**
 * 创建人：付三
 * 创建时间：2017/9/4 18:14
 */

public class BaseViewModel extends ViewModel {
    private ModuleDelegate mDelegate = new ModuleDelegate();

    protected <T extends BaseModule> T getModule(Class<T> moduleClass) {
        return mDelegate.getModule(moduleClass);
    }

    @Override
    protected void onCleared() {
        mDelegate.cancelAll();
        super.onCleared();
    }
}
