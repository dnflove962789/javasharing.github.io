package cn.javasharing.lambda;

public class TestThread {

    private static void newThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void main(String[] args) {
        newThread(()->{
            System.out.println(Thread.currentThread().getName());
        });
    }
}
