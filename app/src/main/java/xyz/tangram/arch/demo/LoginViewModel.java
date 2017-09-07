package xyz.tangram.arch.demo;

import android.arch.lifecycle.MutableLiveData;

import xyz.tangram.arch.BaseViewModel;
import xyz.tangram.arch.ModuleCall;
import xyz.tangram.arch.ModuleCallback;
import xyz.tangram.arch.ModuleResult;
import xyz.tangram.arch.demo.entity.LoginResult;
import xyz.tangram.arch.demo.module.UserModule;


public class LoginViewModel extends BaseViewModel {
    public final MutableLiveData<ModuleResult<LoginResult>> loginResult = new MutableLiveData<>();

    public void login(String username, String password) {
        ModuleCall<LoginResult> loginCall = getModule(UserModule.class).login(username, password);
        loginCall.enqueue(new ModuleCallback<LoginResult>() {
            @Override
            public void onModuleCallback(ModuleResult<LoginResult> result) {
                loginResult.setValue(result);
            }
        });
    }


    public Boolean isLogin() {
        return getModule(UserModule.class).isLogin();
    }

    public String getUserName() {
        return getModule(UserModule.class).getUserName();
    }

}
