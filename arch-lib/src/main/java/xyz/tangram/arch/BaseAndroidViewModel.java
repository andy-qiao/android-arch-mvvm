package xyz.tangram.arch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

/**
 * 创建人：付三
 * 创建时间：2017/9/4 18:24
 */

public class BaseAndroidViewModel extends AndroidViewModel {
    private ModuleDelegate mDelegate = new ModuleDelegate();

    public BaseAndroidViewModel(Application application) {
        super(application);
    }

    protected <T extends BaseModule> T getModule(Class<T> moduleClass) {
        return mDelegate.getModule(moduleClass);
    }

    @Override
    protected void onCleared() {
        mDelegate.cancelAll();
        super.onCleared();
    }


}
