package cn.lab.ai.demo.simple;

import java.util.ArrayList;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/8.
 *
 * 轻松用Java玩转机器学习，入门看这一篇就够了~
 *
 * https://jishuin.proginn.com/p/763bfbd5ada8
 */
public class LrDemo {
    public static void main(String[] args) {
        ArrayList<Double> features = new ArrayList<>();
        ArrayList<Double> targets = new ArrayList<>();
        //准备特征值
        for (int i = 0; i < 200; i++) {
            features.add((double)i);
        }
        //用y = 3x + 1生成目标值
        for (Double feature : features) {
            //生成目标值，并加上一个随机数
            double target = feature * 3 + 1 + Math.random() * 3;
            targets.add(target);
        }

        //创建线性回归模型
        LinearRegression linearRegression = new LinearRegression(features, targets);

        for (long i = 1; i <= 300; i++){
            double loss = linearRegression.gradientDecent(1e-6);
            if(i % 100 == 0){
                System.out.println("第" + i + "次更新后");
                System.out.println("weight = " + linearRegression.getWeight());
                System.out.println("bias = " + linearRegression.getBias());
                System.out.println("loss = " + loss);
            }
        }

        //准备数据用于测试
        ArrayList<Double> testList = new ArrayList<>();
        testList.add(100.0);
        testList.add(27.0);
        ArrayList<Double> testPredict = linearRegression.predict(testList);
        System.out.println("真实结果");
        for (Double testX : testList) {
            System.out.println(testX * 3 + 1);
        }
        System.out.println("预测结果");
        for (Double predict : testPredict) {
            System.out.println(predict);
        }

    }
}
