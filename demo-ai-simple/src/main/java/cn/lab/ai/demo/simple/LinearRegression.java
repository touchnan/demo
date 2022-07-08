package cn.lab.ai.demo.simple;

import java.util.ArrayList;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/8.
 */
public class LinearRegression {
    //权重
    private double weight;
    //偏置值
    private double bias;
    //特征值
    private ArrayList<Double> features;
    //目标值
    private ArrayList<Double> targets;

    /**
     * 构造线性回归模型
     * @param features 训练数据的特征值
     * @param targets 训练数据的目标值
     */
    public LinearRegression(ArrayList<Double> features, ArrayList<Double> targets){
        this.features = features;
        this.targets = targets;
        initParameter();
    }

    /**
     * 初始化权重和偏置值
     */
    public void initParameter(){
        this.weight = Math.random();
        this.bias = Math.random();
    }

    /**
     * 梯度下降更新参数
     * @param learning_rate 学习率
     * @return 损失值
     */
    public double gradientDecent(double learning_rate){
        double w_ = 0;
        double b_ = 0;
        double totalLoss = 0;
        int n = this.features.size();
//        double n = this.features.size();
        for (int i = 0; i < this.features.size(); i++) {
            double yPredict = this.features.get(i) * this.weight + this.bias;
            // loss对w的偏导
            w_ += -2 * learning_rate * this.features.get(i) * (this.targets.get(i) - yPredict) / n;
            // loss对b的偏导
            b_ += -2 * learning_rate * (this.targets.get(i) - (yPredict)) / n;

            // 计算loss用于输出
            totalLoss += Math.pow(this.targets.get(i) - yPredict, 2) / n;
        }
        //更新参数
        this.weight -= w_;
        this.bias -= b_;
        return totalLoss;
    }

    /**
     * 预测结果
     * @param features 特征值
     * @return 预测结果
     */
    public ArrayList<Double> predict(ArrayList<Double> features){
        //用于装预测结果
        ArrayList<Double> yPredict = new ArrayList<>();
        for (Double feature : features) {
            //对每个x进行预测
            yPredict.add(feature * this.weight + this.bias);
        }
        return yPredict;
    }

    public double getWeight() {
        return weight;
    }

    public double getBias() {
        return bias;
    }
}
