package com.cc.algorithm; /**
 * Created by syl on 2017/2/17.
 */

import com.cc.bean.Ship;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TestSYL {
    public static List<Ship> predict(List<Ship> listship) {
        List exportData = new ArrayList<Map>();
        for (int i = 0; i < listship.size(); i++) {
            Map row1 = new LinkedHashMap<String, String>();
            row1.put("1", listship.get(i).getIndex());
            row1.put("2", listship.get(i).getType());
            row1.put("3", listship.get(i).getPort());
            row1.put("4", listship.get(i).getLevel());
            row1.put("5", listship.get(i).getDistrict());
            row1.put("6", listship.get(i).getLength());
            row1.put("7", listship.get(i).getWidth());
            row1.put("8", listship.get(i).getHeight());
            row1.put("9", listship.get(i).getGrosston());
            row1.put("10", listship.get(i).getDeadweight());
            row1.put("11", listship.get(i).getEnginetype());
            row1.put("12", listship.get(i).getEnginepower());
            row1.put("13", listship.get(i).getBuilddate());
            row1.put("14", listship.get(i).getFactory());
            row1.put("15", listship.get(i).getDealdate());
            exportData.add(row1);
        }
        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "编号");
        map.put("2", "船舶类型");
        map.put("3", "船籍港");
        map.put("4", "船级");
        map.put("5", "航区");
        map.put("6", "长度");
        map.put("7", "型宽");
        map.put("8", "型深");
        map.put("9", "总吨");
        map.put("10", "载重吨");
        map.put("11", "主机型号");
        map.put("12", "主机功率");
        map.put("13", "建成日期");
        map.put("14", "建造船厂");
        map.put("15", "成交日期");
        // 生成文件目录
        String path = "/home/cc/Documents/ship";
        //String path = "WEB-INF/output";

        String fileName = "new";
        File file = CSVUtils.createCSVFile(exportData, map, path, fileName);

        String fileName2 = file.getName();

//        String csv3 = "WEB-INF/output/" + fileName2;
//        File newCsv = new File(csv3);
//        String newCsvPath = newCsv.getAbsolutePath();
//
//        String predictPath = "WEB-INF/output/predict.R";
//        File pre = new File(predictPath);
//        String prePath = pre.getAbsolutePath();

        String csv = "/home/cc/Documents/ship/" + fileName2;
        String cmd = "Rscript /home/cc/Documents/ship/predict.R " + csv;

        //String cmd = "Rscript " + prePath + " " + newCsvPath;

        try {
            String[] cm = new String[]{"/bin/sh", "-c", cmd};
            Process ps = Runtime.getRuntime().exec(cm);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> result = Readcsv();//读取csv文件中结果

        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
        for (int i = 0; i < listship.size(); i++) {
            Ship ship = new Ship(listship.get(i).getId(), listship.get(i).getIndex(), listship.get(i).getType(), listship.get(i).getPort(),
                    listship.get(i).getLevel(), listship.get(i).getDistrict(), listship.get(i).getLength(), listship.get(i).getWidth(),
                    listship.get(i).getHeight(), listship.get(i).getGrosston(), listship.get(i).getDeadweight(), listship.get(i).getEnginetype(),
                    listship.get(i).getEnginepower(), listship.get(i).getBuilddate(), listship.get(i).getFactory(), listship.get(i).getDealdate()
                    , Double.valueOf(result.get(i + 1)));
            listship.set(i, ship);
        }

        CSVUtils.deleteFile("/home/cc/Documents/ship/", fileName2);//用完删除文件
        return listship;
    }


    //read the result
    public static List<String> Readcsv() {
        List<String> predict = new ArrayList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/cc/Documents/ship/jieguo.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                predict.add(line.split(",")[1].toString());
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("没找到文件！");
        } catch (IOException ex) {
            System.out.println("读写文件出错！");
        }
        return predict;
    }

}




