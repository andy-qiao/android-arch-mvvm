package xyz.tangram.arch;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:40
 */

public interface ModuleCallback<T> {

    void onModuleCallback(ModuleResult<T> result);

}
