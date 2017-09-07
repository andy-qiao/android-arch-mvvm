package xyz.tangram.arch.demo.entity;

/**
 * 项目名称：arch
 * 创建人：付三
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2017/9/7 上午10:17
 * @version V1.0
 */

public class LoginResult {
    public String username;
    public boolean success;

    @Override
    public String toString() {
        return username + " login result " + success;
    }
}
