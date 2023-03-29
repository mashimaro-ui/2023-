package com.huawei.codecraft;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.io.*;
import java.util.*;

public class Main {
    //    人工势场参数


    public static double D;           //机器人与工作台之间的距离
    public static double fGravity = 0;    //引力大小
    public static double fGravityDir = 0;    //引力方向

    public static double d1 = 0;           //机器人之间的距离
    public static double d2 = 0;           //机器人之间的距离
    public static double d3 = 0;           //机器人之间的距离
    public static double fRepulsion_1 = 0;  //斥力1大小
    public static double fRepulsion_2 = 0;  //斥力2大小
    public static double fRepulsion_3 = 0;  //斥力3大小
    public static double fRepulsionDir_1 = 0;  //斥力1方向
    public static double fRepulsionDir_2 = 0;  //斥力2方向
    public static double fRepulsionDir_3 = 0;  //斥力3方向

    public static double fxResultant,fyResultant;//x轴合力，y轴合力
    public static double angleRes;    //合角度
    public static double ks = 1.2;     //引力势场增益系数
    public static double Alpha = 60; //斥力系数
    public static double dd = 10;     //表示机器人距离障碍物5米内算法才生效
    


    // 用来存工作台对象

    public  static List<Station> sta = new ArrayList<>();
    public  static List<Station> stalist_1 = new ArrayList<>();
    public  static List<Station> stalist_2 = new ArrayList<>();
    public  static List<Station> stalist_3 = new ArrayList<>();
    public  static List<Station> stalist_4 = new ArrayList<>();
    public  static List<Station> stalist_5 = new ArrayList<>();
    public  static List<Station> stalist_6 = new ArrayList<>();
    public  static List<Station> stalist_7 = new ArrayList<>();
    public  static List<Station> stalist_8 = new ArrayList<>();
    public  static List<Station> stalist_9 = new ArrayList<>();

    public  static List<Station> stalist_1_new = new ArrayList<>();
    public  static List<Station> stalist_2_new = new ArrayList<>();
    public  static List<Station> stalist_3_new = new ArrayList<>();
    public  static List<Station> stalist_4_new = new ArrayList<>();
    public  static List<Station> stalist_5_new = new ArrayList<>();
    public  static List<Station> stalist_6_new = new ArrayList<>();
    public  static List<Station> stalist_7_new = new ArrayList<>();
    public  static List<Station> stalist_8_new = new ArrayList<>();
    public  static List<Station> stalist_9_new = new ArrayList<>();

    public  static List<Station> stalist_456 = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_465 = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_546 = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_564 = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_645 = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_654 = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_654_new = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合
    public  static List<Station> stalist_456_new = new ArrayList<>();  // 对456进行的融合，以456分别排好序的顺序进行逐个融合

    public  static List<Station> stalist9_456 = new ArrayList<>();  // 先将456存进去，然后以129的和为基准进行排序
    public static int cnt = 0;
    // 用来存机器人对象
    public static List<Robot> rob = new ArrayList<>();
    // 映射坐标和工作台编号
    public static Map<Double, Integer> map = new HashMap<>();

    public static final Scanner inStream = new Scanner(System.in);

    public static final PrintStream outStream = new PrintStream(new BufferedOutputStream(System.out));

//    public static final BufferedReader inStream = new BufferedReader(new InputStreamReader(System.in));
//
//    public static final PrintStream outStream = new PrintStream(new BufferedOutputStream(System.out), true);

    public static List<Integer> task = new ArrayList<>();


    // 用于记录机器人从编号为 x 的工作台，到编号为 y 的工作台所需最大帧数
    public static int[][] reqFrames = new int[50][50];


    public static int[][][] mapCandies123 = new int[50][50][6];
    public static int[][][] mapCandies456 = new int[50][50][6];
    // 所有编号的工作站到坐标为x，y的距离
    public static int[][][] mapCandies = new int[50][50][100];

    // 不同类型的工作站按照到坐标x，y距离进行排序，存的是编号
    public static int[][][] mapCandie1 = new int[50][50][40];
    public static int[][][] mapCandie2 = new int[50][50][40];
    public static int[][][] mapCandie3 = new int[50][50][40];
    public static int[][][] mapCandie4 = new int[50][50][40];
    public static int[][][] mapCandie5 = new int[50][50][40];
    public static int[][][] mapCandie6 = new int[50][50][40];
    public static int[][][] mapCandie7 = new int[50][50][40];
    public static int[][][] mapCandie8 = new int[50][50][40];
    public static int[][][] mapCandie9 = new int[50][50][40];

    public static int[][][] mapFrames = new int[50][50][100];

    // 用于记录工作台离墙壁的最小距离
    public static double mindistance = Integer.MAX_VALUE;

    // 工具类，用于输出文件等
    public static Myutil myutil = new Myutil();
    public static int[] global456 = new int [3];

    public static boolean ismap1 = false;
    public static boolean ismap2 = false;
    public static boolean ismap3 = false;
    public static boolean ismap4 = false;
    public static int mapID = 0;

    //用于存放两个机器人上一次的距离，来判断连个机器人是在靠近还是在远离
    public static double[][] lastD = new double[4][4];

    public static void main(String[] args) throws IOException, InterruptedException {

//         想跳帧，下面这些取消注释
        // 如果在本地调试时不需要重启，在启动参数中添加restart，如：java -jar main.jar restart
//        if (args.length <= 0) {
//            ProcessBuilder pb = new ProcessBuilder();
//            pb.command("java", "-jar", "-Xmn512m", "-Xms1024m", "-Xmx1024m",
//                    "-XX:TieredStopAtLevel=1", "main.jar", "restart");
//            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
//            pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
//            Process p = pb.start();
//            p.waitFor();
//        } else if (!args[0].equals("restart")) {
//            System.out.println("err");
//        } else {
//            //do something
//            schedule();
//        }

        schedule();
        // 用于输出工作站到另一个工作站所需的帧数，上传请关闭！！！！！！！！！！！！！！！！！！！！！！！！！
//        myutil.outReqFrames(reqFrames);

        // 用于关闭输出文件，上传请关闭！！！！！！！！！！！！！！！！！！！！！！！！！
//        myutil.fw.flush();

    }

    public static void isMap(){
        int nums = sta.size();
        switch (nums)
        {
            case 43 :
                ismap1 = true; ismap2 = false; ismap3 = false; ismap4 = false; resave1(); mapID = 1;break;
            case 25 :
                ismap1 = false; ismap2 = true; ismap3 = false; ismap4 = false; resave2(); mapID = 2;break;
            case 50 :
                ismap1 = false; ismap2 = false; ismap3 = true; ismap4 = false; resave3(); mapID = 3;break;
            case 18 :
                ismap1 = false; ismap2 = false; ismap3 = false; ismap4 = true; resave4(); mapID = 4;break;
            default: break;
        }


    }
    
    
    
