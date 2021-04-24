package cn.javasharing.lambda;

import java.util.function.Function;

public class TestFuction {

    public static void main(String[] args) {

        Function<String, Integer> fun = Integer::new;
        System.out.println(fun.apply("111"));
    }
}
