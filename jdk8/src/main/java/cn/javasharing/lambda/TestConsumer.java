package cn.javasharing.lambda;

import java.util.function.Consumer;

public class TestConsumer {
    public static void main(String[] args) {

        consumer("人民币", (something) -> {
            System.out.println(something + "来来");
        });

        manyConsumer("美元",
                (something) -> {
                    System.out.println("我用"+something);
                }, (something) -> {
                    System.out.println("你用"+something);
                });
    }

    private static void consumer(String something, Consumer<String> consumer) {
        consumer.accept(something);
    }

    private static void manyConsumer(String something, Consumer<String> consumer1, Consumer<String> consumer2) {
        consumer1.andThen(consumer2).accept(something);
    }
}