    private static void schedule() throws IOException {
        readUtilOK();
        makeReqFrames();        //  初始化， 从编号为x的工作站到编号为y的工作站所需要的帧数
        makeMapCandy();     // 存储当机器人处在x，y坐标时距离各工作站的距离, 以及到各工作站所需的帧数
        makeMapCandyPlus();     // 存储当机器人处在x，y坐标时距离各类型工作站的距离，且各类型按照距离进行排序
        makeMapCandy123();      // 存储当机器人处在x，y坐标时距离123工作站的最短距离，及对应工作站的编号
        makeMapCandy456();      // 存储当机器人处在x，y坐标时距离456工作站的最短距离，及对应工作站的编号
        staSpace();     // 对工作站456里面的123进行排序
        sumSpace();     // 对工作站456进行排序, 以及合并
        isMap();
        mergesort(stalist_4_new, stalist_5_new, stalist_6_new, stalist_456_new);
        outStream.println("OK");
        outStream.flush();
        

        
        int frameID = 0;
        int money;
        while (inStream.hasNextLine()) {
            while(true){
                String line = inStream.nextLine();
                // 要先判断一下             ok，表示每一帧的结束
                if(line.equals("OK")) {
                    break;
                }
                String[] parts = line.split(" ");
                // 读取帧序号
                frameID = Integer.parseInt(parts[0]);
                // 读取当前金钱数
                money = Integer.parseInt(parts[1]);
                // 读取工作台数
                line = inStream.nextLine();
                int k = Integer.parseInt(line);

                // 读取工作台信息
                for (int i = 0; i < k; i++) {
                    line = inStream.nextLine();

                    parts = line.split(" ");
                    // 根据每帧的信息，更新相应对象的信息
                    //首先需要找到这个工作台的序号
                    double idx = Double.parseDouble(parts[1])*10000 + Double.parseDouble(parts[2]);
                    int number = map.getOrDefault(idx,0);
                    //得到了 工作台序号 下面进行更新操作
                    sta.get(number).updateState(parts[3],parts[4],parts[5]);
                }

                // 读取机器人信息
                for (int i = 0; i < 4; i++) {
                    line = inStream.nextLine();
                    parts = line.split(" ");
                    // 根据每帧的信息，更新相应对象的信息
                    rob.get(i).updateState(parts[0],parts[1],parts[2],parts[3],parts[4],parts[5],parts[6],parts[7],parts[8],parts[9]); // 更新工作台ID
                }

            }

            
//            readUtilOK();
            outStream.printf("%d\n", frameID);
            int remain_frame = 9000 - frameID;
            findGlobal_456();

            
            
            
///*
//=========================================================================================
//=========================================================================================
//=========================================================================================
//=========================================================================================
//=========================================================================================
//=========================================================================================
// */

//            // 输出每帧的相关信息，提交版请注释！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
//            outMessage(frameID);

            //循环是关键找下一个目的地，以及输出速度与方向
            for (int robotId = 0; robotId < 4; robotId++) {


                Robot robot_now = rob.get(robotId);

                //只有空闲的robot才会进入

                if(robot_now.isbusy == false) {


                    if(remain_frame > 999 ){
                        //这里把9的优先级变为最高，仅用于3图

                        if (ismap2){
                            if (map2(frameID,robot_now,robotId)){
                                robot_now.equationCalculate(frameID);
                                continue;
                            }
                        }

                        if(ismap3) {
                            //专门用于找456-9的程序
                            //todo 递归的函数写好
                            //图3：调用的是searchsation2_new
                            if(robot_now.searchStation1(stalist_9_new) ){
                                robot_now.equationCalculate(frameID);
                                continue;
                            }
                        }
                        
//                        if(ismap4){
//                            boolean istrue = false;
//                            List<Station> list_temp = new ArrayList<>();
//                            for(Station station_4 : stalist_4){
//                                if(station_4.queryDemand().size()==1){
//                                    list_temp.add(station_4);
//                                    if(robot_now.searchStation1_new(list_temp)){
//                                        istrue = true;
//                                        break;
//                                    }
//                                }
//
//                            }
//                            if(istrue){
//                                continue;
//                            }
//
//                        }

                        if (ismap1 && ismap3){
//                        if(!ismap4){
                            //图124：1-4-7-8逻辑  图3: 1-4-2-4-7-8
                            //图124：调用的是searchsation2
                            //图3没有stalist7,进不来
                            //找7站点的8,然后进行递归操作，传入的是stalist_7,其实是对8进行递归找操作。
                            if(robot_now.searchStation3(stalist_7_new)){
                                robot_now.equationCalculate(frameID);
                                continue;
                            }
                        }
    
                        if(ismap4){
                            if(robot_now.searchStation5_new(stalist_7)){
                                continue;
                            }
                            if(robot_now.searchStation4_new(stalist_7)){
                                continue;
                            }
                            
                        }
                        if (!ismap2) {
                            //searchstation7的操作:456-7
                            if (robot_now.searchStation1(stalist_7_new)) {
                                robot_now.equationCalculate(frameID);
                                continue;
                            }

                            findnextStation3(robot_now, robotId, global456);
                            robot_now.equationCalculate(frameID);
                        }





                        // 单独用这个会好一点
//                    findnextStation1(robot_now, robotId);



                    }else{
                        if(remain_frame<100){
                        }else{
                            findnextStation4(robot_now,remain_frame);
                            robot_now.equationCalculate(frameID);
                        }
                    }


                }else{
                    if(remain_frame > 999 ) {
                        if (robot_now.exchangeFlag == true) {
                            for (int robotId2 = robotId + 1; robotId2 < 4; robotId2++) {
                                Robot robot_another = rob.get(robotId2);
                                if (robot_another.isbusy == true && robot_another.exchangeFlag == true && robot_now.carrygood == robot_another.carrygood) {
                                    if(robot_now.target.size() > robot_now.task_i && robot_another.target.size() > robot_another.task_i){
                                        exchange(robot_now, robot_another, mapCandies);
                                        robot_now.equationCalculate(frameID);
                                        robot_another.equationCalculate(frameID);
                                    }

                                }else if(robot_another.isbusy == false && robot_another.exchangeFlag == true && robot_now.carrygood ==0 && robot_another.carrygood == 0){
                                    //图二正优化
//                                    exchange_2(robot_now,robot_another,mapCandies);

                                }

                            }
                        }
                    }
                    //todo 逻辑！！！！！！！

                }
                //任务指针可以被找到
                if(robot_now.task_i < robot_now.target.size()){
                
                //计算一遍当前车的合力方向 ！！！！！！
//                calculateForce(robot_now,robotId);





//                boolean collflag = true;
//                if(ismap3== false){
//                    for (int i = 0; i < 4; i++){
//                        if (i == robotId){
//                            continue;
//                        }
//                        Robot robotB = rob.get(i);
//                        if (!collisionDetection(robot_now , robotB)){
//                            robot_now.equationCalculate(frameID);
//                            robotB.equationCalculate(frameID);
//                            collflag = false;
//                        }else {
//
//                        }
//                    }
//                }
                
//                if(collflag == true ){
////                if(collflag == true && ismap3 == false ){
//                    robot_now.setV_d(6);
//                }
                //运动路径
                robot_now.calculate(sta,frameID);

                    //防碰撞
                    for (int i = 0; i < 4; i++) {
                        if (i == robotId) {
                            continue;
                        }
                        Robot robotB = rob.get(i);
                        lastD[robotId][i] = F2F_AntiColl(robot_now,robotB,lastD[robotId][i],mapID);
                        lastD[i][robotId] = F2F_AntiColl(robot_now,robotB,lastD[robotId][i],mapID);
                        
                    }

                

                
                outStream.printf("forward %d %f\n", robotId, robot_now.getV_r());
                outStream.printf("rotate %d %f\n", robotId, robot_now.getPhi_r());
                }else{
                    outStream.printf("forward %d %f\n", robotId, (float) 0);
                    outStream.printf("rotate %d %f\n", robotId, (float) 0);
                }

            }

            // 输出每帧的相关信息，提交版请注释！！！！！！！
//            outForceMessage(frameID);


            
            //先不整合，这里后续需要优化
            for (int robotId = 0; robotId < 4; robotId++) {
                Robot robot = rob.get(robotId);
                
                
                if(robot.task_i < robot.target.size()){
                    int i = robot.task_i;
                    //任务目标
                    int station_no = robot.target.get(i);
                    //购买命令，满足条件：1机器人确定在站点附近。2机器人确定没有装备物品。3该站点确定生成的物品
                    if( station_no == robot.nowloaction && robot.carrygood == 0  && sta.get(station_no).isHavegenerated()){
                        // 下面几行记录机器人去的上一个工作站当当前工作站所花费的帧数
                        if(robot.task_i == 0){
                            robot.lastloaction = robot.nowloaction;
                            robot.lastFrame = frameID;
                        }else if(robot.task_i == 1){
                            reqFrames[robot.lastloaction][robot.nowloaction] = frameID - robot.lastFrame;
                            robot.lastloaction = robot.nowloaction;
                            robot.lastFrame = frameID;

                        }else{
                            reqFrames[robot.lastloaction][robot.nowloaction] = (reqFrames[robot.lastloaction][robot.nowloaction] + frameID - robot.lastFrame) >> 1;
                            robot.lastloaction = robot.nowloaction;
                            robot.lastFrame = frameID;
                        }

                        if(remain_frame < 499){
                            int next_station_no = robot.target.get(robot.task_i + 1 );
                            int frame_now_to_next = reqFrames[station_no][next_station_no];
                            if(frame_now_to_next < remain_frame){

                                outStream.printf("buy %d\n",robotId);
                                robot.exchangeFlag = true;
                                int nowgoodtpye = sta.get(station_no).types;
                                if( nowgoodtpye == 4 || nowgoodtpye== 5 || nowgoodtpye == 6 || nowgoodtpye == 7){
                                    //该站点置为空闲
                                    sta.get(station_no).setFlag(false);
                                }
                                //任务需要更改
                                rob.get(robotId).task_i++;
                                rob.get(robotId).cnt_frame = 100;
                                //每次task_i++都需要equationcalculate
                                robot.equationCalculate(frameID);
                            }


                        }else{
                            outStream.printf("buy %d\n",robotId);
                            robot.exchangeFlag = true;
                            int nowgoodtpye = sta.get(station_no).types;
                            if( nowgoodtpye == 4 || nowgoodtpye== 5 || nowgoodtpye == 6 || nowgoodtpye == 7){
                                //该站点置为空闲
                                sta.get(station_no).setFlag(false);
                            }
                            //任务需要更改
                            rob.get(robotId).task_i++;
                            rob.get(robotId).cnt_frame = 100;
                            //每次task_i++都需要equationcalculate
                            robot.equationCalculate(frameID);
                        }
                        
                        


//                        if(sta.get(station_no).types !=7){
//                        //该站点置为空闲
//                        sta.get(station_no).setFlag(true);
//                        }else{
//                        }
//                        //任务需要更改
//                        rob.get(robotId).task_i++;

                    }
                    //卖出指令,满足条件：1机器人确定在站点附近。2机器人确定携带物品。3
                    //queryDemand重写了方法，导致卖不出去
//                    if( robot.nowloaction == station_no &&   sta.get(station_no).queryDemand().contains(robot.carrygood) ){
                    if( robot.nowloaction == station_no  && sta.get(station_no).can_sell(robot.carrygood) ){
                        // 下面几行记录机器人去的上一个工作站当当前工作站所花费的帧数
                        if(robot.task_i == 0){
                            robot.lastloaction = robot.nowloaction;
                            robot.lastFrame = frameID;
                        }else if(robot.task_i == 1){
                            reqFrames[robot.lastloaction][robot.nowloaction] = frameID - robot.lastFrame;
                            robot.lastloaction = robot.nowloaction;
                            robot.lastFrame = frameID;

                        }else{
                            reqFrames[robot.lastloaction][robot.nowloaction] = (reqFrames[robot.lastloaction][robot.nowloaction] + frameID - robot.lastFrame) >> 1;
                            robot.lastloaction = robot.nowloaction;
                            robot.lastFrame = frameID;
                        }


                        outStream.printf("sell %d\n",robotId);
                        robot.exchangeFlag = true;
                        //确定卖出？
//                        if(rob.get(robotId).task_i >= rob.get(robotId).target.size() -4 ){
//                            sta.get(station_no).change(robot.carrygood);
//                        }
                        sta.get(station_no).change(robot.carrygood);
                        rob.get(robotId).task_i++;
                        //每次task_i++都需要equationcalculate
                        robot.equationCalculate(frameID);

                        rob.get(robotId).cnt_frame = 100;
                        //目前quickfind有问题,鬼屎
//                        quickfind(robot,station_no);
                        //只有执行完一系列任务置false;
                        if(rob.get(robotId).task_i >= rob.get(robotId).target.size() ){
                            rob.get(robotId).isbusy = false;
                            rob.get(robotId).small_thing = false;
                            rob.get(robotId).small_to_456 = -1;
                        }
                        
                        
                        
                        
                        
                    }
                    
                }
                
            }

            outStream.print("OK\n");
            outStream.flush();
        }



//        Myutil myutil = new Myutil();
//        myutil.output_colltime(rob);

    
    
    }
    
    
    public static void calculateForce(Robot robot_now,int robotId){
        
        //人工势场碰撞避免算法
        switch (robotId)
        {
            case 0 :
                calculate(robot_now,0,1,2,3); break;
            case 1 :
                calculate(robot_now,1,0,2,3); break;
            case 2 :
                calculate(robot_now,2,0,1,3); break;
            case 3 :
                calculate(robot_now,3,0,1,2); break;
            default: break;
        }
        D = Math.sqrt(Math.pow( robot_now.x_d - robot_now.x , 2 ) + Math.pow( robot_now.y_d - robot_now.y , 2 ));

        fGravity = 1 * ks * D;     // 引力的大小
        fGravityDir = Math.atan2(robot_now.y_d - robot_now.y, robot_now.x_d - robot_now.x);

        // 用于debug，上传可以注释了！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        fuZhi(robotId);

        if(D < 5){
            fxResultant = 0 ;
            fyResultant = 0 ;
            angleRes = Math.atan2(fyResultant, fxResultant);
        }else{
            // 合力在x轴和y轴上的分力
            fxResultant = fGravity * Math.cos(fGravityDir) + fRepulsion_1 * Math.cos(fRepulsionDir_1) + fRepulsion_2 * Math.cos(fRepulsionDir_2) + fRepulsion_3 * Math.cos(fRepulsionDir_3);
            fyResultant = fGravity * Math.sin(fGravityDir) + fRepulsion_1 * Math.sin(fRepulsionDir_1) + fRepulsion_2 * Math.sin(fRepulsionDir_2) + fRepulsion_3 * Math.sin(fRepulsionDir_3);
            // 合力的方向
            angleRes = Math.atan2(fyResultant, fxResultant);
        }
    }


