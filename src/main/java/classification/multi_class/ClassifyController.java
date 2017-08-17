package classification.multi_class;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.tools.ant.taskdefs.Java;
import scala.Tuple2;
import util.ConfigUtil;
import util.FileUtil;

import java.util.Arrays;
import java.util.Map;

/**
 * 多类别分类控制器
 *
 * Created by helencoder on 2017/8/17.
 */
public class ClassifyController {

    public static void main(String[] args) {
        // 测试用例

        String path = "data/corpus/zixun/0a0cd120-6e4d-4386-b8e2-5cd86df10da5.txt";
        String content = FileUtil.getFileData(path);
        String label = classPredict(content);
        System.out.println(label);

//        train();

    }

    /**
     * 分类器预测(默认使用1000000维训练数据)
     */
    public static String classPredict(String content) {
        // windows下保存模型配置
        System.setProperty("hadoop.home.dir","D:\\software\\winutils");
        // 创建SparkContext
        String master = "local[*]";
        SparkConf conf = new SparkConf()
                .setAppName("trainModel")
                .setMaster(master);
        SparkContext sc = new SparkContext(conf);

        // 加载训练模型
        LogisticRegressionModel model = LogisticRegressionModel.load(sc, "data/model/LogisticRegressionModelBy1000000/");

        // 待预测数据模型构建
        final HashingTF tf = new HashingTF(1000000);
        Vector features = tf.transform(Arrays.asList(content.split(" ")));

        Double predictedClass = model.predict(features);
        // 获取类别配置
        String configpath = "data/conf/type.txt";
        Map<Double, String> classMap = ConfigUtil.getClassConfigByIndex(configpath);
        if (classMap.containsKey(predictedClass)) {
            return classMap.get(predictedClass);
        } else {
            return null;
        }
    }

    /**
     * 分类器训练
     */
    public static void train() {
        // windows下保存模型配置
        System.setProperty("hadoop.home.dir","D:\\software\\winutils");
        // 创建SparkContext
        String master = "local[*]";
        SparkConf conf = new SparkConf()
                .setAppName("trainModel")
                .setMaster(master);
//        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkContext sc = new SparkContext(conf);
        //String path = "data/corpus/sample_multiclass_classification_data.txt";
//        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();

        String newspath = "data/corpus/newsSegData.txt";
        JavaRDD<String> newsDataRDD = sc.textFile(newspath, 1).toJavaRDD();

        // 创建一个HashingTF实例来把邮件文本映射为包含10000个特征的向量
        final HashingTF tf = new HashingTF(1000000);

        // 获取类别配置
        String configpath = "data/conf/type.txt";
        final Map<String, Double> classMap = ConfigUtil.getClassConfig(configpath);

        // 创建LabelPoint数据集分别存放阳性和阴性的例子
        JavaRDD<LabeledPoint> newsClassDataRDD = newsDataRDD.filter(
                new Function<String, Boolean>() {
                    @Override
                    public Boolean call(String line) throws Exception {
                        String[] details = line.split("\t\t\t");
                        if (classMap.containsKey(details[1])) {
                            return true;
                        }
                        return false;
                    }
                }).map(
                new Function<String, LabeledPoint>() {
                    @Override
                    public LabeledPoint call(String line) throws Exception {
                        String[] details = line.split("\t\t\t");
                        Double label = classMap.get(details[1]);
                        return new LabeledPoint(label, tf.transform(Arrays.asList(details[3].split(" "))));
                    }
                }
        );

        // Split initial RDD into two... [60% training data, 40% testing data].
        JavaRDD<LabeledPoint>[] splits = newsClassDataRDD.randomSplit(new double[]{0.99, 0.01}, 11L);
        JavaRDD<LabeledPoint> training = splits[0].cache();
        JavaRDD<LabeledPoint> test = splits[1];

        // Run training algorithm to build the model.
        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
                .setNumClasses(6)
                .run(training.rdd());

        // Compute raw scores on the test set.
        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

        // Get evaluation metrics.
        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());

        // Confusion matrix
        Matrix confusion = metrics.confusionMatrix();
        System.out.println("Confusion matrix: \n" + confusion);

        //LogisticRegressionModel sameModel = LogisticRegressionModel.load(sc, "data/model/LogisticRegressionModelBy10000/");

        // 累加器
        //final Accumulator<Integer> correctCount = sc.accumulator(0);
        test.foreach(r -> {
            System.out.println("当前文章类别为: " + r.label() + "\t预测类别为: " + model.predict(r.features()));
        });

        // Overall statistics
        //System.out.println("Accuracy = " + metrics.accuracy());

        // Stats by labels
        for (int i = 0; i < metrics.labels().length; i++) {
            System.out.format("Class %f precision = %f\n", metrics.labels()[i],metrics.precision(
                    metrics.labels()[i]));
            System.out.format("Class %f recall = %f\n", metrics.labels()[i], metrics.recall(
                    metrics.labels()[i]));
            System.out.format("Class %f F1 score = %f\n", metrics.labels()[i], metrics.fMeasure(
                    metrics.labels()[i]));
        }

        //Weighted stats
        System.out.format("Weighted precision = %f\n", metrics.weightedPrecision());
        System.out.format("Weighted recall = %f\n", metrics.weightedRecall());
        System.out.format("Weighted F1 score = %f\n", metrics.weightedFMeasure());
        System.out.format("Weighted false positive rate = %f\n", metrics.weightedFalsePositiveRate());

        // Save and load model
        model.save(sc, "data/model/LogisticRegressionModelBy1000000/");

        // $example off$

        sc.stop();
    }



}
