package xyz.tangram.arch.demo.module;


import io.reactivex.Observable;
import xyz.tangram.arch.BaseModuleImpl;
import xyz.tangram.arch.demo.entity.LoginResult;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 17:20
 */

public class UserModuleImpl extends BaseModuleImpl {

    public String getUserName() {
        return "fusang";
    }

    public boolean isLogin() {
        return true;
    }

    public Observable<LoginResult> login(String username, String password) {
        // 模拟实现
        LoginResult result = new LoginResult();
        result.success = true;
        result.username = username;

        LoginResult result2 = new LoginResult();
        result2.success = false;
        result2.username = password;
        return Observable.just(result, result2);
    }

}