    public static void fuZhi(int robotId){
        rob.get(robotId).jiGongJu = D;
        rob.get(robotId).yinLi = fGravity;
        rob.get(robotId).yinLiFang = fGravityDir;
        rob.get(robotId).jiJiJu1 = d1;
        rob.get(robotId).jiJiJu2 = d2;
        rob.get(robotId).jiJiJu3 = d3;
        rob.get(robotId).chiLi1 = fRepulsion_1;
        rob.get(robotId).chiLi2 = fRepulsion_2;
        rob.get(robotId).chiLi3 = fRepulsion_3;
        rob.get(robotId).chiLiFang1 = fRepulsionDir_1;
        rob.get(robotId).chiLiFang2 = fRepulsionDir_2;
        rob.get(robotId).chiLiFang3 = fRepulsionDir_3;

        rob.get(robotId).xHeLi = fxResultant;
        rob.get(robotId).yHeLi = fyResultant;
        rob.get(robotId).heLiJiao = angleRes;

    }



    //对头防碰撞
    public static double F2F_AntiColl(Robot robot1 , Robot robot2 , double lastd , int mapID){
        boolean approachFlag = false;
        double d = Math.sqrt( Math.pow( robot1.y - robot2.y , 2) + Math.pow(robot1.x - robot2.x , 2 ) );
//      判断两个机器人是否在靠近
        if (d < lastd){
            approachFlag = true;
        }
        lastd = d;
        double angle1 = robot1.direction;
        double angle2 = robot2.direction;
        if (angle1 < 0){
            angle1 += 2 * Math.PI;
        }
        if (angle2 < 0){
            angle2 += 2 * Math.PI;
        }

        double eAngle = angle1 - angle2;
        if (angle1 > 0 && angle1 < Math.PI && eAngle < -Math.PI){
            eAngle += 2 * Math.PI;
        }
        if (angle1 > Math.PI && angle1 < 2 * Math.PI && eAngle > Math.PI){
            eAngle -= 2 * Math.PI;
        }
        //机器人是否在以下范围内
        boolean scopeFlag = false;
        switch (mapID) {
            case 1:
                if (robot1.x > 15 && robot1.x < 35 && robot1.y > 12.5 && robot1.y < 25) {
                    scopeFlag = true;
                }
                break;
            case 2:
                break;
            case 3:
                if (robot1.x > 15 && robot1.x < 35 && robot1.y > 12.5 && robot1.y < 25) {
                    scopeFlag = true;
                }
                break;
            case 4:
                break;
        }
        //防对头碰
        if (!ismap2) {
            if (d < 4 && Math.abs(eAngle) >= 0.75 * Math.PI && Math.abs(eAngle) <= Math.PI && approachFlag && !scopeFlag) {
                robot1.setPhi_r(Math.PI / 2);
                robot2.setPhi_r(Math.PI / 2);
            }
        }else if (d < 4 && Math.abs(eAngle) >= 0.6 * Math.PI && Math.abs(eAngle) <= Math.PI && approachFlag && !scopeFlag){
            double x_ = (robot1.x + robot2.x) / 2;
            double y_ = (robot1.y + robot2.y) / 2;
            double angled1 = Math.atan2(y_ - robot1.y , x_ - robot1.x);
            double angled2 = Math.atan2(y_ - robot2.y , x_ - robot2.x);
            if (angled1 < 0){
                angled1 += 2 * Math.PI;
            }
            if (angled2 < 0){
                angled2 += 2 * Math.PI;
            }
            //添加了左转右转判断
            double eAngle1 = angle1 - angled1;
            double eAngle2 = angle2 - angled2;
            if (angle1 > 0 && angle1 < Math.PI && eAngle1 < -Math.PI){
                eAngle1 += 2 * Math.PI;
            }
            if (angle1 > Math.PI && angle1 < 2 * Math.PI && eAngle1 > Math.PI){
                eAngle1 -= 2 * Math.PI;
            }
            if (angle2 > 0 && angle2 < Math.PI && eAngle2 < -Math.PI){
                eAngle2 += 2 * Math.PI;
            }
            if (angle2 > Math.PI && angle2 < 2 * Math.PI && eAngle2 > Math.PI){
                eAngle2 -= 2 * Math.PI;
            }
            if (eAngle1 >= 0){
                robot1.setPhi_r( Math.PI / 2 );
            }else {
                robot1.setPhi_r( -Math.PI / 2 );
            }
            if (eAngle2 >= 0){
                robot2.setPhi_r( Math.PI / 2 );
            }else {
                robot2.setPhi_r( -Math.PI / 2 );
            }
        }
        //防蹭
        if (d < 1.25 && Math.abs(robot1.velocity) <= 0.75 && Math.abs(robot2.velocity ) <= 0.75){
            robot1.setPhi_r( Math.PI / 2 );
            robot2.setPhi_r( Math.PI / 2 );
            if (ismap1) {
            robot1.setV_r( -0.75 );
            robot2.setV_r( -0.75 );
            }
        }
        
        //防并排走
        double D1 = Math.pow( robot1.x_d - robot1.x , 2 ) + Math.pow( robot1.y_d - robot1.y , 2 );
        double D2 = Math.pow( robot2.x_d - robot2.x , 2 ) + Math.pow( robot2.y_d - robot2.y , 2 );
        
        if(robot1.task_i < robot1.target.size() && robot2.task_i < robot2.target.size()){
            Station station_1 = sta.get(robot1.target.get(robot1.task_i));
            Station station_2 = sta.get(robot2.target.get(robot2.task_i));
    
            double s1_and_s2 = mapCandies[(int) station_1.x][(int) station_1.y][station_2.no];
            boolean thesame_target = false;
            //图二玄学影响
            if(s1_and_s2 < 25 | ismap3){
                thesame_target = true;
            }
            
            //加approachFlag为67w 加!scopeFlag为65w 都加为64w 不防并排走为66w
    //        if (d < 10 && Math.abs(eAngle) >= 0 && Math.abs(eAngle) <= 0.166 * Math.PI && approachFlag && !scopeFlag){
            if (d < 10 && Math.abs(eAngle) >= 0 && Math.abs(eAngle) <= 0.166 * Math.PI && approachFlag && thesame_target){
                if (D1 >= D2){
                    robot1.setV_r(2);
                }else {
                    robot2.setV_r(2);
                }
            }
        }
        return lastd;
    }


    public static void calculate(Robot robot_now, int robotId, int index1, int index2, int index3) {
        // 斥力1的大小
        d1 = collDistance(robotId,index1);
        if(d1 < dd){
            fRepulsion_1 = Alpha * ((dd - d1) / dd);
        }else{
            fRepulsion_1 = 0;
        }
        // 斥力1的方向
        fRepulsionDir_1 = -1 * Math.atan2(rob.get(index1).y - robot_now.y, rob.get(index1).x - robot_now.x);


        // 斥力2的大小
        d2 = collDistance(robotId,index2);
        if(d2 < dd){
            fRepulsion_2 = Alpha * ((dd - d2) / dd);
        }else{
            fRepulsion_2 = 0;
        }
        // 斥力2的方向
        fRepulsionDir_2 = -1 * Math.atan2(rob.get(index2).y - robot_now.y, rob.get(index2).x - robot_now.x);


        // 斥力3的大小
        d3 = collDistance(robotId,index3);
        if(d3 < dd){
            fRepulsion_3 = Alpha * ((dd - d3) / dd);
        }else{
            fRepulsion_3 = 0;
        }
        // 斥力3的方向
        fRepulsionDir_3 = -1 * Math.atan2(rob.get(index3).y - robot_now.y, rob.get(index3).x - robot_now.x);


    }

    //检测四个机器人是否相撞
    public static boolean collisionDetection(Robot robotA , Robot robotB){
        double Xc = (robotB.b - robotA.b) / (robotA.K - robotB.K);
        double Yc = (robotB.K * robotA.b - robotA.K * robotB.b) / (robotB.K - robotA.K);
        double Ta = (Xc - robotA.x0) / robotA.Kx;
        double Tb = (Xc - robotB.x0) / robotB.Kx;
        if (Ta < 0 || Tb < 0) {
            return true;
        }
        //todo 这个100是要调的参数
//        if (Math.abs(Ta - Tb) < 50 && Ta < robotA.startFrame + robotA.totalFrame  && robotA.startFrame < Ta && Tb < robotB.startFrame + robotB.totalFrame  && robotB.startFrame < Tb){
        if (Math.abs(robotA.startFrame + Ta - Tb - robotB.startFrame ) < 50 && Ta <  robotA.totalFrame  && Tb <  robotB.totalFrame  ){
//        if (Math.abs( Ta - Tb ) < 50 && Ta <  robotA.totalFrame  && Tb <  robotB.totalFrame  ){
//        if (Math.abs(Ta - Tb) < 50 ){
            
//            if(robotA.startFrame +Ta >= robotB.startFrame +Tb){
//                robotA.v_d = robotA.getV_d() + (-1.0 * 50)/Ta;
//            }else if (robotA.startFrame +Ta < robotB.startFrame +Tb){
//                robotB.v_d = robotB.getV_d() + (-1.0 * 50)/Tb;
//            }

//            if(Ta >= Tb){
//                robotA.v_d = robotA.getV_d() + (-1.0 * 50)/Ta;
//            }else if (Ta < Tb){
//                robotB.v_d = robotB.getV_d() + (-1.0 * 50)/Tb;
//            }
            
//            if(robotA.collpriority >= robotB.collpriority ){
//                robotB.v_d = robotB.getV_d() + (-1 * 50)/Tb;
//
//            }else{
//                robotA.v_d = robotA.getV_d() + (-1 * 50)/Ta;
//            }
//
            
            
            
            return false;
            
            
            
        }
        return true;
    }

