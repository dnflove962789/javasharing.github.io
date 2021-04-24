package cn.javasharing.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

public class JdkProxyBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException{
        //加如被切点命中
        if(bean instanceof InstanceA){
            JdkDynimcProxy jdkDynimcProxy = new JdkDynimcProxy(bean);
            return jdkDynimcProxy.getProxy();
        }
        return bean;
    }
}
