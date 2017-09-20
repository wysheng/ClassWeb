package com.helencoder.util;

import com.helencoder.model.News;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.broadcast.Broadcast;
//import org.json.JSONException;
//import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置操作类
 *
 * Created by helencoder on 2017/8/16.
 */
public class ConfigUtil {

    /**
     * 获取全部资讯详情
     *
     * @param configpath 配置文件路径
     * @param corpuspath 语料库文件路径
     */
    public static List<News> getNewsItems(String configpath, String corpuspath) {
        List<News> newsItemsList = new ArrayList<News>();

        return newsItemsList;
//        // 创建SparkContext
//        String master = "local[*]";
//        SparkConf conf = new SparkConf()
//                .setAppName("getNewsItems")
//                .setMaster(master);
//        JavaSparkContext sc = new JavaSparkContext(conf);
//
//        // 读入配置文件
//        JavaRDD<String> newsItemsDataRDD = sc.textFile(configpath);
//
//        JavaRDD<News> newsDataRDD = newsItemsDataRDD.com.helencoder.filter(
//                new Function<String, Boolean>() {
//                    @Override
//                    public Boolean call(String line) throws Exception {
//                        JSONObject json = new JSONObject(line);
//                        if (json.equals(null)) {
//                            return false;
//                        }
//                        return true;
//                    }
//                }).map(
//                new Function<String, News>() {
//                    @Override
//                    public News call(String line) throws Exception {
//                        String[] details = new String[3];
//                        String baseUrl = "http://ai.cmbchina.com/mbf4info/CmbNewsView.aspx?newsID=";
//                        try {
//                            JSONObject json = new JSONObject(line);
//                            details[0] = json.getString("newstitle");
//                            details[1] = json.getString("newsid");
//                            details[2] = json.getString("newstypename");
//                        } catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
//                        // 获取正文信息()
//                        News news = new News();
//                        news.setId(details[1]);
//                        news.setTitle(details[0]);
//                        news.setType(details[2]);
//                        news.setLink(baseUrl + details[1]);
//
//                        return news;
//                    }
//                }
//        );
//
//        final Broadcast<List<News>> broadcastList = sc.broadcast(newsItemsList);
//        // 数据收集
//        newsDataRDD.foreach(r -> {
//            List<News> newsList = broadcastList.getValue();
//            newsList.add(r);
//        });
//
//        List<News> retList = broadcastList.getValue();
//        return retList;
    }

    /**
     * 获取指定资讯详情
     *
     * @param newsid 指定资讯的newsid
     * @param configpath 配置文件位置
     */
    public static News getSpecifiedNewsDetail(String newsid, String configpath) {
        News news = new News();
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configpath)));
//            for (String line = br.readLine(); line != null; line = br.readLine()) {
//                JSONObject json = new JSONObject(line);
//                if (!json.equals(null)) {
//                    String tmpNewsid = json.getString("newsid");
//                    if (tmpNewsid.equals(newsid)) {
//                        String type = json.getString("newstypename");
//                        String title = json.getString("newstitle");
//                        String link = "http://ai.cmbchina.com/mbf4info/CmbNewsView.aspx?newsID=" + tmpNewsid;
//                        news.setId(tmpNewsid);
//                        news.setTitle(title);
//                        news.setType(type);
//                        news.setLink(link);
//                        break;
//                    }
//                }
//            }
//            br.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return news;
    }

    /**
     * 获取资讯类型设置
     *
     * @param configpath 配置文件路径(默认存在)
     */
    public static Map<String, Double> getClassConfig(String configpath) {
        Map<String, Double> configMap = new HashMap<String, Double>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configpath)));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] details = line.split(" ");
                configMap.put(details[0], Double.parseDouble(details[1]));
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return configMap;
    }

    /**
     * 获取资讯类型设置(数字在前)
     *
     * @param configpath 配置文件路径(默认存在)
     */
    public static Map<Double, String> getClassConfigByIndex(String configpath) {
        Map<Double, String> configMap = new HashMap<Double, String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configpath)));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] details = line.split(" ");
                configMap.put(Double.parseDouble(details[1]), details[0]);
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return configMap;
    }

}
