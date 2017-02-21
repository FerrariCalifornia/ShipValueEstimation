package com.cc.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by cc on 2017/2/21.
 */
public class Test2 {
    public static void main(String[] args){
        String csv = "/home/cc/Documents/ship/" + "new554370993272613098.csv";
        String cmd = "Rscript /home/cc/Documents/ship/predict.R " + csv;
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

    }
}
