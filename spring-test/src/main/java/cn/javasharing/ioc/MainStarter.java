package cn.javasharing.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MainStarter {

    private static ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(16);
    public static void loadBean(){
        RootBeanDefinition aBeanDefinition = new RootBeanDefinition(InstanceA.class);
        RootBeanDefinition bBeanDefinition = new RootBeanDefinition(InstanceB.class);
        beanDefinitionMap.put("instanceA",aBeanDefinition);
        beanDefinitionMap.put("instanceB",bBeanDefinition);
    }

    public static void main(String[] args) {

        loadBean();




    }

    // 一级缓存
    public static Map<String,Object> singletonObjects=new ConcurrentHashMap<>();


    // 二级缓存： 为了将 成熟Bean和纯净Bean分离，避免读取到不完整得Bean
    public static Map<String,Object> earlySingletonObjects=new ConcurrentHashMap<>();

    // 三级缓存
    public static Map<String,ObjectFactory> singletonFactories=new ConcurrentHashMap<>();

    // 循环依赖标识
    public  static Set<String> singletonsCurrennlyInCreation=new HashSet<>();

    public static Object getBean(String beanName) throws Exception {
        Object singleton = getSingleton(beanName);
        if(singleton != null){
            return singleton;
        }

        //正在创建
        if(!singletonsCurrennlyInCreation.contains(beanName)){
            singletonsCurrennlyInCreation.add(beanName);
        }
        //实例化
        RootBeanDefinition beanDefinition = (RootBeanDefinition) beanDefinitionMap.get(beanName);
        Class<?> beanClass = beanDefinition.getBeanClass();
        //无参数构造方法
        Object instance = beanClass.newInstance();
        //创建动态代理
        singletonFactories.put(beanName, () -> new JdkProxyBeanPostProcessor().getEarlyBeanReference(earlySingletonObjects.get(beanName), beanName));

        Field[] declaredFields = beanClass.getDeclaredFields();
        for(Field field: declaredFields){
            Autowired annotation = field.getAnnotation(Autowired.class);
            //假如属性上有Autowired
            if(annotation != null){
                field.setAccessible(true);

                String name = field.getName();
                //拿到B的Bean
                Object insideObject = getBean(name);
                field.set(instance, insideObject);

            }
        }

        if(earlySingletonObjects.containsKey(beanName)){
            instance = earlySingletonObjects.get(beanName);
        }
        //添加到一级缓存
        singletonObjects.put(beanName, instance);

        return instance;

    }

    private static Object getSingleton(String beanName) {
        //先从一级缓存拿
        Object bean = singletonObjects.get(beanName);
        //存在循环依赖的情况
        if(bean == null && singletonsCurrennlyInCreation.contains(beanName)){
            //从二级缓存拿
            bean = earlySingletonObjects.get(beanName);
            if(bean == null){
                ObjectFactory factory = singletonFactories.get(beanName);
                if(factory!=null){
                    //拿到动态代理
                    bean = factory.getObject();
                    earlySingletonObjects.put(beanName, bean);
                }
            }
        }
        return bean;
    }
}
