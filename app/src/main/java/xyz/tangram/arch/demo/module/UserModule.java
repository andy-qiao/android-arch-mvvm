package xyz.tangram.arch.demo.module;


import xyz.tangram.arch.BaseModule;
import xyz.tangram.arch.ModuleCall;
import xyz.tangram.arch.ProxyTarget;
import xyz.tangram.arch.demo.entity.LoginResult;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:17
 */
@ProxyTarget(UserModuleImpl.class)
public interface UserModule extends BaseModule {

    String getUserName();

    boolean isLogin();

    ModuleCall<LoginResult> login(String username, String password);

}
