package xyz.tangram.arch;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:21
 */

public class BaseModuleImpl {

    /**
     * 获取模块实现类
     * @param moduleClass
     * @param <T>
     */
    protected <T extends BaseModuleImpl> T getModule(Class<T> moduleClass) {
        if (getClass().equals(moduleClass)) {
            return (T) this;
        }
        return ModuleManager.getImpl(moduleClass);
    }

}
