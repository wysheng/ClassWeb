package classification.multi_class;

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
import org.apache.spark.mllib.util.MLUtils;
import org.apache.tools.ant.taskdefs.Java;
import scala.Tuple2;
import util.ConfigUtil;

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

        //SparkConf conf = new SparkConf().setAppName("Multi class Classification Metrics Example");
//        String master = "local[*]";
//        SparkConf conf = new SparkConf()
//                .setAppName("trainModel")
//                .setMaster(master);
//        SparkContext sc = new SparkContext(conf);
//        // $example on$
//        String path = "data/corpus/sample_multiclass_classification_data.txt";
//        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();
//
//        // Split initial RDD into two... [60% training data, 40% testing data].
//        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.6, 0.4}, 11L);
//        JavaRDD<LabeledPoint> training = splits[0].cache();
//        JavaRDD<LabeledPoint> test = splits[1];
//        test.foreach(r -> {
//            System.out.println(r.label());
//
//        });
//
//        // Run training algorithm to build the model.
//        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
//                .setNumClasses(3)
//                .run(training.rdd());
//
//        // Compute raw scores on the test set.
//        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p ->
//                new Tuple2<>(model.predict(p.features()), p.label()));
//
//        // Get evaluation metrics.
//        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
//
//        // Confusion matrix
//        Matrix confusion = metrics.confusionMatrix();
//        System.out.println("Confusion matrix: \n" + confusion);
//
//        // Overall statistics
//        //System.out.println("Accuracy = " + metrics.accuracy());
//
//        // Stats by labels
//        for (int i = 0; i < metrics.labels().length; i++) {
//            System.out.format("Class %f precision = %f\n", metrics.labels()[i],metrics.precision(
//                    metrics.labels()[i]));
//            System.out.format("Class %f recall = %f\n", metrics.labels()[i], metrics.recall(
//                    metrics.labels()[i]));
//            System.out.format("Class %f F1 score = %f\n", metrics.labels()[i], metrics.fMeasure(
//                    metrics.labels()[i]));
//        }
//
//        //Weighted stats
//        System.out.format("Weighted precision = %f\n", metrics.weightedPrecision());
//        System.out.format("Weighted recall = %f\n", metrics.weightedRecall());
//        System.out.format("Weighted F1 score = %f\n", metrics.weightedFMeasure());
//        System.out.format("Weighted false positive rate = %f\n", metrics.weightedFalsePositiveRate());
//
//        // Save and load model
//        model.save(sc, "target/tmp/LogisticRegressionModel");
//        LogisticRegressionModel sameModel = LogisticRegressionModel.load(sc,
//                "target/tmp/LogisticRegressionModel");
//        // $example off$
//
//        sc.stop();

        train();
    }


    /**
     * 创建SparkContext
     *
     */
//    private static String master = "local[*]";
//    private static SparkConf conf = new SparkConf()
//            .setAppName(ClassifyController.class.getName())
//            .setMaster(master);
//    private static JavaSparkContext sc = new JavaSparkContext(conf);

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
        final HashingTF tf = new HashingTF(10000);

        // 获取类别配置
        String configpath = "data/conf/type.txt";
        final Map<String, Double> classMap = ConfigUtil.getClassConfig(configpath);

        // 创建LabelPoint数据集分别存放阳性和阴性的例子
        JavaRDD<LabeledPoint> newsClassDataRDD = newsDataRDD.map(
                new Function<String, LabeledPoint>() {
                    @Override
                    public LabeledPoint call(String line) throws Exception {
                        String[] details = line.split("\t\t\t");
                        Double label;
                        if (classMap.containsKey(details[1])) {
                            label = classMap.get(details[1]);
                        } else {
                            label = 0d;
                        }
                        return new LabeledPoint(label, tf.transform(Arrays.asList(details[3].split(" "))));
                    }
                }
        );

        // Split initial RDD into two... [60% training data, 40% testing data].
        JavaRDD<LabeledPoint>[] splits = newsClassDataRDD.randomSplit(new double[]{0.9, 0.1}, 11L);
        JavaRDD<LabeledPoint> training = splits[0].cache();
        JavaRDD<LabeledPoint> test = splits[1];

        // Run training algorithm to build the model.
//        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
//                .setNumClasses(6)
//                .run(training.rdd());
//
//        // Compute raw scores on the test set.
//        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p ->
//                new Tuple2<>(model.predict(p.features()), p.label()));
//
//        // Get evaluation metrics.
//        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
//
//        // Confusion matrix
//        Matrix confusion = metrics.confusionMatrix();
//        System.out.println("Confusion matrix: \n" + confusion);

        LogisticRegressionModel sameModel = LogisticRegressionModel.load(sc, "data/model/LogisticRegressionModel/");

        test.foreach(r -> {
            System.out.println("当前文章类别为: " + r.label() + "\t预测类别为: " + sameModel.predict(r.features()));
        });

        // Overall statistics
        //System.out.println("Accuracy = " + metrics.accuracy());

        // Stats by labels
//        for (int i = 0; i < metrics.labels().length; i++) {
//            System.out.format("Class %f precision = %f\n", metrics.labels()[i],metrics.precision(
//                    metrics.labels()[i]));
//            System.out.format("Class %f recall = %f\n", metrics.labels()[i], metrics.recall(
//                    metrics.labels()[i]));
//            System.out.format("Class %f F1 score = %f\n", metrics.labels()[i], metrics.fMeasure(
//                    metrics.labels()[i]));
//        }
//
//        //Weighted stats
//        System.out.format("Weighted precision = %f\n", metrics.weightedPrecision());
//        System.out.format("Weighted recall = %f\n", metrics.weightedRecall());
//        System.out.format("Weighted F1 score = %f\n", metrics.weightedFMeasure());
//        System.out.format("Weighted false positive rate = %f\n", metrics.weightedFalsePositiveRate());

        // Save and load model
        //model.save(sc, "data/model/LogisticRegressionModel/");

        // $example off$

        sc.stop();
    }



}
