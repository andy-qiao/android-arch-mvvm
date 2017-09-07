package xyz.tangram.arch;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

/**
 * 基础ViewModel，可自动取消Module的调用,可获取Application实例的引用
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
