package cn.javasharing.lambda;

@FunctionalInterface
public interface TestFunctionalInterface {

    public void test();

    //默认方法
    default String getName() {
        return "人参";
    }


    //静态方法
    static String getName2() {
        return "鹿茸";
    }
}