    //人工势场法碰撞避免算法之距离检测
    public static double collDistance(int now, int index){
        return Math.sqrt( Math.pow( rob.get(now).y - rob.get(index).y , 2) + Math.pow(rob.get(now).x - rob.get(index).x , 2 ) );
    }
    
    public static void quickfind(Robot robot , int station_no){
        //逻辑为：  当前站点的生产时间还有一点点，或者 已经生产了，
        if( sta.get(station_no).getProduct_time() <=300 && sta.get(station_no).getProduct_time() >=0 || sta.get(station_no).isHavegenerated()  ){
            //则找每个7号站点，是否需要这个物品，并且这个站点的isflag = true， 则倒序添加
            for(Station station : stalist_7_new){
                if(station.queryDemand().contains( sta.get(station_no).types) && station.isFlag()  ){
                    robot.target.add(robot.task_i, station.no);
                    robot.target.add(robot.task_i,station_no);
                    
                }
            }
        }
    }

    //剩余时间 传入参数int remain_frame
    public static void findnextStation4(Robot robot_now , int remain_frame){
        Deque<Integer> queue = new ArrayDeque<>();
        //并行方案1： 456-7-8 或 7-8
        for(Station station7 : stalist_7_new){
            int come_i_to_7 = mapFrames[(int)robot_now.x][(int) robot_now.y][station7.no];
            int come_7_to_8 = reqFrames[station7.no][station7.distance4.get(0).no];
            List<Integer> demand = station7.queryDemand();
            
            if( (station7.isHavegenerated() | station7.product_time >=0  )&& station7.flag == false && ( Math.max(come_i_to_7,station7.product_time) + come_7_to_8  < remain_frame )   ){
                queue.addLast(station7.no);
                queue.addLast(station7.distance4.get(0).no);//这里添加的是8的编号
                if(demand.size() >=1 ){
                    for(int i = demand.size()-1 ; i >=0 ; i--){
                        List<Station> station_456_list = new ArrayList<>();
                        int type = -1;
                        if( demand.get(i) == 6  ){
                            station_456_list = station7.distance3;
                            type = 2;
                        }
                        if( demand.get(i) == 5 ){
                            station_456_list = station7.distance2;
                            type = 1;
                        }
                        if( demand.get(i) == 4 ){
                            station_456_list = station7.distance1;
                            type = 0;
                        }

                        for(Station station_456_now : station_456_list){
                            int come_i_to_456 = mapFrames[(int) robot_now.x][(int) robot_now.y][ station_456_now.no ];
                            int come_456_to_7 = reqFrames[station_456_now.no ][ station7.no];
                            if( ( station_456_now.isHavegenerated() | (station_456_now.product_time < come_i_to_456 && station_456_now.product_time >=0)) && station_456_now.flag == false){
                                if( come_i_to_456 + come_456_to_7 + come_7_to_8 > remain_frame  ){
                                    while(!queue.isEmpty()){
                                        robot_now.target.add(queue.pollFirst());
                                    }
                                    robot_now.isbusy = true;
                                    station7.setFlag(true);
                                    return ;
                                }else{
                                    queue.addFirst(station7.no);
                                    queue.addFirst(station_456_now.no);
                                    while(!queue.isEmpty()){
                                        robot_now.target.add(queue.pollFirst());
                                    }
                                    robot_now.isbusy = true;
                                    station7.setFlag(true);
                                    station_456_now.setFlag(true);
                                    station7.rob_come_cnt[type] = 1;
                                    return;
                                }
                            }else{
                                while(!queue.isEmpty()){
                                    robot_now.target.add(queue.pollFirst());
                                }
                                robot_now.isbusy = true;
                                station7.setFlag(true);
                                return ;
                            }
                        }
                    }
                }else{
                    while(!queue.isEmpty()){
                        robot_now.target.add(queue.pollFirst());
                    }
                    robot_now.isbusy = true;
                    station7.setFlag(true);
                    return ;
                }
            }
            
            //方案：456-7;
//                if(demand.size() >=1 || (demand.size()== 0 && station7.product_time < remain_frame && !(station7.isHavegenerated() |station7.isFlag()) )){
                if(demand.size() >=1 || (demand.size()== 0 && station7.product_time < remain_frame  )){
                    
                    for(int i = demand.size()-1 ; i >= 0  ; i--){
                        int type = demand.get(i);
                        List<Station> station_456_list = new ArrayList<>();
                        int set = -1;
                        if(type == 6){
                            station_456_list = station7.distance3;
                            set = 2;
                        }
                        if(type == 5){
                            station_456_list = station7.distance2;
                            set = 1;
                        }
                        if(type == 4){
                            station_456_list = station7.distance1;
                            set = 0;
                        }
                        for(Station station_456_now : station_456_list){
                            int come_i_to_456 = mapFrames[(int) robot_now.x][(int) robot_now.y][ station_456_now.no];
                            int come_456_to_7 = reqFrames[station_456_now.no][station7.no];
                            if( (station_456_now.isHavegenerated() | (station_456_now.product_time < come_i_to_456 && station_456_now.product_time >=0)) && station_456_now.isFlag() == false ){
                                if(come_i_to_456 + come_456_to_7 < remain_frame ){
                                    robot_now.isbusy = true;
                                    station_456_now.setFlag(true);
                                    station7.rob_come_cnt[set] = 1;
                                    robot_now.target.add(station_456_now.no);
                                    robot_now.target.add(station7.no);
                                    return;
                                }

                            }

                        }

                    }

                }
                
        }
        //同源方法：456-9
        for(Station station9 : stalist_9_new){
            List<Integer> demand = station9.queryDemand();
            for(int i : demand){
                List<Station> station_456_list = new ArrayList<>();
                if(i == 4){
                    station_456_list = station9.distance1;
                }
                if(i == 5){
                    station_456_list = station9.distance2;
                }
                if(i == 6){
                    station_456_list = station9.distance3;
                }
                for (Station station_456_now : station_456_list ){
                    int come_i_to_456 = mapFrames[(int) robot_now.x][(int) robot_now.y][station9.no];
                    int come_456_to_9 = reqFrames[station_456_now.no][station9.no];
                    if ((station_456_now.isHavegenerated() | (station_456_now.product_time < come_i_to_456 && station_456_now.product_time >=0)) && station_456_now.flag == false){
                        if( come_i_to_456 + come_456_to_9 > remain_frame){
                            continue;
                        }else{
                            robot_now.target.add(station_456_now.no);
                            robot_now.target.add(station9.no);
                            station_456_now.setFlag(true);
                            robot_now.isbusy = true;
                            return;
                        }
                    }
                }
                
            }
        }
        
//        //并行方案：2 找123-456
        int minFrames = 10000;
        int station_456_no = -1;
        int station_123_no = -1;
        int come_set = -1;
        for ( int i = 0 ; i < 3 ; i++ ){
            int station_no = mapCandies123[(int) robot_now.x][(int) robot_now.y][i];
            int distance_i_to_123 = mapCandies123[(int) robot_now.x][(int) robot_now.y][i+3];
            int frame_i_to_123 = (int) (Math.sqrt(distance_i_to_123) / 3);
            
            List<Station> stationlist_456_1 = sta.get(station_no).distance1;
            for (Station station_456 : stationlist_456_1){
                if(station_456.queryDemand().contains(sta.get(station_no).types)){
                    int set  = -1;
                    if(i == 0 ){
                        set = 0;
                    }
                    if(i == 1 ){
                        set = 1;
                    }
                    if(i == 2 ){
                        set = 1;
                    }
                    if (reqFrames[station_no][station_456.no] + frame_i_to_123 < remain_frame && station_456.rob_come_cnt[set] < 1){
                        if(reqFrames[station_no][station_456.no] + frame_i_to_123 < minFrames ){
                            minFrames = reqFrames[station_no][station_456.no] + frame_i_to_123;
                            station_456_no = station_456.no;
                            station_123_no = station_no;
                            come_set = set;
                        }
//                        robot_now.target.add(station_no);
//                        robot_now.target.add(station_456.no);
//                        station_456.rob_come_cnt[set] = 1;
//                        robot_now.isbusy = true;
//                        return;
                    }
                }
            }
            List<Station> stationlist_456_2 = sta.get(station_no).distance2;
            for(Station station_456 : stationlist_456_2){
                if(station_456.queryDemand().contains(sta.get(station_no).types)){
                    int set  = -1;
                    if(i == 0 ){
                        set = 0;
                    }
                    if(i == 1 ){
                        set = 0;
                    }
                    if(i == 2 ){
                        set = 1;
                    }
                    if (reqFrames[station_no][station_456.no] + frame_i_to_123 < remain_frame && station_456.rob_come_cnt[set] < 1){
                            if(reqFrames[station_no][station_456.no] + frame_i_to_123 < minFrames ){
                                minFrames = reqFrames[station_no][station_456.no] + frame_i_to_123;
                                station_456_no = station_456.no;
                                station_123_no = station_no;
                                come_set = set;
                            }
//                        robot_now.target.add(station_no);
//                        robot_now.target.add(station_456.no);
//                        station_456.rob_come_cnt[set] = 1;
//                        robot_now.isbusy = true;
//                        return;
                    }
                }
                
            }
        }
        if(station_456_no != -1 && station_123_no != -1 && come_set != -1 ){
            robot_now.target.add(station_123_no);
            robot_now.target.add(station_456_no);
            sta.get(station_456_no).rob_come_cnt[come_set] = 1;
            robot_now.isbusy = true;
            return;
        }


    }

