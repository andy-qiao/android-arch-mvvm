package xyz.tangram.arch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建人：付三
 * 创建时间：2017/8/31 19:11
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyTarget {
    Class<? extends BaseModuleImpl> value();
}
