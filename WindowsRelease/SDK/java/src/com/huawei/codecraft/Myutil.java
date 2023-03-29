package com.huawei.codecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Myutil {
    
//    public String s = "E:\\2023hwcode\\2023\\huawei2023\\WindowsRelease\\debug\\debug.txt";


//    public String s = "D:\\Huawei2023\\huawei2023\\WindowsRelease\\debug\\debug.txt";

    public String s = "C:\\Users\\Eward\\Documents\\gitcode\\huawei2023\\WindowsRelease\\debug\\debug.txt";
    File file = new File(s);
    FileWriter fw;

    {
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outMapCandyPlus(int[][][][] data) throws IOException {

        File file = new File(s);

        FileWriter fw = new FileWriter(file);
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 1; k < 10; k++) {
                    for (int l = 0; l < 30; l++) {
                        if (data[j][i][k][l] < 10) {
                            fw.write(data[i][j] + "  ");
                        } else {
                            fw.write(data[i][j] + " ");

                        }

                        fw.write("==============================================================================");
                    }
                    fw.write("/////////////////////////////////////////////////////////////////////////");
                }


                fw.flush();
            }
        }
    }

    public void outReqFrames(int[][] data) throws IOException {

        File file = new File(s);

        FileWriter fw = new FileWriter(file);
        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 31; j++) {
                if (data[i][j] < 10){
                    fw.write(data[i][j] + "    ");
                }else if(data[i][j] < 100){
                    fw.write(data[i][j] + "   ");
                }else if(data[i][j] < 1000){
                    fw.write(data[i][j] + "  ");
                }else {
                    fw.write(data[i][j] + " ");
                }

            }
            fw.write("\r\n");

        }

        fw.flush();
    }

    public void output(List<Float> data) throws IOException {

        File file = new File(s);

        FileWriter fw = new FileWriter(file);

        for (int i = 0; i < data.size(); i++) {
            fw.write(data.get(i) +"");
            fw.write("\r\n");
//            if (i == 10) {
//                fw.write("\n");
//            }

        }
        fw.flush();
    }
    
    public void outstr_aline(String str) throws IOException {
        fw.write(str + " ");
        
    }
    
    


    // 输出字符串
    public void outStr(String str) throws IOException {
        fw.write(str);
        fw.write("\r\n");
    }

    // 求四个数的最小值
    public double myMin(double a, double b, double c, double d){
        double min = a;
        if (min > b) {
            min = b;
        }
        if (min > c) {
            min = c;
        }
        if (min > d) {
            min = d;
        }
//        double min = ((a < b ? a : b) < (c < d ? c : d)) ? (a < b ? a : b) : (c < d ? c : d);
        return  min;

    }


    
    public void output_colltime(List<Robot> robot) throws IOException {
//        String s = "E:\\2023hwcode\\2023\\huawei2023\\WindowsRelease\\debug\\debug.txt";
        String s = "D:\\Huawei2023\\huawei2023\\WindowsRelease\\debug\\debug.txt";
        File file = new File(s);
        
        FileWriter fw = new FileWriter(file);
        
        for (int i = 0; i < robot.size(); i++) {
            fw.write(i+"  "+robot.get(i).getTest_colltime() +" ");
            fw.write("\r\n");
//            if (i == 10) {
//                fw.write("\n");
//            }
        
        }
        fw.flush();
    }

}