    public static void findnextStation1(Robot robot , int robotid){
        
            //寻找哪个站点需要商品
            switch (robotid) {
                case 0:
                    if(robot.searchStation1(stalist_7_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_4_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_5_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_6_new)){
                        break;
                    }
                    break;
                case 1:
                    if(robot.searchStation1(stalist_7_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_5_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_4_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_6_new)){
                        break;
                    }
                    break;
                case 2:
                    if(robot.searchStation1(stalist_7_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_6_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_4_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_5_new)){
                        break;
                    }
                    break;
                case 3:
                    if(robot.searchStation1(stalist_8_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_7_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_4_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_5_new)){
                        break;
                    }
                    if(robot.searchStation1(stalist_6_new)){
                        break;
                    }
                
                default:
                    break;
            }
        
    }
//    public static boolean findDiguiStation(Robot robot, int robotId) throws IOException {
//        if(stalist_8.size() ==0 && stalist_7.size() ==0){
//           return false;
//        }
//        //执行这个语句，如果执行成功，返回true，否则返回false;
//        if(robot.searchStation3(stalist_8,stalist_7,stalist_456)){
//            return true;
//        }else{
//            return false;
//        }
//
//
//    }
    
    //todo 方法没用到，没有改stalist_new
    public static boolean findnextStation2(Robot robot , int robotId ,int[] global_456){
            Deque<Integer> deque_temp = new ArrayDeque<>();
            if(stalist_7.size() == 0 && stalist_8.size() == 0  ){
                return robot.searchStation2(stalist9_456,deque_temp);
            }else{
                int global4 = global456[0];
                int global5 = global456[1];
                int global6 = global456[2];
                
                
                if (global4 <= 1 && global5 <= 1 && global6 <=1 ){
                    
                    if(robot.searchStation2(stalist_654,deque_temp)){
                        robot.small_thing =  true;
                        robot.small_to_456 = robot.target.get(robot.task_i + 1);

                        return true;
                    }
                    return false;
                }
                int min = (global4 < global5 ? global4 : global5) < global6 ? (global4 < global5 ? global4 : global5) : global6;

                
                if(min == global6){
                    if(robot.searchStation2(stalist_6,deque_temp)){
                        robot.small_thing = true;
                        robot.small_to_456 = robot.target.get(robot.task_i + 1);
                        

                        return true;
                    }
                }
                if(min == global5){
                    if(robot.searchStation2(stalist_5,deque_temp)){
                        robot.small_thing = true;
                        robot.small_to_456 = robot.target.get(robot.task_i + 1);

                        return true;
                    }
                    
                }
                if(min == global4){
                    if(robot.searchStation2(stalist_4,deque_temp)){
                        robot.small_thing = true;
                        robot.small_to_456 = robot.target.get(robot.task_i + 1);

                        return true;
                    }
                    
                }
                
                
                
                
                return false;
                
            }
//        robot.searchStation2(stalist_456);

    }


