package cn.javasharing.lambda;

public class TestFunctional {

    private static void test(TestFunctionalInterface functionalInterface){
        functionalInterface.test();
    }

    public static void main(String[] args) {
        test(()->{
            System.out.println("hello");
        });
    }
}
