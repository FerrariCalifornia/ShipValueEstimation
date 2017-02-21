package com.cc.algorithm; /**
 * Created by syl on 2017/2/16.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 文件操作
 */
public class CSVUtils {

    /**
     * 生成为CVS文件
     * @param exportData
     *       源数据List
     * @param map
     *       csv文件的列表头map
     * @param outPutPath
     *       文件路径
     * @param fileName
     *       文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,
                                     String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            //定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            //   csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GBK"), 1024);
           csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                csvFileOutputStream.write("" + (String) propertyEntry.getValue() != null ? (String) propertyEntry
                                .getValue() : "" + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                        .hasNext();) {
                    Map.Entry propertyEntry = (Map.Entry) propertyIterator
                            .next();
                    csvFileOutputStream.write((String) BeanUtils.getProperty(row,
                            (String) propertyEntry.getKey()));
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 下载文件
     * @param response
     * @param csvFilePath
     *       文件路径
     * @param fileName
     *       文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName)
            throws IOException {
        response.setContentType("application/csv;charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     * @param filePath
     *      文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     * @param filePath
     *     文件目录路径
     * @param fileName
     *     文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().equals(fileName)) {
                        files[i].delete();
                        return;
                    }
                }
            }
        }
    }

    /**
     * 测试数据
     * @param args
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) {
        List exportData = new ArrayList<Map>();
        Map row1 = new LinkedHashMap<String, String>();
        row1.put("1", "TEST331");
        row1.put("2", "油船");
        row1.put("3", "深圳");
        row1.put("4", "ZC");
        row1.put("5", "内河");
        row1.put("6", "46.5");
        row1.put("7", "8.6");
        row1.put("8", "3.6");
        row1.put("9", "385");
        row1.put("10", "503");
        row1.put("11", "8140ZC450-1");
        row1.put("12", "660");
        row1.put("13", "10/31/2011");
        row1.put("14", "台州市路桥金清海祥船舶修造有限公司");
        row1.put("15", "9/1/2016");
        exportData.add(row1);
        row1 = new LinkedHashMap<String, String>();
        row1.put("1", "TEST331");
        row1.put("2", "油船");
        row1.put("3", "深圳");
        row1.put("4", "ZC");
        row1.put("5", "内河");
        row1.put("6", "46.5");
        row1.put("7", "8.6");
        row1.put("8", "3.6");
        row1.put("9", "385");
        row1.put("10", "503");
        row1.put("11", "8140ZC450-1");
        row1.put("12", "660");
        row1.put("13", "10/31/2011");
        row1.put("14", "台州市路桥金清海祥船舶修造有限公司");
        row1.put("15", "9/1/2016");
        exportData.add(row1);
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
        String path = "c:/export/";
        String fileName = "new";
        File file = CSVUtils.createCSVFile(exportData, map, path, fileName);
        String fileName2 = file.getName();
      //  CSVUtils.deleteFiles(exportData, map, path, fileName);
        System.out.println("文件名称：" + fileName2);
    }
}