    public static void findnextStation3(Robot robot , int robotId ,int[] global_456){
        Deque<Integer> deque_temp = new ArrayDeque<>();
        if( ismap1  ){
            int global4 = global456[0];
            int global5 = global456[1];
            int global6 = global456[2];
            int min = (global4 < global5 ? global4 : global5) < global6 ? (global4 < global5 ? global4 : global5) : global6;
            //searchsation1_new 的效果就是 找4、5、6其中去填1个1或者2的操作
            if(min == global4){
                if(robot.searchStation1_new(stalist_4_new)){
                    return;
                }
            }
            if(min == global5 ){
                if(robot.searchStation1_new(stalist_5_new)){
                    return;
                }
            }
            if(min == global6 ){
                if(robot.searchStation1_new(stalist_6_new)){
                    return;
                }
            }

            robot.searchStation1_new(stalist_654_new);
            return ;
        }
        
        if(ismap2){
            robot.searchStation2(stalist_456,deque_temp);
        }

        if(ismap3){
            //to do 下面的函数，当前robot去总距离：车->1->4->2->4->9最近的站点
            int[] ans =robot.findShortestStation456();
            List<Station> list = new ArrayList<>();
            list.add(sta.get(ans[1]));
            robot.searchStation2_new(list);
//            robot.target.add(ans[0]);
//            robot.target.add(ans[1]);
//            robot.target.add(ans[2]);
//            robot.target.add(ans[1]);
//            sta.get(ans[1]).rob_come_cnt[0] = 1;
//            sta.get(ans[1]).rob_come_cnt[1] = 1;
//            robot.isbusy = true;
            
            
            //稳定85W
//            robot.searchStation2_new(stalist_654);
            
        }


        if(ismap4){
            
            //开局执行一次
            if( cnt < 2){
                if(robot.searchStation1_new(stalist_4)){
                    cnt++;
                    return ;
                }
            }




//            int global4 = global456[0];
            int global5 = global456[1];
            int global6 = global456[2];
//            int min = (global4 < global5 ? global4 : global5) < global6 ? (global4 < global5 ? global4 : global5) : global6;
    
            
            
//            robot.searchStation1_new_puls(stalist_456);
            if (robot.searchStation1_new_puls(stalist_456,robotId)){
//                try {
//                    myutil.outStr(robotId + "号机器人找了：");
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
            return ;

            
                
            
            
            
            //robot.searchStation1_new(stalist_654);
            
//            return;
            
            
            


        }
    }

    public static boolean map2(int frameId,Robot robot,int robotId){
        if (frameId < 50){
            if (robot.searchStation2_map2(stalist_4_new)){
                return true;
            }
            if (robot.searchStation2_map2(stalist_6_new)){
                return true;
            }
            if (robot.searchStation2_map2(stalist_5_new)){
                return true;
            }
        }else {
            //功能，7有产品，1->4->7->8
            if (robot.searchStation3_map2(stalist_7_new)) {
                return true;
            }
            //只有1个 1 ->  4/5/6 -> 7
            if (robot.searchStation1_map2(stalist_7_new)) {
                return true;
            }
            //找123->456
            if (robot.searchStation2_new(stalist_456_new)) {
                return true;
            }
        }
        return false;
    }



    
    
    public static void exchange(Robot robotA , Robot robotB , int [][][] map_dis){
        
        int dis  = map_dis[(int)robotA.x][(int) robotA.y][robotA.target.get(robotA.task_i)]
                + map_dis[(int)robotB.x][(int) robotB.y][robotB.target.get(robotB.task_i)] - 30;
        
        int exchangedis = map_dis[(int)robotA.x][(int) robotA.y][ robotB.target.get(robotB.task_i) ]
                + map_dis[(int)robotB.x][(int) robotB.y][robotA.target.get(robotA.task_i)] ;
    
        
        if(dis > exchangedis){
            //进行交换
            List<Integer>  tempA = robotA.target;
            int tempa = robotA.task_i;
            robotA.target = robotB.target;
            robotA.task_i = robotB.task_i;
    
            robotB.target = tempA;
            robotB.task_i = tempa;
            robotA.exchangeFlag = false;
            robotB.exchangeFlag = false;
            
            
//            if((robotA.small_thing | robotB.small_thing) && robotA.carrygood == 0 && robotB.carrygood == 0 ){
//
//                if(robotA.small_thing && robotB.small_thing && robotA.target.size() -4 == robotA.task_i  && robotB.target.size() -4== robotB.task_i ){
//                    sta.get(robotA.small_to_456).rob_come_cnt[0] = 0;
//                    sta.get(robotA.small_to_456).rob_come_cnt[1] = 0;
//                    sta.get(robotA.small_to_456).state += 1;
//
//                    sta.get(robotB.small_to_456).rob_come_cnt[0] = 0;
//                    sta.get(robotB.small_to_456).rob_come_cnt[1] = 0;
//                    sta.get(robotB.small_to_456).state += 1;
//
//                    int robotA_types = sta.get(robotA.small_to_456).types;
//                    int robotB_types = sta.get(robotB.small_to_456).types;
//
//                    robotA.task_i = robotA.target.size();
//                    robotB.task_i = robotB.target.size();
//
//                    switch (robotA_types){
//                        case 4:
//                            robotB.searchStation2(stalist_4_new);
//                            break;
//                        case 5:
//                            robotB.searchStation2(stalist_5_new);
//                            break;
//                        case 6:
//                            robotB.searchStation2(stalist_6_new);
//                            break;
//                        default:
//                            break;
//                    }
//                    switch (robotB_types){
//                        case 4:
//                            robotA.searchStation2(stalist_4);
//                            break;
//                        case 5:
//                            robotA.searchStation2(stalist_5);
//                            break;
//                        case 6:
//                            robotA.searchStation2(stalist_6);
//                            break;
//                        default:
//                            break;
//                    }
//                    robotA.small_thing =false;
//                    robotB.small_thing =false;
//
//                }
//
//                if(robotA.small_thing && robotB.small_thing == false && robotB.target.size() -4 == robotB.task_i  ){
//
//                    sta.get(robotA.small_to_456).rob_come_cnt[0] = 0;
//                    sta.get(robotA.small_to_456).rob_come_cnt[1] = 0;
//                    sta.get(robotA.small_to_456).state += 1;
//                    switch (sta.get(robotA.small_to_456).types){
//                        case 4:
//                            robotB.task_i = robotB.target.size();
//                            robotB.searchStation2(stalist_4);
//                            break;
//                        case 5:
//                            robotB.task_i = robotB.target.size();
//                            robotB.searchStation2(stalist_5);
//                            break;
//                        case 6:
//                            robotB.task_i = robotB.target.size();
//                            robotB.searchStation2(stalist_6);
//                            break;
//                        default:
//                            break;
//                    }
//                    robotA.small_thing = false;
//
//                }
//
//
//                if(robotB.small_thing && robotA.small_thing ==false  && robotA.target.size() -4 == robotA.task_i){
//
//                    sta.get(robotB.small_to_456).rob_come_cnt[0] = 0;
//                    sta.get(robotB.small_to_456).rob_come_cnt[1] = 0;
//                    sta.get(robotB.small_to_456).state += 1;
//                    switch (sta.get(robotB.small_to_456).types){
//                        case 4:
//                            robotA.task_i = robotA.target.size();
//                            robotA.searchStation2(stalist_4);
//                            break;
//                        case 5:
//                            robotA.task_i = robotA.target.size();
//                            robotA.searchStation2(stalist_5);
//                            break;
//                        case 6:
//                            robotA.task_i = robotA.target.size();
//                            robotA.searchStation2(stalist_6);
//                            break;
//                        default:
//                            break;
//                    }
//                    robotB.small_thing = false;
//
//                }
//
//
//            }else{
//                Boolean temp = robotA.small_thing;
//                robotA.small_thing = robotB.small_thing;
//                robotB.small_thing = temp;
//            }
//
            
            
            
            
            
        }
        
    }
    //robotA是有任务的点车， robotB为没任务的点
    public static void exchange_2(Robot robotA,Robot robotB , int [][][] map_dis){
        int dis_A = map_dis[(int)robotA.x][(int) robotA.y][robotA.target.get(robotA.task_i)] - 30;
    
        int dis_B = map_dis[(int)robotB.x][(int) robotB.y][ robotA.target.get(robotA.task_i) ] ;
        
        
        if(dis_A > dis_B ){
            //进行交换
            List<Integer>  tempA = robotA.target;
            int tempa = robotA.task_i;
            robotA.target = robotB.target;
            robotA.task_i = robotB.task_i;
    
            robotB.target = tempA;
            robotB.task_i = tempa;
            robotA.exchangeFlag = false;
            robotB.exchangeFlag = false;
            robotA.isbusy = false;
            robotB.isbusy = true;
            
        }
        
    }
    

    private static boolean readUtilOK() {
        String line;
        int x = 0, y = 99, no = 0;
        Myutil myutil = new Myutil();
        while (inStream.hasNextLine()) {
            line = inStream.nextLine();
            if ("OK".equals(line)) {
                return true;
            }
            // do something;
            while(x < 100){
                if(line.charAt(x) != '.'){
                    if(line.charAt(x) != 'A'){
                        double x_f = (x * 0.5 + 0.25);
                        double y_f = (y * 0.5 + 0.25);
                        double xy = ( x_f * 10000 + y_f);     // 坐标拼接作为，哈希表的key，后面添加
                        // 求工作站距离墙壁的最小值；
                        mindistance = Math.min(mindistance, myutil.myMin(x_f, y_f, (50 - x_f), (50 - y_f)));
                        int type = line.charAt(x) - '0';
                        Station station = StationFactory.creatStation(no, type ,x_f ,y_f);

                        sta.add(station);

                        switch (type)
                        {
                            case 1:
                                stalist_1.add(station); break;
                            case 2:
                                stalist_2.add(station); break;
                            case 3:
                                stalist_3.add(station); break;
                            case 4:
                                stalist_4.add(station); break;
                            case 5:
                                stalist_5.add(station); break;
                            case 6:
                                stalist_6.add(station); break;
                            case 7:
                                stalist_7.add(station); break;
                            case 8:
                                stalist_8.add(station); break;
                            case 9:
                                stalist_9.add(station); break;
                            default: break;
                        }

                        map.put(xy, no);
                        no++;


                    }else{
                        double x_f = (x * 0.5 + 0.25);
                        double y_f = (y * 0.5 + 0.25);
                        double xy = ( x_f * 10000 + y_f);     // 后面添加
                        Robot robot = new Robot(x_f, y_f);
                        rob.add(robot);

                    }

                }
                x++;
            }
            x = 0;
            y--;
        }
        return false;
    }

    public static void makeReqFrames() {
        for (int i = 0; i < sta.size(); i++) {
            for (int j = 0; j < sta.size(); j++) {
//                sqrt算距离，除以速度3得到所需时间，乘以每秒帧数50
                reqFrames[i][j] = (int) (Math.sqrt(Math.pow(sta.get(i).x - sta.get(j).x, 2) + Math.pow(sta.get(i).y - sta.get(j).y, 2)) / 3 * 50);
            }
        }
    }

    public static void makeMapCandyPlus() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
//                全部初始化为-1
                for (int k = 0; k < 30; k++) {
                    mapCandie1[j][i][k] = -1;
                    mapCandie2[j][i][k] = -1;
                    mapCandie3[j][i][k] = -1;
                    mapCandie4[j][i][k] = -1;
                    mapCandie5[j][i][k] = -1;
                    mapCandie6[j][i][k] = -1;
                    mapCandie7[j][i][k] = -1;
                    mapCandie8[j][i][k] = -1;
                    mapCandie9[j][i][k] = -1;
                }

                sortBaseType(i, j, mapCandie1, stalist_1);
                sortBaseType(i, j, mapCandie2, stalist_2);
                sortBaseType(i, j, mapCandie3, stalist_3);
                sortBaseType(i, j, mapCandie4, stalist_4);
                sortBaseType(i, j, mapCandie5, stalist_5);
                sortBaseType(i, j, mapCandie6, stalist_6);
                sortBaseType(i, j, mapCandie7, stalist_7);
                sortBaseType(i, j, mapCandie8, stalist_8);
                sortBaseType(i, j, mapCandie9, stalist_9);



            }
        }
    }



    public static void sortBaseType(int i, int j, int[][][]mapCandies, List<Station> lists){
        Collections.sort(lists, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                double sum1 = (Math.pow((o1.x - j), 2) + Math.pow((o1.y - i), 2));
                double sum2 = (Math.pow((o2.x - j), 2) + Math.pow((o2.y - i), 2));
                if (sum1 > sum2) {
                    return 1;
                } else if (sum1 < sum2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for (int l = 0; l < lists.size(); l++) {
            mapCandies[j][i][l] = lists.get(l).getNo();
        }

    }

    public static void makeMapCandy() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 0; k < sta.size(); k++) {
//                    存储当机器人处在x，y坐标时距离各工作站的距离
                    mapCandies[j][i][k] = (int) (Math.pow(sta.get(k).x - j, 2) + Math.pow(sta.get(k).y - i, 2));
                    mapFrames[j][i][k] = (int) (Math.sqrt( mapCandies[j][i][k] ) / 3 * 50);
                }
            }
        }
    }

    public static void makeMapCandy123() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 3; k < 6; k++) {
                    mapCandies123[j][i][k] = 5000;
                }

                for (Station sta1:
                        stalist_1) {
                    int temp = (int) (Math.pow(sta1.x - j, 2) + Math.pow(sta1.y - i, 2));
                    if (mapCandies123[j][i][3] > temp){
                        mapCandies123[j][i][3] = temp;
                        mapCandies123[j][i][0] = sta1.getNo();
                    }
                }

                for (Station sta2:
                        stalist_2) {
                    int temp = (int) (Math.pow(sta2.x - j, 2) + Math.pow(sta2.y - i, 2));
                    if (mapCandies123[j][i][4] > temp){
                        mapCandies123[j][i][4] = temp;
                        mapCandies123[j][i][1] = sta2.getNo();
                    }
                }

                for (Station sta3:
                        stalist_3) {
                    int temp = (int) (Math.pow(sta3.x - j, 2) + Math.pow(sta3.y - i, 2));
                    if (mapCandies123[j][i][5] > temp){
                        mapCandies123[j][i][5] = temp;
                        mapCandies123[j][i][2] = sta3.getNo();
                    }
                }


            }
        }
    }

    public static void makeMapCandy456() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                for (int k = 3; k < 6; k++) {
                    mapCandies456[j][i][k] = 5000;
                }

                for (Station sta4:
                     stalist_4) {
                    int temp = (int) (Math.pow(sta4.x - j, 2) + Math.pow(sta4.y - i, 2));
                    if (mapCandies456[j][i][3] > temp){
                        mapCandies456[j][i][3] = temp;
                        mapCandies456[j][i][0] = sta4.getNo();
                    }
                }

                for (Station sta5:
                        stalist_5) {
                    int temp = (int) (Math.pow(sta5.x - j, 2) + Math.pow(sta5.y - i, 2));
                    if (mapCandies456[j][i][4] > temp){
                        mapCandies456[j][i][4] = temp;
                        mapCandies456[j][i][1] = sta5.getNo();
                    }
                }

                for (Station sta6:
                        stalist_6) {
                    int temp = (int) (Math.pow(sta6.x - j, 2) + Math.pow(sta6.y - i, 2));
                    if (mapCandies456[j][i][5] > temp){
                        mapCandies456[j][i][5] = temp;
                        mapCandies456[j][i][2] = sta6.getNo();
                    }
                }


            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void staSpace() {
        // 分别将129相对于4进行排序，且将129相对于4最近的距离添加到4的属性里面
        //mysort 传入参数， 第一个:  stalist_m , 后面是stalist_s 。作用为：将stalist_s的最小距离全传入 stalist_m的每个站点的每三个属性中
        if (stalist_9.size() != 0){
            mysort(stalist_4, stalist_1, stalist_2, stalist_9);
            mysort(stalist_1, stalist_4, stalist_5);
            mysort(stalist_2, stalist_4, stalist_6);
            mysort(stalist_3, stalist_5, stalist_6);
            mysort(stalist_5, stalist_1, stalist_3, stalist_9);
            mysort(stalist_6, stalist_2, stalist_3, stalist_9);
            mysort(stalist_7, stalist_4, stalist_5, stalist_6, stalist_8);
            mysort(stalist_9, stalist_4, stalist_5, stalist_6); // 456
            mysort(stalist_8, stalist_7);

        }else{
            mysort(stalist_4, stalist_1, stalist_2);
            mysort(stalist_5, stalist_1, stalist_3);
            mysort(stalist_6, stalist_2, stalist_3);
            mysort(stalist_1, stalist_4, stalist_5);
            mysort(stalist_2, stalist_4, stalist_6);
            mysort(stalist_3, stalist_5, stalist_6);
            mysort(stalist_7, stalist_4, stalist_5, stalist_6, stalist_8);
//            mysort(stalist_9, stalist_4, stalist_5, stalist_6); // 456
            mysort(stalist_8, stalist_7);

        }


        //将456分别添加到每个9的属性 distance456 里，并且对这里面的456相对于每个9进行排序（后面好像用不到）
//        mysort9(stalist_9, stalist_4, stalist_5, stalist_6);

//        // 想看什么数据，写到文件里
//        Myutil myutil = new Myutil();
//        List<Float> list = new ArrayList<>();
//        for (Station sta:
//                stalist_7.get(0).distance1) {
//                list.add((float) sta.no);
//        }
//        try {
//            myutil.output(list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static void mysort9(List<Station> stalist_9, List<Station> stalist_4, List<Station> stalist_5, List<Station> stalist_6){
        int len4 = stalist_4.size();
        int len5 = stalist_5.size();
        int len6 = stalist_6.size();
        int max = (len4 > len5 ? len4 : len5) > len6 ? (len4 > len5 ? len4 : len5) : len6;
        List<Station> temp456 = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            if(i < len4) {
                temp456.add(stalist_4.get(i));
            }
            if(i < len5){
                temp456.add(stalist_5.get(i));
            }
            if(i < len6){
                temp456.add(stalist_6.get(i));
            }
        }

        for (Station stationm : stalist_9) {
            for (int i = 0; i < temp456.size(); i++) {
                stationm.distance456.add(temp456.get(i));
            }

            Collections.sort(stationm.distance456, new Comparator<Station>() {
                @Override
                public int compare(Station o1, Station o2) {
                    double sum1 = o1.dis1 + o1.dis2 + (Math.pow((o1.x - stationm.x), 2) + Math.pow((o1.y - stationm.y), 2));
                    double sum2 = o2.dis1 + o2.dis2 + (Math.pow((o2.x - stationm.x), 2) + Math.pow((o2.y - stationm.y), 2));
                    if (sum1 > sum2) {
                        return 1;
                    } else if (sum1 < sum2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

        }

    }

    @SuppressWarnings("unchecked")
    public static void mysort(List<Station> stalist_m , List<Station>...stalist){
        int len = stalist.length;
//        int len = 3;
        // 添加工作台4和原材料工作台之间的距离的平方
        for (Station stationm : stalist_m) {
            for (int i = 0; i < len; i++) {
                switch (i)
                {
                    case 0:
                        mysorthaha(stationm, stationm.distance1, stalist[0]);
                        stationm.dis1 = Math.pow(stationm.distance1.get(0).x - stationm.x, 2) + Math.pow(stationm.distance1.get(0).y - stationm.y, 2);
                        break;
                    case 1:
                        mysorthaha(stationm, stationm.distance2, stalist[1]);
                        stationm.dis2 = Math.pow(stationm.distance2.get(0).x - stationm.x, 2) + Math.pow(stationm.distance2.get(0).y - stationm.y, 2);
                        break;
                    case 2:
                        mysorthaha(stationm, stationm.distance3, stalist[2]);
                        stationm.dis3 = Math.pow(stationm.distance3.get(0).x - stationm.x, 2) + Math.pow(stationm.distance3.get(0).y - stationm.y, 2);
                        break;
                    case 3:
                        mysorthaha(stationm, stationm.distance4, stalist[3]);
                        stationm.dis4 = Math.pow(stationm.distance4.get(0).x - stationm.x, 2) + Math.pow(stationm.distance4.get(0).y - stationm.y, 2);
                        
                    default: break;
                }
            }
        }


    }

    @SuppressWarnings("unchecked")
    public static void mysorthaha(Station stationm, List<Station> distancen, List<Station> stalistn){
        // 添加对应工作台到distance中
        for (Station stations : stalistn) {
            distancen.add(stations);
        }
        // diatance中的工作台按照间距从小到大排序
        Collections.sort(distancen, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                if ((Math.pow((o1.x - stationm.x), 2) + Math.pow((o1.y - stationm.y), 2)) > (Math.pow((o2.x - stationm.x), 2) + Math.pow((o2.y - stationm.y), 2))) {
                    return 1;
                } else if ((Math.pow((o1.x - stationm.x), 2) + Math.pow((o1.y - stationm.y), 2)) < (Math.pow((o2.x - stationm.x), 2) + Math.pow((o2.y - stationm.y), 2))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

    }
    
    public static boolean collDetection(Robot rob1 , Robot rob2){
        double rob1x1 = Math.min(rob1.x,rob1.predict_x);
        double rob1y1 = Math.min(rob1.y,rob1.predict_y);
        double rob1x2 = Math.max(rob1.x,rob1.predict_x);
        double rob1y2 = Math.max(rob1.y,rob1.predict_y);
    
        double rob2x1 = Math.min(rob2.x,rob2.predict_x);
        double rob2y1 = Math.min(rob2.y,rob2.predict_y);
        double rob2x2 = Math.max(rob2.x,rob2.predict_x);
        double rob2y2 = Math.max(rob2.y,rob2.predict_y);
        
        
        
        return (Math.min(rob1x2,rob2x2) > Math.max(rob1x1,rob2x1) &&
                
                Math.min(rob1y2,rob2y2) > Math.max(rob1y1,rob2y1) );
    
    }


    public static void sumSpace() {
        // 以当前工作站相对于原材料工作站的距离的和为基准进行排序
        sumsort(stalist_4);
        sumsort(stalist_5);
        sumsort(stalist_6);
        if(stalist_9.size() != 0){
            sumsort9_456(stalist9_456);
        }

        mergesort(stalist_4, stalist_5, stalist_6, stalist_456);
        mergesort(stalist_4, stalist_6, stalist_5, stalist_465);
        mergesort(stalist_5, stalist_4, stalist_6, stalist_546);
        mergesort(stalist_5, stalist_6, stalist_4, stalist_564);
        mergesort(stalist_6, stalist_4, stalist_5, stalist_645);
        mergesort(stalist_6, stalist_5, stalist_4, stalist_654);

    }

    public static void sumsort(List<Station> stalist_m){
        Collections.sort(stalist_m, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                double sum1 = Math.pow((o1.distance1.get(0).x - o1.x), 2) + Math.pow((o1.distance1.get(0).y - o1.y), 2) + Math.pow((o1.distance2.get(0).x - o1.x), 2) + Math.pow((o1.distance2.get(0).y - o1.y), 2);
                double sum2 = Math.pow((o2.distance1.get(0).x - o2.x), 2) + Math.pow((o2.distance1.get(0).y - o2.y), 2) + Math.pow((o2.distance2.get(0).x - o2.x), 2) + Math.pow((o2.distance2.get(0).y - o2.y), 2);
                if (sum1 > sum2) {
                    return 1;
                } else if (sum1 < sum2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public static void sumsort9_456(List<Station> stalist_m){
        int len4 = stalist_4.size();
        int len5 = stalist_5.size();
        int len6 = stalist_6.size();
        int max = (len4 > len5 ? len4 : len5) > len6 ? (len4 > len5 ? len4 : len5) : len6;

        // 首先将所有的456添加到全局变量stalist9_456中
        for (int i = 0; i < max; i++) {
            if(i < len4){
                stalist9_456.add(stalist_4.get(i));
            }
            if(i < len5) {
                stalist9_456.add(stalist_5.get(i));
            }
            if(i < len6) {
                stalist9_456.add(stalist_6.get(i));
            }
        }

        // 然年对齐进行排序
        Collections.sort(stalist9_456, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                double sum1 = o1.dis1 + o1.dis2 + o1.dis3;
                double sum2 = o2.dis1 + o2.dis2 + o2.dis3;
                if (sum1 > sum2) {
                    return 1;
                } else if (sum1 < sum2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

    }

    public static void findGlobal_456( ){

        //统计场上456站点中在生产或有产品的个数
        global456 = new  int[3];
        for (Station station_4 : stalist_4){
            if (station_4.isHavegenerated() ){
                global456[0] += 1;
            }
            if (station_4.product_time >= 0){
                global456[0] += 1;
            }
            if (station_4.queryDemand().size() == 0){
                global456[0] += 1;
            }
        }
        for (Station station_5 : stalist_5){
            if (station_5.isHavegenerated()){
                global456[1] += 1;
            }
            if (station_5.product_time >= 0){
                global456[1] += 1;
            }
            if (station_5.queryDemand().size() == 0){
                global456[1] += 1;
            }
        }
        for (Station station_6 : stalist_6){
            if (station_6.isHavegenerated()){
                global456[2] += 1;
            }
            if (station_6.product_time >= 0){
                global456[2] += 1;
            }
            if (station_6.queryDemand().size() == 0){
                global456[2] += 1;
            }
        }

        //统计车上携带的456个数
        for (Robot robot : rob){
            if (robot.carrygood == 4){
                global456[0] += 1;
            }
            if (robot.carrygood == 5){
                global456[1] += 1;
            }
            if (robot.carrygood == 6){
                global456[2] += 1;
            }
        }

        //统计7站点原料456的个数
        for (Station station_7 : stalist_7){
            global456[0] = (station_7.existgood[0])?  global456[0] +1 : global456[0];
            global456[1] = (station_7.existgood[1])?  global456[1] +1 : global456[1];
            global456[2] = (station_7.existgood[2])?  global456[2] +1 : global456[2];
        }


//        global456 = new int [3];
//        for(Station station456_now : stalist_456){
//            int type = station456_now.types;
//            int set = -1;
//            if(type ==4){
//                set = 0;
//            }else if(type ==5){
//                set = 1;
//            }else if(type ==6){
//                set = 2;
//            }
//            boolean almostgenenerated = (station456_now.product_time >=0  && station456_now.product_time < 100);
//
//            if( (station456_now.isHavegenerated() && !station456_now.isFlag())  ){
//                if(almostgenenerated){
//                    global456[set] += 2;
//                }else{
//                    global456[set] += 1;
//                }
//            }else if( !station456_now.isFlag() && almostgenenerated ){
//                global456[set] += 1;
//            }else if(station456_now.isHavegenerated() && station456_now.isFlag() && almostgenenerated ){
//                global456[set] += 1;
//            }
//        }
        
    }

    public static void mergesort(List<Station> stalist_4, List<Station> stalist_5, List<Station> stalist_6,  List<Station> stalist_456) {
        int len4 = stalist_4.size();
        int len5 = stalist_5.size();
        int len6 = stalist_6.size();
        int max = (len4 > len5 ? len4 : len5) > len6 ? (len4 > len5 ? len4 : len5) : len6;


        if(len4 <= len5 && len4 <= len6 && len5 <= len6) {
            // 456
            for (int i = 0; i < max; i++) {
                if(i < len4) {
                    stalist_456.add(stalist_4.get(i));
                }
                if(i < len5) {
                    stalist_456.add(stalist_5.get(i));
                }
                if(i < len6) {
                    stalist_456.add(stalist_6.get(i));
                }
            }


        }else if(len4 <= len5 && len4 <= len6 && len6 <= len5){
            // 465
            for (int i = 0; i < max; i++) {
                if(i < len4) {
                    stalist_456.add(stalist_4.get(i));
                }
                if(i < len6) {
                    stalist_456.add(stalist_6.get(i));
                }
                if(i < len5) {
                    stalist_456.add(stalist_5.get(i));
                }
            }
        }else if(len5 <= len4 && len5 <= len6 && len4 <= len6){
            // 546
            for (int i = 0; i < max; i++) {
                if(i < len5) {
                    stalist_456.add(stalist_5.get(i));
                }
                if(i < len4) {
                    stalist_456.add(stalist_4.get(i));
                }
                if(i < len6) {
                    stalist_456.add(stalist_6.get(i));
                }
            }

        }else if(len5 <= len6 && len5 <= len4 && len6 <= len4){
            // 564
            for (int i = 0; i < max; i++) {
                if(i < len5) {
                    stalist_456.add(stalist_5.get(i));
                }
                if(i < len6) {
                    stalist_456.add(stalist_6.get(i));
                }
                if(i < len4) {
                    stalist_456.add(stalist_4.get(i));
                }
            }

        }else if(len6 <= len4 && len6 <= len5 && len4 <= len5){
            // 645
            for (int i = 0; i < max; i++) {
                if(i < len6) {
                    stalist_456.add(stalist_6.get(i));
                }
                if(i < len4) {
                    stalist_456.add(stalist_4.get(i));
                }
                if(i < len5) {
                    stalist_456.add(stalist_5.get(i));
                }
            }

        }else if (len4 <= len5 && len4 <= len6 && len5 <= len6){
            // 654
            for (int i = 0; i < max; i++) {
                if(i < len6) {
                    stalist_456.add(stalist_6.get(i));
                }
                if(i < len5) {
                    stalist_456.add(stalist_5.get(i));
                }
                if(i < len4) {
                    stalist_456.add(stalist_4.get(i));
                }
            }

        }
    }


    public static void outForceMessage(int frameID) throws IOException {
        myutil.outStr("当前帧ID：" + frameID);
        for (int i = 0; i < 4; i++) {

////            myutil.outStr(i + "号机器人的引力：" + (int)rob.get(i).yinLi  + "引力方向：" + rob.get(i).yinLiFang);
//            myutil.outStr(i + "号机器人的引力和方向：" + (int)rob.get(i).yinLi + "  " + rob.get(i).yinLiFang);
//
//
//            myutil.outStr(i + "号机器人的斥力：" + rob.get(i).chiLi1 + "  " + rob.get(i).chiLi2 + "  " + rob.get(i).chiLi3);
//            myutil.outStr(i + "号机器人的斥力方向：" + rob.get(i).chiLiFang1 + "  " + rob.get(i).chiLiFang2 + "  " + rob.get(i).chiLiFang3);
//
//
//
//            myutil.outStr(i + "号机器人的x和y轴合力：" + rob.get(i).xHeLi + "  " + rob.get(i).yHeLi);
//            myutil.outStr(i + "号机器人的合力角：" + rob.get(i).heLiJiao);
//
////            myutil.outStr(i + "号机器人的引力距离：" + rob.get(i).jiGongJu);
////            myutil.outStr(i + "号机器人的引力：" + rob.get(i).yinLi);
////            myutil.outStr(i + "号机器人的引力方向：" + rob.get(i).yinLiFang);
////
////            myutil.outStr(i + "号机器人的斥力1距离：" + rob.get(i).jiJiJu1);
////            myutil.outStr(i + "号机器人的斥力1：" + rob.get(i).chiLi1);
////            myutil.outStr(i + "号机器人的斥力1方向：" + rob.get(i).chiLiFang1);
////
////            myutil.outStr(i + "号机器人的斥力2距离：" + rob.get(i).jiJiJu2);
////            myutil.outStr(i + "号机器人的斥力2：" + rob.get(i).chiLi2);
////            myutil.outStr(i + "号机器人的斥力2方向：" + rob.get(i).chiLiFang2);
////
////            myutil.outStr(i + "号机器人的斥力3距离：" + rob.get(i).jiJiJu3);
////            myutil.outStr(i + "号机器人的斥力3：" + rob.get(i).chiLi3);
////            myutil.outStr(i + "号机器人的斥力3方向：" + rob.get(i).chiLiFang3);
////
////
////            myutil.outStr(i + "号机器人的x轴合力方向：" + rob.get(i).xHeLi);
////            myutil.outStr(i + "号机器人的y轴合力方向：" + rob.get(i).yHeLi);
////            myutil.outStr(i + "号机器人的合力角：" + rob.get(i).heLiJiao);

        }

        myutil.outStr("=======================================");
    }




    public static void outMessage(int frameID) throws IOException {
        myutil.outStr("当前帧ID：" + frameID);
        for(int j = 0 ; j < sta.size() ; j ++){
            myutil.outstr_aline( String.valueOf(sta.get(j).no) +String.valueOf(sta.get(j).isFlag())) ;
            if(sta.get(j).types<=7 && sta.get(j).types>=4){
                if(sta.get(j).rob_come_cnt[0] <0){
                    myutil.outStr("序号" + sta.get(j).no  + "0" + "出问题");
                }
                if(sta.get(j).rob_come_cnt[1] <0){
                    myutil.outStr("序号" + sta.get(j).no  + "1" + "出问题");
                }
                if(sta.get(j).rob_come_cnt[2] <0){
                    myutil.outStr("序号" + sta.get(j).no  + "2" + "出问题");
                }
            }
            
        }
        myutil.outStr(" ");
        for(int j = 0 ; j < sta.size() ; j ++){
            if(sta.get(j).types<=6 && sta.get(j).types>=4){
                myutil.outstr_aline(  String.valueOf(sta.get(j).no) + "状态"  + sta.get(j).state + "querydemand" + sta.get(j).queryDemand().toString() ) ;
            }else{
                myutil.outstr_aline( String.valueOf(sta.get(j).no)+ " ") ;
            }
            
        
        }
        myutil.outStr(" ");
        for (int i = 0; i < 4; i++) {

            myutil.outStr(i + "号机器人信息大小：" + rob.get(i).target.size());
            myutil.outStr(i + "号机器人的target：" + rob.get(i).target.toString());
            myutil.outStr( i + "号机器人的指针：" + rob.get(i).task_i);
            myutil.outStr(i + "号机器人是否忙碌：" + rob.get(i).isbusy);
            myutil.outStr(i + "号机器人的速度信息: " + rob.get(i).velocity);
            myutil.outStr(i + "号机器人距离目标点长度： " + Math.sqrt (  (rob.get(i).x_d - rob.get(i).x) *  (rob.get(i).x_d - rob.get(i).x) + (rob.get(i).y_d - rob.get(i).y)*(rob.get(i).y_d - rob.get(i).y)          ) );
            myutil.outStr(i + "号机器人x^2： " + (rob.get(i).x_d - rob.get(i).x) *  (rob.get(i).x_d - rob.get(i).x) );
            myutil.outStr(i + "号机器人y^2： " + (rob.get(i).y_d - rob.get(i).y) *  (rob.get(i).y_d - rob.get(i).y) );
            if(rob.get(i).small_thing){
                myutil.outStr(i + "号机器人在做small_thing");
            }
            
            if(rob.get(i).exchangeFlag == false){
                myutil.outStr(i + "号机器人进行了交换！！！");
            }
            
        }
        if(cnt>0){
            myutil.outStr("图4执行了刚刚我写的语句"+String.valueOf(cnt));
        }else{
            myutil.outStr("没找478");
        }
        myutil.outStr("=======================================");
    }




    public static void resave1(){

        int[] index1 = new int[]{0};
        for (int index: index1) {
            stalist_1_new.add(sta.get(index));
        }

        int[] index2 = new int[]{41};
        for (int index: index2) {
            stalist_2_new.add(sta.get(index));
        }

        int[] index3 = new int[]{42};
        for (int index: index3) {
            stalist_3_new.add(sta.get(index));
        }

        int[] index4 = new int[]{20,26};
        for (int index: index4) {
            stalist_4_new.add(sta.get(index));
        }

        int[] index5 = new int[]{8,13};
        for (int index: index5) {
            stalist_5_new.add(sta.get(index));
        }

        int[] index6 = new int[]{38,39};
        for (int index: index6) {
            stalist_6_new.add(sta.get(index));
        }

        int[] index7 = new int[]{10,12,21,23};
        for (int index: index7) {
            stalist_7_new.add(sta.get(index));
        }

        int[] index8 = new int[]{};
        for (int index: index8) {
            stalist_8_new.add(sta.get(index));
        }

        int[] index9 = new int[]{18,30};
        for (int index: index9) {
            stalist_9_new.add(sta.get(index));
        }

    }

    public static void resave2(){

        int[] index5 = new int[]{1,23};
        for (int index: index5) {
            stalist_5_new.add(sta.get(index));
        }

        int[] index6 = new int[]{0,24};
        for (int index: index6) {
            stalist_6_new.add(sta.get(index));
        }

        stalist_1_new.addAll(stalist_1);
        stalist_2_new.addAll(stalist_2);
        stalist_3_new.addAll(stalist_3);
        stalist_4_new.addAll(stalist_4);
//        stalist_5_new.addAll(stalist_5);
//        stalist_6_new.addAll(stalist_6);
        stalist_7_new.addAll(stalist_7);
        stalist_8_new.addAll(stalist_8);
        stalist_9_new.addAll(stalist_9);

    }

    public static void resave3(){


        stalist_1_new.addAll(stalist_1);
        stalist_2_new.addAll(stalist_2);
        stalist_3_new.addAll(stalist_3);
//        stalist_4_new.addAll(stalist_4);
        stalist_5_new.addAll(stalist_5);
        stalist_6_new.addAll(stalist_6);
        stalist_7_new.addAll(stalist_7);
        stalist_8_new.addAll(stalist_8);
        stalist_9_new.addAll(stalist_9);

    }

    public static void resave4(){



        stalist_1_new.addAll(stalist_1);
        stalist_2_new.addAll(stalist_2);
        stalist_3_new.addAll(stalist_3);
        stalist_4_new.addAll(stalist_4);
        stalist_5_new.addAll(stalist_5);
        stalist_6_new.addAll(stalist_6);
        stalist_7_new.addAll(stalist_7);
        stalist_8_new.addAll(stalist_8);
        stalist_9_new.addAll(stalist_9);



    }


}


