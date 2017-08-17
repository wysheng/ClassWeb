package classification.multi_class;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
/**
 * Created by helencoder on 2017/8/17.
 */
public class MultiClassTest {
    public static void main(String[] args) {
        // 创建SparkContext
        String master = "local[*]";
        SparkConf conf = new SparkConf()
                .setAppName("trainModel")
                .setMaster(master);
//        JavaSparkContext jsc = new JavaSparkContext(conf);
        SparkContext sc = new SparkContext(conf);
        String path = "data/corpus/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc, path).toJavaRDD();

// Split initial RDD into two... [60% training data, 40% testing data].
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[] {0.6, 0.4}, 11L);
        JavaRDD<LabeledPoint> training = splits[0].cache();
        JavaRDD<LabeledPoint> test = splits[1];

// Run training algorithm to build the model.
        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
                .setNumClasses(10)
                .run(training.rdd());

// Compute raw scores on the test set.
        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p ->
                new Tuple2<>(model.predict(p.features()), p.label()));

// Get evaluation metrics.
        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
//        double accuracy = metrics.accuracy();
//        System.out.println("Accuracy = " + accuracy);

// Save and load model
        model.save(sc, "target/tmp/javaLogisticRegressionWithLBFGSModel");
        LogisticRegressionModel sameModel = LogisticRegressionModel.load(sc,
                "target/tmp/javaLogisticRegressionWithLBFGSModel");

    }
}
