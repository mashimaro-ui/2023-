package com.huawei.codecraft;


import javax.swing.plaf.basic.BasicBorders;
import java.io.IOException;
import java.util.*;

import static com.huawei.codecraft.Main.sta;
import static com.huawei.codecraft.Main.*;

/**
 * @author xyhong
 * @create 2023-03-10-10:53
 */
public class Robot {

    //路径参数方程
    public double Kx; //参数方程x中的斜率
    public double Ky; //参数方程y中的斜率
    public int startFrame;//初始帧
    public int totalFrame;//总用时帧
    public double K;//路径参数中的斜率
    public double b;//路径参数中的常数
    public double x0;//起点x坐标
    public double y0;//起点y坐标



//    人工势场参数
    public double jiGongJu;
    public double yinLi;
    public double yinLiFang;


    public double jiJiJu1;
    public double jiJiJu2;
    public double jiJiJu3;
    public double chiLi1;
    public double chiLi2;
    public double chiLi3;
    public double chiLiFang1;
    public double chiLiFang2;
    public double chiLiFang3;

    public double xHeLi;
    public double yHeLi;
    public double heLiJiao;


    public double x,y;
    //携带物品类型，0为没带物品，1-7对应物品
    public int carrygood = 0 ;
    //当前处于哪个工作台附近，-1没有工作台在附近，0-（工作台总数-1），nowloaction为序号数。
    public int nowloaction ;
    public int lastFrame = 0;
    public int lastloaction = 0;

    //时间系数,碰撞系数;
    public double timevalue ;
    public double collvalue ;
    //角速度
    public double anglevelocity;
    //线速度
    public double x_velocity,y_velocity;
    public double velocity;
    //朝向
    public double direction ;
    
    public double v_r;
    public double phi_r;
    
    public boolean exchangeFlag = true;
    
    public List<Integer> target = new ArrayList<>();
    public int task_i = 0 ;
    
    public boolean isbusy = false;
    //碰撞优先级，优先级
    public double collpriority;
    //持有物品的价值表                     0   1     2    3    4     5     6      7
    public int[] goodvalue = new int[]{100,6000,7600,9200,22500,25000,27500,105000};

    
    //测试用例来查询撞了多少次
    public int test_colltime ;
    
    //这个用来存 机器人去 to_with[0] 的序号站点 ， 带有to_whith[1] 类型的货物
    public int[] to_with = new int [2];

    //初始值直接设置
    //新的属性，目的地的速度与角度
    public double predictangle;
    public double predict_x;
    public double predict_y;

    public double v_d = 6;
    public double angle_d= 0;
    public double x_d = 0;
    public double y_d = 0;
    
    //倒数帧，先执行500帧的转向算法，后执行pd控制
    public int cnt_frame = 500;
    public double last_e3 = 0;
    public double para_p = 0.1;
    public double para_d = 0.001;

    
    
    public boolean collalert=false;
    //用于交换任务后，重定位123——> 456 的最短路径。
    public boolean small_thing = false;
    public int small_to_456 = -1;


    public void equationCalculate(int nowFrame){
        getDestinatiuon(sta);
        totalFrame = (int) (Math.sqrt(Math.pow( x_d - x , 2 ) + Math.pow( y_d - y , 2 )) / this.v_d * 50);
        Kx = (x_d - x) / totalFrame;
        Ky = (y_d - y) / totalFrame;
        K = Ky / Kx;
        b = y - K * x;
        startFrame = nowFrame;
        x0 = x ;
        y0 = y ;

    }


    
    
    public void calculate(List<Station> sta , int nowFrame){
        getDestinatiuon(sta);
        //碰撞优先级
        collpriority = goodvalue[carrygood] * timevalue * collvalue ;
        if(direction <= 0){
            direction = direction + 2 * Math.PI;
        }
        if(angle_d <= 0 ){
            angle_d = angle_d + 2 * Math.PI;
        }

        
        double x_e = (Kx * (nowFrame - startFrame + 1 ) + x0) - x;
        double y_e = (Ky * (nowFrame - startFrame + 1 ) + y0) - y;

//        if (fxResultant != 0 || fyResultant != 0){
//            x_e += 100 * Math.cos(angleRes);
//            y_e += 100 * Math.sin(angleRes);
//        }

        
//        double x_e = x_d  - x;
//        double y_e = y_d  - y;
        
//        if(x_e * x_e + y_e * y_e >36){
//            predict_x = x + Math.cos(angle_d) * 6;
//            predict_y = y + Math.sin(angle_d) * 6;
//            x_e = predict_x - x;
//            y_e = predict_y - y;
//            setV_d(4);
//        }else{
//            setV_d(1);
//        }

        double e_3 = angle_d - direction;
        double sin_dirction = Math.sin(direction);
        double cos_dirction = Math.cos(direction);
        double e_1 = cos_dirction * x_e + sin_dirction * y_e;
        double e_2 = -sin_dirction * x_e  + cos_dirction * y_e;
        
        if(angle_d > 0 && angle_d < Math.PI && e_3 < -Math.PI ){
            e_3 = e_3 + 2* Math.PI;
        }
        if(angle_d > Math.PI && angle_d < 2 * Math.PI && e_3 > Math.PI ){
            e_3 = e_3 - 2* Math.PI;
        }
//        if(e_3==0){
//            e_3 = 0.0000000001;
//        }
        
        //预测角平分线的方向和位置
        predictangle = e_3 /2 + direction;
        if(predictangle >= Math.PI *2){
            predictangle -= 2*Math.PI ;
        }
        
        predict_x = x + Math.cos(predictangle) * v_r * 0.5;
        predict_y = y + Math.sin(predictangle) * v_r * 0.5;
        
        
        
        //记录 v 最好3  lamda1最好6  lamda2最好2 lamda3最好70  v_d 最好 3  或者1
    
    
        
        
//        if(collalert == true){
//            //以下   碰撞避免算法
//
//            //以上
//            collalert = false;
//        }
        
//        if(cnt_frame >=  0 || x_e * x_e + y_e * y_e <36){
            //        v_r = v_d * Math.cos(e_3) + 6*Math.tanh(e_1);
            v_r = v_d * Math.cos(e_3) + 4 *Math.tanh(e_1);
            //   (1+ e_1 * e_1 + e_2*e_2 )     phi_r = v_r / v_d * angle_d + 2 * v_r * e_2 * Math.sin(e_3) /( (1+ e_1 * e_1 + e_2*e_2 ) * e_3)+ 70 * Math.tanh( e_3 ) / v_d;
            
        
            phi_r = v_r / v_d * angle_d + 2 * v_r * e_2  *Math.sin(e_3) /( (1+ e_1 * e_1 + e_2*e_2 )  * e_3)+ 100 * Math.sin( e_3 ) / v_d;
            cnt_frame -- ;
            
            if(e_3 == 0){
                phi_r = 0  ;
            }
//        }else{
//            v_r = 6;
//            phi_r = v_r / v_d * angle_d + 2 * v_r * e_2  *Math.sin(e_3) /( (1+ e_1 * e_1 + e_2*e_2 )  * e_3)+ 1000 * Math.sin( e_3 ) / v_d;
//
//        }
        //靠近点位进行减速逼近
        if( (x_d - x ) * (x_d - x ) + (y_d - y)  * (y_d - y ) < 4 || nowFrame > startFrame + totalFrame ){
            x_e = x_d - x ;
            y_e = y_d - y ;
            e_1 = cos_dirction * x_e + sin_dirction * y_e;
            e_2 = -sin_dirction * x_e  + cos_dirction * y_e;
            
            v_r = 2 * Math.cos(e_3) + 3 *Math.tanh(e_1);
            phi_r = v_r / v_d * angle_d + 2 * v_r * e_2  *Math.sin(e_3) /( (1+ e_1 * e_1 + e_2*e_2 )  * e_3)+ 100 * Math.sin( e_3 ) / v_d;
        }
        
        if (ismap4){
            x_e = x_d  - x;
            y_e = y_d  - y;
            e_3 = angle_d - direction;
            sin_dirction = Math.sin(direction);
            cos_dirction = Math.cos(direction);
            e_1 = cos_dirction * x_e + sin_dirction * y_e;
            e_2 = -sin_dirction * x_e  + cos_dirction * y_e;
            if(angle_d > 0 && angle_d < Math.PI && e_3 < -Math.PI ){
                e_3 = e_3 + 2* Math.PI;
            }
            if(angle_d > Math.PI && angle_d < 2 * Math.PI && e_3 > Math.PI ){
                e_3 = e_3 - 2* Math.PI;
            }
            if( x_e * x_e + y_e* y_e > 36){
                setV_d(4);
            } else if( x_e * x_e + y_e* y_e < 10 ){
                setV_d(2);
            }

            v_r = v_d * Math.cos(e_3) + 6 *Math.tanh(e_1);
            phi_r = v_r / v_d * angle_d + 2 * v_r * e_2  *Math.sin(e_3) /( (1+ e_1 * e_1 + e_2*e_2 )  * e_3)+ 100 * Math.sin( e_3 ) / v_d;
            if(e_3 == 0){
                phi_r = 0  ;
            }
        }

        last_e3 = e_3 ;
        
    }
    
    
    public void getDestinatiuon(List<Station> sta){
        if(task_i < target.size() && task_i >= 0){
            Station station = sta.get( target.get(task_i) );
            x_d = station.getX() ;
            y_d = station.getY() ;
    
            angle_d = Math.atan2( y_d - y , x_d - x);
            
        }
        
        
    
    }
    
    //引用这个程序时，需要先判断robot是否已经有目标，有目标的话不需要寻找
    public boolean searchStation1(List<Station> list1){
        
//        if(ismap4){
//            for(int set = 0 ; set < list1.size() ; set++){
//                Station station = list1.get(set);
//                //遍历所有的 7 或者 9 看有没有需求的 材料出现
//                if( station.queryDemand().size() != 0  ){
//                    //queryDemand会返回此站点需求信息
//                    List<Integer> ll = station.queryDemand();
//                    int type = station.types;
//                    for(int i : ll){
////                if(!ll.isEmpty())
//                        //缺 i 的材料
////                  i = ll.get(0);
//                        //这里需要找到，合适的i类型对应的站点
//                        //并且把这个站点加入任务系统，并且该站点需要置位
//                        //robot.isbusy设置为 true
//                        //最后把原站点加入task系统
//                        List<Station> dislist = new ArrayList<>();
//                        if(type == 7 ){
//                            //todo 车-456-7最近  而不是  456-7最近
//                            if(i ==4){
//                                dislist = station.distance1;
//                            }else if(i ==5){
//                                dislist = station.distance2;
//                            }else if(i ==6){
//                                dislist = station.distance3;
//                            }
//                            int minDistatnce = 100000;
//                            int station_456_no = -1;
//                            Deque<Integer> queue = new ArrayDeque<>();
//                            for(Station station_base : dislist){
//                                // 七号找四五六号不需要判断flag
////                            if(station_base.isHavegenerated() && station_base.isFlag() ){
//                                if( (  station_base.isHavegenerated() || (station_base.getProduct_time() >= 0 && station_base.getProduct_time() <= 50) )&& !station_base.isFlag()){
//                                    int Distance_i_to_456 = mapCandies[(int) x][(int) y][station_base.no];
//                                    int Distance_456_to_7 = mapCandies[(int) station_base.x][(int) station_base.y][station.no];
//                                    if(Distance_i_to_456 +Distance_456_to_7 < minDistatnce){
//                                        minDistatnce = Distance_i_to_456 +Distance_456_to_7;
//                                        station_456_no = station_base.no;
//                                        queue.clear();
//                                        queue.addLast(station_base.no);
//                                        queue.addLast(station.no);
//
//                                    }
//                                }
//                            }
//                            if(minDistatnce < 100000 && station_456_no != -1 ){
//                                List<Station> station_temp = new ArrayList<>();
//                                if(sta.get(station_456_no).types  == 4){
//                                    if (sta.get(station_456_no).queryDemand().size() == 0){
//                                        return false;
//                                    }else if (sta.get(station_456_no).queryDemand().size() == 2){
//                                        this.target.add( station_456_no );
//                                        //有车来 station_base 产品 置为true ; 否则false`
//                                        sta.get(station_456_no).setFlag(true);
//                                        this.isbusy = true;
//                                        this.target.add( station.getNo());
//                                        if(i == 4){
////                                    station.rob_come_flag[0] = true;
//                                            station.rob_come_cnt[0] = 1;
//                                        }
//                                        if(i == 5){
//                                            station.rob_come_cnt[1] = 1;
//                                        }
//                                        if(i == 6){
//                                            station.rob_come_cnt[2] = 1;
//                                        }
//
//                                        return true;
//
//                                    }
//
//                                }
//
//
//                                this.target.add( station_456_no );
//                                //有车来 station_base 产品 置为true ; 否则false`
//                                sta.get(station_456_no).setFlag(true);
//                                this.isbusy = true;
//                                this.target.add( station.getNo());
//                                if(i == 4){
////                                    station.rob_come_flag[0] = true;
//                                    station.rob_come_cnt[0] = 1;
//                                }
//                                if(i == 5){
//                                    station.rob_come_cnt[1] = 1;
//                                }
//                                if(i == 6){
//                                    station.rob_come_cnt[2] = 1;
//                                }
//
//                                return true;
//                            }
//
//                        }
//
//
//
//                    }
//                }
//            }
//            return false;
//
//        }


        for(int set = 0 ; set < list1.size() ; set++){
            Station station = list1.get(set);
            //遍历所有的 7 或者 9 看有没有需求的 材料出现
            if( station.queryDemand().size() != 0  ){
                //queryDemand会返回此站点需求信息
                List<Integer> ll = station.queryDemand();
                int type = station.types;
                for(int i : ll){
//                if(!ll.isEmpty())
                    //缺 i 的材料
//                  i = ll.get(0);
                    //这里需要找到，合适的i类型对应的站点
                    //并且把这个站点加入任务系统，并且该站点需要置位
                    //robot.isbusy设置为 true
                    //最后把原站点加入task系统
                    List<Station> dislist = new ArrayList<>();
                    if(type == 4){
                        if(i==1){
                            dislist = station.distance1;
                        }else if(i==2){
                            dislist = station.distance2;
                        }
                    }
                    
                    if(type == 5){
                        if(i==1){
                            dislist = station.distance1;
                        }else if(i==3){
                            dislist = station.distance2;
                        }
                        
                    }
                    
                    if(type == 6){
                        if(i == 2){
                            dislist = station.distance1;
                        }else if( i ==3){
                            dislist = station.distance2;
                        }
                    }
                    
                    if(type == 7 ){
                        //todo 车-456-7最近  而不是  456-7最近
                        if(i == 4){
                            if(ismap4){
                                continue;
                            }
                            dislist = station.distance1;
                        }else if(i ==5){
                            dislist = station.distance2;
                        }else if(i ==6){
                            dislist = station.distance3;
                        }
                        int minDistatnce = 100000;
                        int station_456_no = -1;
                        Deque<Integer> queue = new ArrayDeque<>();
                        for(Station station_base : dislist){
                            // 七号找四五六号不需要判断flag
//                            if(station_base.isHavegenerated() && station_base.isFlag() ){
                            if( (  station_base.isHavegenerated() || (station_base.getProduct_time() >= 0 && station_base.getProduct_time() <= 50) )&& !station_base.isFlag()){
                                int Distance_i_to_456 = mapCandies[(int) x][(int) y][station_base.no];
                                int Distance_456_to_7 = mapCandies[(int) station_base.x][(int) station_base.y][station.no];
                                if(Distance_i_to_456 +Distance_456_to_7 < minDistatnce){
                                    minDistatnce = Distance_i_to_456 +Distance_456_to_7;
                                    station_456_no = station_base.no;
                                    queue.clear();
                                    queue.addLast(station_base.no);
                                    queue.addLast(station.no);
                                    
                                }
                            }
                        }
                        if(minDistatnce < 100000 && station_456_no != -1 ){
                            this.target.add( station_456_no );
                            //有车来 station_base 产品 置为true ; 否则false`
                            sta.get(station_456_no).setFlag(true);
                            this.isbusy = true;
                            this.target.add( station.getNo());
                            if(i == 4){
//                                    station.rob_come_flag[0] = true;
                                station.rob_come_cnt[0] = 1;
                            }
                            if(i == 5){
                                station.rob_come_cnt[1] = 1;
                            }
                            if(i == 6){
                                station.rob_come_cnt[2] = 1;
                            }
                            if(station.queryDemand().size() == 0 && ismap1){
                                //进行排序，querydemand.size()进行排序，1230这样排序
                                Collections.sort(stalist_7_new, new Comparator<Station>() {
                                    @Override
                                    public int compare(Station o1, Station o2) {
                                        int size1;
                                        int size2;
                                        if (o1.queryDemand().size() == 0){
                                            size1 = 4;
                                        }else {
                                            size1 = o1.queryDemand().size();
                                        }
                
                                        if (o2.queryDemand().size() == 0){
                                            size2 = 4;
                                        }else {
                                            size2 = o2.queryDemand().size();
                                        }
                
                
                
                                        if (size1 > size2) {
                                            return 1;
                                        } else if (size1 < size2) {
                                            return -1;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            }
                            return true;
                        }
                        
                    }


                    if (type == 9){
                        int minDistance = 100000;
                        int station_456_no = -1;
                        Deque<Integer> queue = new ArrayDeque<>();
                        for (Station station_456_now : stalist_456){
//                            if ( (station_456_now.isHavegenerated() ||(station_456_now.getProduct_time() >= 0 && station_456_now.getProduct_time() <= 50) ) && !station_456_now.isFlag()){
                            if ( (station_456_now.isHavegenerated()  ) && !station_456_now.isFlag()){
                                int Distance_i_to_456 = mapCandies[(int) x][(int) y][station_456_now.no];
                                int Distance_456_to_9 = mapCandies[(int) station_456_now.x][(int) station_456_now.y][station.no];
                                if (Distance_i_to_456 + Distance_456_to_9 < minDistance){
                                    minDistance = Distance_i_to_456 + Distance_456_to_9;
                                    station_456_no = station_456_now.no;
                                    queue.clear();
                                    queue.addLast(station_456_now.no);
                                    queue.addLast(station.no);
                                }
                            }
                        }
                        if (minDistance < 100000 && station_456_no != -1){
                            List<Station> stationtemp = new ArrayList<>();
                            stationtemp.add(sta.get(station_456_no));
                            if(this.searchStation2_new(stationtemp)){
                            
                            }
                            while (!queue.isEmpty()){
                                this.target.add(queue.pollFirst());
                            }
                            sta.get(station_456_no).setFlag(true);
                            this.isbusy = true;
                            return true;
                        }
                    }
                    
                    if(type == 8){
                        if(i == 7){
                            dislist = station.distance1;
                        }
                        
                        for(Station station_base : dislist){
                            //bug station_base 没有判断
                            if(station_base.isHavegenerated() && ! station_base.isFlag()){
                                this.target.add( station_base.getNo() );
                                station_base.setFlag(true);
                                this.isbusy = true;
                                this.target.add( station.getNo());
//                                station.setFlag(false);
                                return true;
                            }else{
            
                            }
        
                        }
                        
                    }
                    
                    if(type == 4 ||type ==5 || type == 6  ){
                        for(Station station_base : dislist){
                            this.target.add( station_base.getNo() );
//                            station_base.setFlag(false);
                            this.isbusy = true;
                            this.target.add( station.getNo());
                            station.setFlag(false);
                            Collections.swap(list1,0,set);
                            return true;
                        }
                    }
                    
                    
                }
            }
        }
        return false;
    }

    public void doubleFill(Station sta1, Station sta2, Station sta0){
        this.target.add( sta1.getNo() );
        this.target.add( sta0.getNo());
        this.target.add( sta2.getNo());
        this.target.add( sta0.getNo());

    }

    //对该站点只找访问一次操作
    public boolean searchStation2(List<Station> list1 ,Deque<Integer> deque){
        boolean ans = false;
        //图124用

        if(stalist_9.size() ==0  || ismap1 ||ismap2 || ismap4 ){


            int minDistance = 500000;
            int station_no = -1;
            int station_first = -1;//最终答案
            int station_second = -1;
            int station_456_type = -1;
            int station_123_type = -1;
            Deque<Integer> queue = new ArrayDeque<>();
            for (int i = 0; i < list1.size() ; i++ ){
                Station station = list1.get(i);
                
                
                
                if ( ( station.state == 1  |station.state == 3 | station.state == 5 | station.state == 7 ) && station.queryDemand().size() != 0) {
                    List<Station> stationList_123 = new ArrayList<>();
                    switch(station.types){
                        case 4:
                            if(station.queryDemand().size() == 2){
                                stationList_123.addAll(station.distance1);
                                stationList_123.addAll(station.distance2);
                            }else if(station.queryDemand().size() == 1){
                                if(station.queryDemand().get(0) == 1){
                                    stationList_123 = station.distance1;
                                }else{
                                    stationList_123 = station.distance2;
                                }
                            }
                            break;
                        case 5:
                            if(station.queryDemand().size() == 2){
                                stationList_123.addAll(station.distance1);
                                stationList_123.addAll(station.distance2);
                            }else if(station.queryDemand().size() == 1){
                                if(station.queryDemand().get(0) == 1){
                                    stationList_123 = station.distance1;
                                }else{
                                    stationList_123 = station.distance2;
                                }
                            }
                            break;
                        case 6:
                            if(station.queryDemand().size() == 2){
                                stationList_123.addAll(station.distance1);
                                stationList_123.addAll(station.distance2);
                            }else if(station.queryDemand().size() == 1){
                                if(station.queryDemand().get(0) == 2){
                                    stationList_123 = station.distance1;
                                }else{
                                    stationList_123 = station.distance2;
                                }
                            }
                            break;
                    }
                    for (Station station_123 : stationList_123){
                        int distance_i_to_123 = mapCandies[(int) this.x][(int) this.y][station_123.no];
                        int distance_123_to_456 = mapCandies[(int) station_123.x][(int) station_123.y][station.no];
                        if(station.queryDemand().contains(station_123.types)){
                            if (distance_i_to_123 + distance_123_to_456 < minDistance) {
                                minDistance = distance_i_to_123 + distance_123_to_456;
                                station_no = station.no;
                                station_first = station_123.no;
                                station_456_type = station.types;
                                station_123_type = station_123.types;
                            }
                        }
                       

                    }

//                    List<Station> stationList_12 = station.distance1;
//                    stationList_12.addAll(station.distance2);
//                    for (Station station12_now : stationList_12) {
//                        int distance_i_to_12 = mapCandies[(int) this.x][(int) this.y][station12_now.no];
//                        int distance_12_to_4 = mapCandies[(int) station12_now.x][(int) station12_now.y][station.no];
//                        int distance_another_to_4 = 10000;
//                        int station_one = -1;//每次遍历的最后答案
//                        int station_two = -1;
//
//                        if (station12_now.types == 1 | (station12_now.types == 2 && station.types == 6)) {
//                            distance_another_to_4 = mapCandies[(int) station.x][(int) station.y][station.distance2.get(0).no];
//                            station_one = station12_now.no;
//                            station_two = station.distance2.get(0).no;
//                        }//对于1类型的材料，他的另一个材料无论是2还是3，都对应station。distance2
//                        if (station12_now.types == 3 | (station12_now.types == 2 && station.types == 4)) {
//                            distance_another_to_4 = mapCandies[(int) station.x][(int) station.y][station.distance1.get(0).no];
//                            station_one = station12_now.no;
//                            station_two = station.distance1.get(0).no;
//                        }//对于3类型的材料，他的另一个材料无论是1还是2，都对应station。distance1
//
//                        //计算i_to_123 + 123_to_456 + 2 * 456最近的个另一个材料的站点的距离
//                        if (distance_i_to_12 + distance_12_to_4 + 2 * distance_another_to_4 < minDistance) {
//                            minDistance = distance_i_to_12 + distance_12_to_4 + 2 * distance_another_to_4;
//                            station_no = station.no;
//                            station_first = station_one;
//                            station_second = station_two;
//
//                        }
//                    }

                //两个括号不能动位置
                }
            }

//            if (station_first != -1 &&  station_no != -1) {
//                this.target.add(station_first);
//                this.target.add(station_no);
//                this.target.add(station_second);
//                this.target.add(station_no);
//                sta.get(station_no).rob_come_cnt[0] = 1;
//                sta.get(station_no).rob_come_cnt[1] = 1;
//                this.isbusy = true;
//                return true;
//            }



            if (station_first != -1 && station_no != -1 && sta.get(station_no).queryDemand().contains(sta.get(station_first).types)) {
                deque.addLast(station_first);
                deque.addLast(station_no);
                
                this.target.add(station_first);
                this.target.add(station_no);

                int set = -1;
                switch(station_456_type){
                    case 4:
                        if(station_123_type==1){
                            set = 0;
                        }else{
                            set = 1;
                        }
                        sta.get(station_no).rob_come_cnt[set] = 1;
                        if(sta.get(station_no).queryDemand().size() == 0){
                            global456[0] += 1;
                        }
                        break;
                    case 5:
                        if(station_123_type==1){
                            set = 0;
                        }else{
                            set = 1;
                        }
                        sta.get(station_no).rob_come_cnt[set] = 1;
                        if(sta.get(station_no).queryDemand().size() == 0){
                            global456[1] += 1;
                        }
                        break;
                    case 6:
                        if(station_123_type==2){
                            set = 0;
                        }else{
                            set = 1;
                        }
                        sta.get(station_no).rob_come_cnt[set] = 1;
                        if(sta.get(station_no).queryDemand().size() == 0){
                            global456[2] += 1;
                        }
                        break;
                    default:
                        break;

                }
                this.isbusy = true;
                return true;
            }
        }
        

        
        return ans;
    }
    
    //整体逻辑为：对7站点进行判断是否有7生成，有7生成才进行1-4-7-8操作
    public boolean searchStation3( List<Station> list_7 ) throws IOException {
        Deque<Integer> queue = new ArrayDeque<>();
        Deque<Integer> deque = new ArrayDeque<>();
            for(Station station7 : list_7){
                Station station8 = station7.distance4.get(0);
                
                if((station7.isHavegenerated() || (station7.product_time >= 0 && ismap4)  ) && !station7.isFlag() ){

                    queue.addLast(station7.getNo() );
                    queue.addLast(station8.getNo() );
                    if(ismap1){
                        while(!queue.isEmpty()){
                            this.target.add(queue.pollFirst());
                        }
                        station7.setFlag(true);
                        this.isbusy = true ;
                        return true;
                    }
                    List<Integer> demand = station7.queryDemand();
                    
                    switch (station7.state){
                        case 5:
                        case 7:
                            if(demand.size() != 0){
                                //cnt =1
                                
                                for(int type : demand){
//                                    cnt ++;
                                    int minDistatance = 100000;
                                    int station_456_no = -1;
                                    int set = -1;
                                    List<Station> station_456_list = new ArrayList<>();
                                    if(type ==4){
                                        station_456_list = station7.distance1;
                                        set = 0;
                                    }
                                    if(type ==5){
                                        station_456_list = station7.distance2;
                                        set = 1;
                                    }
                                    if(type ==6){
                                        station_456_list = station7.distance3;
                                        set = 2;
                                    }
                                    
                                    
                                    for(Station station_456_now : station_456_list){
                                        if( (station_456_now.isHavegenerated() || (station_456_now.getProduct_time() >=0 && station_456_now.getProduct_time() <=50)) && !station_456_now.isFlag() ){
//                                        if( (station_456_now.isHavegenerated() || (station_456_now.getProduct_time() >=0 ) ) && !station_456_now.isFlag() ){
                                                
                                                
                                                
                                                int Distance_i_to_456 = mapCandies[(int) x][(int) y][station_456_now.no];
                                                int Distance_456_7 = mapCandies[(int) station_456_now.x][(int) station_456_now.y][ station7.no];
                                                if(Distance_i_to_456 + Distance_456_7 < minDistatance){
                                                    minDistatance = Distance_i_to_456 + Distance_456_7;
                                                    station_456_no = station_456_now.no;
                                                    deque.clear();
                                                    deque.addLast(station_456_now.no);
                                                    deque.addLast(station7.no);
                                                }
                                            
                                                
//                                            queue.addFirst(station7.getNo());
//                                            queue.addFirst(station_456_now.getNo());
//                                            List<Station> station_temp = new ArrayList<>();
//                                            station_temp.add(station_456_now);
//                                            //调用二方法，添加4操作的target
//
//                                            if(this.searchStation2(station_temp)){
//                                                cnt++;
//                                            }
//
//                                            while(!queue.isEmpty()){
//                                                this.target.add(queue.pollFirst());
//
//                                            }
//                                            station7.rob_come_cnt[set] = 1;
//                                            station_456_now.setFlag(true);
//                                            station7.setFlag(true);
//                                            this.isbusy = true ;
//                                            return true;
                                        }
                                    }
    
                                    List<Station> station_temp = new ArrayList<>();
                                    //调用之后的读取
                                    Deque<Integer> deque_temp = new ArrayDeque<>();
                                    if(minDistatance < 100000 && station_456_no != -1 && !ismap4){
                                        station_temp.add(sta.get(station_456_no));
                                        //图124都是 填一次 即1-4-7-8;
                                        if(this.searchStation2(station_temp,deque_temp)){

                                        }
                                        
                                        while(!deque.isEmpty()){
                                            this.target.add(deque.pollFirst());
                                        }
                                        while(!queue.isEmpty()){
                                            this.target.add(queue.pollFirst());
                                        }
                                        station7.rob_come_cnt[set] = 1;
                                        sta.get(station_456_no).setFlag(true);
                                        station7.setFlag(true);
                                        this.isbusy = true;

                                        return true;
                                    }
                                    
                                    
                                    if(minDistatance < 100000 && station_456_no != -1 && ismap4){
                                        if(station7.isHavegenerated()){
                                            station_temp.add(sta.get(station_456_no));
                                                //图124都是 填一次 即1-4-7-8;
                                            if(this.searchStation2(station_temp,deque_temp)){
                                                while(!deque.isEmpty()){
                                                    this.target.add(deque.pollFirst());
                                                }
                                                while(!queue.isEmpty()){
                                                    this.target.add(queue.pollFirst());
                                                }
                                                station7.rob_come_cnt[set] = 1;
                                                sta.get(station_456_no).setFlag(true);
                                                station7.setFlag(true);
                                                this.isbusy = true;
                                                return true;
                                            }else{
                                                return false;
                                            }
                                            

                                        }else if(station7.product_time >=0){
                                            //这里，通过判断剩余生成时间够不够去到一系列流程取到1-4-7-8
                                            station_temp.add(sta.get(station_456_no));
                                            if(this.searchStation2(station_temp,deque_temp)){
                                            }
                                            int frame_cnt = 0;
                                            int last_location_no = -1;
                                            while(!deque_temp.isEmpty()){
                                                int now_location_no = deque_temp.pollFirst();
                                                if(last_location_no != -1){
                                                    frame_cnt += reqFrames[last_location_no][now_location_no];
                                                }
                                                last_location_no = now_location_no;
                                            }
                                            while(!deque.isEmpty()){
                                                int now_location_no = deque.pollFirst();
                                                if(last_location_no != -1){
                                                    frame_cnt += reqFrames[last_location_no][now_location_no];
                                                }
                                                last_location_no = now_location_no;
                                            }
                                            while(!queue.isEmpty()){
                                                int now_location_no = queue.pollFirst();
                                                if(last_location_no != -1){
                                                    frame_cnt += reqFrames[last_location_no][now_location_no];
                                                }
                                                last_location_no = now_location_no;
                                            }

                                            if(frame_cnt > station7.product_time){
                                                station7.rob_come_cnt[set] = 1;
                                                sta.get(station_456_no).setFlag(true);
                                                station7.setFlag(true);
                                                this.isbusy = true;
                                                return true;
                                            }


                                        }

                                    }
                                    
                                }
                                
                                
                                
                            }else{
                                while(!queue.isEmpty()){
                                    this.target.add(queue.pollFirst());
                                }
//                                cnt++;
                                station7.setFlag(true);
                                this.isbusy = true ;
                                return true;
                            }
                            
                            break;
                        case 6:
                            //最急
                            while(!queue.isEmpty()){
                                this.target.add(queue.pollFirst());
                            }
//                            cnt++;
                            station7.setFlag(true);
                            this.isbusy = true ;
                            return true;
                            
                        default:
//                            myutil.outStr("状态错误");
                            break;
                    }
            
                }else{
                    List<Integer> demand = station7.queryDemand();
                    switch (station7.state){
                        case 1:
                        
                        case 3:
                        default:
                            break;
                    
                    }
            
                }
                
            
        }
        
        
    
//        return true;
        
        return false;
    }

    public boolean searchStation3_map2( List<Station> list_7 ) {

        List<Integer> list_map = new ArrayList<>();
        for(Station station7 : list_7){
            Deque<Integer> queue = new ArrayDeque<>();
            Deque<Integer> deque = new ArrayDeque<>();
            Station station8 = station7.distance4.get(0);
            if(station7.no == 3){
                list_map.add(0);
                list_map.add(1);
                list_map.add(12);
            }else{
                list_map.add(12);
                list_map.add(23);
                list_map.add(24);
            }

            if(station7.isHavegenerated() && !station7.isFlag() ){
                queue.addLast(station7.getNo() );
                queue.addLast(station8.getNo() );


                List<Integer> demand = station7.queryDemand();

                switch (station7.state){
                    case 5:
                    case 7:
                        if(demand.size() != 0){
                            //cnt =1

                            for(int type : demand){
//                                    cnt ++;
                                int minDistatance = 100000;
                                int station_456_no = -1;
                                int set = -1;
                                List<Station> station_456_list = new ArrayList<>();
                                if(type ==4){
                                    station_456_list = station7.distance1;
                                    set = 0;
                                }
                                if(type ==5){
                                    station_456_list = station7.distance2;
                                    set = 1;
                                }
                                if(type ==6){
                                    station_456_list = station7.distance3;
                                    set = 2;
                                }


                                for(Station station_456_now : station_456_list){
                                    if( (station_456_now.isHavegenerated() || (station_456_now.getProduct_time() >=0 && station_456_now.getProduct_time() <=50)) && !station_456_now.isFlag() && list_map.contains(station_456_now.no) ){
//
                                        int Distance_i_to_456 = mapCandies[(int) x][(int) y][station_456_now.no];
                                        int Distance_456_7 = mapCandies[(int) station_456_now.x][(int) station_456_now.y][ station7.no];
                                        if(Distance_i_to_456 + Distance_456_7 < minDistatance){
                                            minDistatance = Distance_i_to_456 + Distance_456_7;
                                            station_456_no = station_456_now.no;
                                            deque.clear();
                                            deque.addLast(station_456_now.no);
                                            deque.addLast(station7.no);
                                        }


//
                                    }
                                }

                                List<Station> station_temp = new ArrayList<>();
                                //调用之后的读取
                                Deque<Integer> deque_temp = new ArrayDeque<>();
                                if(minDistatance < 100000 && station_456_no != -1 ){
                                    station_temp.add(sta.get(station_456_no));
                                    //图124都是 填一次 即1-4-7-8;
                                    if(this.searchStation2(station_temp,deque_temp)){

                                    }

                                    while(!deque.isEmpty()){
                                        this.target.add(deque.pollFirst());
                                    }
                                    while(!queue.isEmpty()){
                                        this.target.add(queue.pollFirst());
                                    }
                                    station7.rob_come_cnt[set] = 1;
                                    sta.get(station_456_no).setFlag(true);
                                    station7.setFlag(true);
                                    this.isbusy = true;

                                    return true;
                                }
                            }
                        }else{
                            while(!queue.isEmpty()){
                                this.target.add(queue.pollFirst());
                            }
//                                cnt++;
                            station7.setFlag(true);
                            this.isbusy = true ;
                            return true;
                        }

                        break;
                    case 6:
                        //最急
                        while(!queue.isEmpty()){
                            this.target.add(queue.pollFirst());
                        }
//                            cnt++;
                        station7.setFlag(true);
                        this.isbusy = true ;
                        return true;

                    default:
//                            myutil.outStr("状态错误");
                        break;
                }

            }else{
                List<Integer> demand = station7.queryDemand();
                switch (station7.state){
                    case 1:

                    case 3:
                    default:
                        break;

                }

            }


        }



//        return true;

        return false;


    }

    public boolean searchStation1_map2(List<Station> list1){

        List<Integer> list_map = new ArrayList<>();

        for(int set = 0 ; set < list1.size() ; set++){
            Station station = list1.get(set);
            if(station.no==3){
                list_map.add(0);
                list_map.add(1);
                list_map.add(12);
            }else{
                list_map.add(12);
                list_map.add(23);
                list_map.add(24);
            }

            //遍历所有的 7 或者 9 看有没有需求的 材料出现
            if( station.queryDemand().size() != 0  ){
                //queryDemand会返回此站点需求信息
                List<Integer> ll = station.queryDemand();
                int type = station.types;
                for(int i : ll){
//                if(!ll.isEmpty())
                    //缺 i 的材料
//                  i = ll.get(0);
                    //这里需要找到，合适的i类型对应的站点
                    //并且把这个站点加入任务系统，并且该站点需要置位
                    //robot.isbusy设置为 true
                    //最后把原站点加入task系统
                    List<Station> dislist = new ArrayList<>();


                    if(type == 7 ){
                        //todo 车-456-7最近  而不是  456-7最近
                        if(i == 4){
                            dislist = station.distance1;
                        }else if(i ==5){
                            dislist = station.distance2;
                        }else if(i ==6){
                            dislist = station.distance3;
                        }
                        int minDistatnce = 100000;
                        int station_456_no = -1;
                        Deque<Integer> queue = new ArrayDeque<>();
                        for(Station station_base : dislist){
                            // 七号找四五六号不需要判断flag
//                            if(station_base.isHavegenerated() && station_base.isFlag() ){
                            if( (  station_base.isHavegenerated() || (station_base.getProduct_time() >= 0 && station_base.getProduct_time() <= 50) )&& !station_base.isFlag() && list_map.contains(station_base.no)){
                                int Distance_i_to_456 = mapCandies[(int) x][(int) y][station_base.no];
                                int Distance_456_to_7 = mapCandies[(int) station_base.x][(int) station_base.y][station.no];
                                if(Distance_i_to_456 +Distance_456_to_7 < minDistatnce){
                                    minDistatnce = Distance_i_to_456 +Distance_456_to_7;
                                    station_456_no = station_base.no;
                                    queue.clear();
                                    queue.addLast(station_base.no);
                                    queue.addLast(station.no);

                                }
                            }
                        }
                        if(minDistatnce < 100000 && station_456_no != -1 ){
                            //并且进入访问一个单个的1->4
                            List<Station> list_temp = new ArrayList<>();
                            Deque<Integer> deque = new ArrayDeque<>();
                            list_temp.add(sta.get(station_456_no));
                            searchStation2(list_temp,deque );

                            this.target.add( station_456_no );
                            //有车来 station_base 产品 置为true ; 否则false`
                            sta.get(station_456_no).setFlag(true);
                            this.isbusy = true;
                            this.target.add( station.getNo());
                            if(i == 4){
//                                    station.rob_come_flag[0] = true;
                                station.rob_come_cnt[0] = 1;
                            }
                            if(i == 5){
                                station.rob_come_cnt[1] = 1;
                            }
                            if(i == 6){
                                station.rob_come_cnt[2] = 1;
                            }

                            return true;
                        }

                    }




                }
            }
        }
        return false;
    }


    public int[] findShortestStation456(){

        int[] ans = new int[3];
        int min = 500000;
        for (int i = 0; i < stalist_654.size(); i++) {
            Station station = stalist_654.get(i);
            if(!station.flag && station.queryDemand().size() == 2){
                // 该工作站到工作站的distance1的第一个的距离的平方
                double a1 = station.dis1;
                // 该工作站到工作站的distance2的第一个的距离的平方
                double a2 = station.dis2;
                // 机器人到该工作站的disance1的第一个的距离的平方
                int b1 = mapCandies[(int) x][(int) y][station.distance1.get(0).getNo()];
                // 机器人到该工作站的distance2的第一个的距离的平方
                int b2 = mapCandies[(int) x][(int) y][station.distance2.get(0).getNo()];

                for (int j = 0; j < station.distance1.size(); j++) {
                    for (int k = 0; k < station.distance2.size(); k++) {
                        // 机器人到其他工作站类型1的最短距离
                        int d1 = mapCandies[(int) x][(int) y][station.distance1.get(j).getNo()];
                        // 机器人到其他工作站类型2的最短距离
                        int d2 = mapCandies[(int) x][(int) y][station.distance2.get(k).getNo()];
                        // 其他工作站类型1到工作站类型4的距离
                        int c1 = mapCandies[(int) station.x][(int) station.y][station.distance1.get(j).getNo()];
                        // 其他工作站类型2到工作站类型4的距离
                        int c2 = mapCandies[(int) station.x][(int) station.y][station.distance2.get(k).getNo()];
                        int e = mapCandies[(int) station.x][(int) station.y][stalist_9.get(0).no];
                        int temp1 = (int) (d1 + c1 + a2 * 2 + e );
                        int temp2 = (int) (b1 + a1 + a2 * 2 + e );
                        int temp3 = (int) (b2 + a2 + a1 * 2 + e );
                        int temp4 = (int) (d2 + c2 + a1 * 2 + e );
                        int strategy = this.findStrategy_4(new double[]{temp1, temp2, temp3, temp4});

                        if(temp1 < min || temp2 < min || temp3 < min || temp4 < min){
                            switch (strategy){
                                case 0:
                                    min = temp1;
                                    ans[0] = station.distance1.get(j).getNo();
                                    ans[1] = station.getNo();
                                    ans[2] = station.distance2.get(0).getNo();
                                    break;
                                case 1:
                                    min = temp2;
                                    ans[0] = station.distance1.get(0).getNo();
                                    ans[1] = station.getNo();
                                    ans[2] = station.distance2.get(0).getNo();
                                    break;
                                case 2:
                                    min = temp3;
                                    ans[0] = station.distance2.get(0).getNo();
                                    ans[1] = station.getNo();
                                    ans[2] = station.distance1.get(0).getNo();
                                    break;
                                case 3:
                                    min = temp4;
                                    ans[0] = station.distance2.get(k).getNo();
                                    ans[1] = station.getNo();
                                    ans[2] = station.distance1.get(0).getNo();
                                    break;
                                default: break;
                            }
                        }
                    }
                }

            }

        }

        return ans;

    }


    public boolean searchStation1_new(List<Station> stalist){
        if(ismap1 | ismap4){
            for(Station station_456_now : stalist){
                if(   (station_456_now.state == 1 || station_456_now.state ==3 | station_456_now.state==5 || station_456_now.state == 7)
                      &&  station_456_now.queryDemand().size() != 0){
                    List<Station> stationList_123 = new ArrayList<>();
                    switch (station_456_now.types){
                        case 4:
                            if(station_456_now.queryDemand().size() == 2){
                                stationList_123.addAll(station_456_now.distance1);
                                stationList_123.addAll(station_456_now.distance2);
                            }else if(station_456_now.queryDemand().size() == 1){
                                if(station_456_now.queryDemand().get(0) == 1){
                                    stationList_123.addAll(station_456_now.distance1);
                                }else{
                                    stationList_123.addAll(station_456_now.distance2);
                                }
                            }
                            break;
                        case 5:
                            if(station_456_now.queryDemand().size() == 2){
                                stationList_123.addAll(station_456_now.distance1);
                                stationList_123.addAll(station_456_now.distance2);
                            }else if(station_456_now.queryDemand().size() == 1){
                                if(station_456_now.queryDemand().get(0) == 1){
                                    stationList_123.addAll(station_456_now.distance1);
                                }else{
                                    stationList_123.addAll(station_456_now.distance2);
                                }
                            }
                            break;
                        case 6:
                            if(station_456_now.queryDemand().size() == 2){
                                stationList_123.addAll(station_456_now.distance1);
                                stationList_123.addAll(station_456_now.distance2);
                            }else if(station_456_now.queryDemand().size() == 1){
                                if(station_456_now.queryDemand().get(0) == 2){
                                    stationList_123.addAll(station_456_now.distance1);
                                }else{
                                    stationList_123.addAll(station_456_now.distance2);
                                }

                            }
                            break;
                        default:
                            break;
                    }


                    int minDistatance = 100000;
                    int station_123_no = -1;
                    int station_123_type = -1;
                    Deque<Integer> deque = new ArrayDeque<>();
                    for(Station station_123 : stationList_123){
                        if (station_456_now.queryDemand().contains(station_123.types)){
                            int Distance_i_to_123 = mapCandies[(int) x][(int) y][station_123.no];
                            int Distance_123_to_456 = mapCandies[(int) station_123.x][(int) station_123.y][station_456_now.no];
                            if (Distance_i_to_123 + Distance_123_to_456 < minDistatance){
                                minDistatance = Distance_i_to_123 + Distance_123_to_456;
                                station_123_no = station_123.no;
                                station_123_type = station_123.types;
                                deque.clear();
                                deque.addLast(station_123_no);
                                deque.addLast(station_456_now.no);
                            }
                        }
                    }

                    if(minDistatance < 100000 && station_123_no != -1){
                        while(!deque.isEmpty()){
                            this.target.add(deque.pollFirst());
                        }
                        int set = -1;
                        switch(station_456_now.types){
                            case 4:
                                if(station_123_type==1){
                                    set = 0;
                                }else{
                                    set = 1;
                                }
                                station_456_now.rob_come_cnt[set] = 1;
                                if(station_456_now.queryDemand().size() == 0){
                                    global456[0] += 1;
                                }
                                break;
                            case 5:
                                if(station_123_type==1){
                                    set = 0;
                                }else{
                                    set = 1;
                                }
                                station_456_now.rob_come_cnt[set] = 1;
                                if(station_456_now.queryDemand().size() == 0){
                                    global456[1] += 1;
                                }
                                break;
                            case 6:
                                if(station_123_type==2){
                                    set = 0;
                                }else{
                                    set = 1;
                                }
                                station_456_now.rob_come_cnt[set] = 1;
                                if(station_456_now.queryDemand().size() == 0){
                                    global456[2] += 1;
                                }
                                break;
                            default:
                                break;

                        }


                        this.isbusy = true;


                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    public boolean searchStation4_new(List<Station> stationlist){
        for(Station station_7 : stalist_7){
            if(station_7.queryDemand().contains(4)){
                for(Station station_4 : stalist_4){
                    if( (station_4.isHavegenerated() || (station_4.product_time <250 && station_4.product_time >= 0)) && station_4.rob_come_cnt[1] == 0){
                        Station station_2  = station_4.distance2.get(0);
                        int reqfream  =0;
                        reqfream += reqFrames[station_2.no][station_4.no];
                        reqfream += reqFrames[station_4.no][station_7.no];
//                            if(station_7.isHavegenerated()){
                        this.target.add(station_2.no);
                        this.target.add(station_4.no);
                        this.target.add(station_4.no);
                        this.target.add(station_7.no);
                        station_4.setFlag(true);
                        station_4.rob_come_cnt[1] = 1;
                        station_7.rob_come_cnt[0] = 1;
                        this.isbusy = true;
//                            }else if(reqfream > station_7.product_time){
//                                this.target.add(station_1.no);
//                                this.target.add(station_4.no);
//                                this.target.add(station_4.no);
//                                this.target.add(station_7.no);
//                                station_4.setFlag(true);
//                                station_4.rob_come_cnt[0] = 1;
//                                station_7.rob_come_cnt[0] = 1;
//                                this.isbusy = true;
//
//                            }
//                            if(station_7.isHavegenerated()|| (reqfream > station_7.product_time && station_7.product_time >=0)&& !station_7.isFlag()  ) {
//                                this.target.add(station_7.no);
//                                this.target.add(station_7.distance4.get(0).no);
//                                station_7.setFlag(true);
//                            }
                        return true;
                    }
                
                }
            }
        }
    
    
        return false;
    }
    
    public boolean searchStation5_new(List<Station> stationlist){
        for(Station station_7 : stationlist){
            double D = Math.sqrt(  (this.x - station_7.x) *(this.x - station_7.x)+(this.y - station_7.y) *(this.y - station_7.y) ) ;
            if(D < 4){
                if( (station_7.isHavegenerated() ||  (station_7.product_time >=0 && station_7.product_time <=100)) && !station_7.isFlag() ) {
                    this.target.add(station_7.no);
                    this.target.add(station_7.distance4.get(0).no);
                    station_7.setFlag(true);
                    this.isbusy = true;
                    return true;
                }
            }
            
            
        }
        
        return false;
        
    }
    
    public boolean searchStation1_new_puls(List<Station> stalist,int robotID){
        if(ismap4){
            for(Station station_456_now : stalist){
                if(   (station_456_now.state == 1 || station_456_now.state ==3 | station_456_now.state==5 || station_456_now.state == 7)
                        &&  station_456_now.queryDemand().size() != 0){
                    List<Station> stationList_123 = new ArrayList<>();
                    switch (station_456_now.types){
                        case 4:
                            if(station_456_now.queryDemand().contains(1)){
                                stationList_123.addAll(station_456_now.distance1);
//                                stationList_123.addAll(station_456_now.distance2);
                            }
                            break;
                        case 5:
                            if(station_456_now.queryDemand().size() == 2){
                                stationList_123.addAll(station_456_now.distance1);
                                stationList_123.addAll(station_456_now.distance2);
                            }else if(station_456_now.queryDemand().size() == 1){
                                if(station_456_now.queryDemand().get(0) == 1){
                                    stationList_123.addAll(station_456_now.distance1);
                                }else{
                                    stationList_123.addAll(station_456_now.distance2);
                                }
                            }
                            break;
                        case 6:
                            if(station_456_now.queryDemand().size() == 2){
                                stationList_123.addAll(station_456_now.distance1);
                                stationList_123.addAll(station_456_now.distance2);
                            }else if(station_456_now.queryDemand().size() == 1){
                                if(station_456_now.queryDemand().get(0) == 2){
                                    stationList_123.addAll(station_456_now.distance1);
                                }else{
                                    stationList_123.addAll(station_456_now.distance2);
                                }
                                
                            }
                            break;
                        default:
                            break;
                    }
                    
                    
                    int minDistatance = 100000;
                    int station_123_no = -1;
                    int station_123_type = -1;
                    Deque<Integer> deque = new ArrayDeque<>();
                    for(Station station_123 : stationList_123){
                        if (station_456_now.queryDemand().contains(station_123.types)){
                            int Distance_i_to_123 = mapCandies[(int) x][(int) y][station_123.no];
                            int Distance_123_to_456 = mapCandies[(int) station_123.x][(int) station_123.y][station_456_now.no];
                            if (Distance_i_to_123 + Distance_123_to_456 < minDistatance){
                                minDistatance = Distance_i_to_123 + Distance_123_to_456;
                                station_123_no = station_123.no;
                                station_123_type = station_123.types;
                                deque.clear();
                                deque.addLast(station_123_no);
                                deque.addLast(station_456_now.no);
                            }
                        }
                    }
                    
                    if(minDistatance < 100000 && station_123_no != -1){
                        while(!deque.isEmpty()){
                            this.target.add(deque.pollFirst());
                        }
                        int set = -1;
                        switch(station_456_now.types){
                            case 4:
                                if(station_123_type == 1){
                                    set = 0;
                                }else{
                                    set = 1;
                                }
                                station_456_now.rob_come_cnt[set] = 1;
                                if(station_456_now.queryDemand().size() == 0){
                                    global456[0] += 1;
                                }
                                break;
                            case 5:
                                if(station_123_type==1){
                                    set = 0;
                                }else{
                                    set = 1;
                                }
                                station_456_now.rob_come_cnt[set] = 1;
                                if(station_456_now.queryDemand().size() == 0){
                                    global456[1] += 1;
                                }
                                break;
                            case 6:
                                if(station_123_type==2){
                                    set = 0;
                                }else{
                                    set = 1;
                                }
                                station_456_now.rob_come_cnt[set] = 1;
                                if(station_456_now.queryDemand().size() == 0){
                                    global456[2] += 1;
                                }
                                break;
                            default:
                                break;
                            
                        }
                        
                        
                        this.isbusy = true;
                        
                        
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    //这个函数用来递归的，递归当前站点，如何去取，并且判断此站点是什么state类型，进行填充操作后再返回回来。
    public boolean searchStation2_new(List<Station> list1){
    
            if( ismap3 || ismap2){
                for(int i = 0 ; i < list1.size() ; i++){
                    //遍历每个站点
                    Station station = list1.get(i);
                    //station.isFlag 此站点不忙碌，才进入 true 为不忙碌 false 为忙碌
                    if(station.queryDemand().size() != 0){
                
                        Station station1;
                        Station station2;
                        Station station3;
                
                        // 该工作站到工作站的distance1的第一个的距离的平方
                        double a1 = station.dis1;
                        // 该工作站到工作站的distance2的第一个的距离的平方
                        double a2 = station.dis2;
                        // 机器人到该工作站的disance1的第一个的距离的平方
                        double b1 = Math.pow(x - station.distance1.get(0).x, 2) + Math.pow(y - station.distance1.get(0).y, 2);
                        // 机器人到该工作站的distance2的第一个的距离的平方
                        double b2 = Math.pow(x - station.distance2.get(0).x, 2) + Math.pow(y - station.distance2.get(0).y, 2);
                        // 机器人到其他工作站类型1的最短距离，及编号
                        double[] d1 = this.findMinDis(station.distance1);
                        // 机器人到其他工作站类型2的最短距离，及编号
                        double[] d2 = this.findMinDis(station.distance2);
                        double c1 = Math.pow(sta.get((int) d1[1]).x - station.x, 2) + Math.pow(sta.get((int) d1[1]).y - station.y, 2);
                        double c2 = Math.pow(sta.get((int) d2[1]).x - station.x, 2) + Math.pow(sta.get((int) d2[1]).y - station.y, 2);
                        int strategy = this.findStrategy_4(new double[]{a1 + b1, a2 + b2, c1 + d1[0], c2 + d2[0]});
                
                        //通过状态判断是否执行
                        int state = station.state;
                        switch (state){
                    
                            case 0:
                                //不处理
                                break;
                            case 1:     // 001
                            case 5:
                                //1 5执行两次屯入
                                if(station.queryDemand().size() == 2){  // 有两个需求
                            
                                    if(strategy == 0 ){
                                
                                        doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                        doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                
                                    }else if(strategy == 1){
                                
                                        doubleFill(station.distance2.get(0), station.distance1.get(0), station);
                                        doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                
                                    } else if(strategy == 2){
                                
                                        doubleFill(sta.get((int) d1[1]), station.distance2.get(0), station);
                                        doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                
                                    }else {
                                
                                        doubleFill(sta.get((int) d2[1]), station.distance1.get(0), station);
                                        doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                
                                    }
                                    //                            station.rob_come_flag[0] = true;
                                    //                            station.rob_come_flag[1] = true;
                                    station.rob_come_cnt[0] = 2;
                                    station.rob_come_cnt[1] = 2;
                                    this.isbusy = true; // 设置为忙在忙
                                    return true;
                            
                            
                                }else{      // 有一个需求
                                    if ((station.queryDemand().get(0) == 1) || (station.types == 6 && station.queryDemand().get(0) == 2)){
                                        if(a1 + b1 > c1 + d1[0]){
                                            this.target.add((int) d1[1]);
                                        }else {
                                            this.target.add(station.distance1.get(0).getNo());
                                        }
                                        this.target.add( station.getNo());
                                        //                                station.rob_come_flag[0] = true;
                                        station.rob_come_cnt[0] = 1;
                                        this.isbusy = true; // 设置为忙在忙
                                        return true;
                                
                                    }else if(station.queryDemand().get(0) == 3 || (station.types == 4 && station.queryDemand().get(0) == 2)){
                                        if(a1 + b1 > c1 + d2[0]){
                                            this.target.add((int) d2[1]);
                                        }else {
                                            this.target.add(station.distance2.get(0).getNo());
                                        }
                                        this.target.add( station.getNo());
                                        //                                station.rob_come_flag[1] = true;
                                        station.rob_come_cnt[1] = 1;
                                        this.isbusy = true; // 设置为忙在忙
                                        return true;
                                
                                    }
                            
                                }
                                break;
                    
                            case 3:
                            case 7:
                                //
                                //3、7 执行一次屯入
                                if(station.queryDemand().size() == 2){  // 有两个需求
                            
                                    if(strategy == 0 ){
                                
                                        doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                
                                    }else if (strategy == 1){
                                
                                        doubleFill(station.distance2.get(0), station.distance1.get(0), station);
                                
                                    }else if(strategy == 2){
                                
                                        doubleFill(sta.get((int) d1[1]), station.distance2.get(0), station);
                                
                                    }else {
                                
                                        doubleFill(sta.get((int) d2[1]), station.distance1.get(0), station);
                                
                                    }
                                    //                            station.rob_come_flag[0] = true;
                                    //                            station.rob_come_flag[1] = true;
                                    station.rob_come_cnt[0] = 1;
                                    station.rob_come_cnt[1] = 1;
                                    this.isbusy = true; // 设置为忙在忙
                                    return true;
                            
                            
                                }else{      // 有一个需求
                                    if ((station.queryDemand().get(0) == 1) || (station.types == 6 && station.queryDemand().get(0) == 2)){
                                        if(a1 + b1 > c1 + d1[0]){
                                            this.target.add((int) d1[1]);
                                        }else {
                                            this.target.add(station.distance1.get(0).getNo());
                                        }
                                        this.target.add( station.getNo());
                                        //                                station.rob_come_flag[0] = true;
                                        station.rob_come_cnt[0] = 1;
                                        this.isbusy = true; // 设置为忙在忙
                                        return true;
                                
                                    }else if(station.queryDemand().get(0) == 3 || (station.types == 4 && station.queryDemand().get(0) == 2)){
                                        if(a1 + b1 > c1 + d2[0]){
                                            this.target.add((int) d2[1]);
                                        }else {
                                            this.target.add(station.distance2.get(0).getNo());
                                        }
                                        this.target.add( station.getNo());
                                        //                                station.rob_come_flag[1] = true;
                                        station.rob_come_cnt[1] = 1;
                                        this.isbusy = true; // 设置为忙在忙
                                        return true;
                                
                                    }
                            
                                }
                                break;
                    
                    
                            //                        //执行两次屯入3  5 操作一样
                            //                        station1 =  station.distance1.get(0);
                            //                        station2 =  station.distance2.get(0);
                            //                        this.target.add( station1.getNo() );
                            //                        this.target.add( station.getNo());
                            //                        this.target.add( station2.getNo());
                            //                        this.target.add( station.getNo());
                            //                        station.rob_come_flag[0] = true;
                            //                        station.rob_come_flag[1] = true;
                            //                        this.isbusy = true; // 设置为忙在忙
                            //                        return true;
                            case 2:
                                //状态2 不管找下一个站点
                                break;
                            case 4:
                                //不处理
                                break;
                            case 6:
                                //6的操作为取4 去 7 不应该在这里写
                                break;
                    
                    
                            default:
                                break;
                    
                        }
                    }
                }
            }
        
        return false;
    }

    public boolean searchStation2_map2(List<Station> list1){

            for(int i = 0 ; i < list1.size() ; i++){
                //遍历每个站点
                Station station = list1.get(i);
                //station.isFlag 此站点不忙碌，才进入 true 为不忙碌 false 为忙碌
                if(station.queryDemand().size() != 0){

                    Station station1;
                    Station station2;
                    Station station3;

                    // 该工作站到工作站的distance1的第一个的距离的平方
                    double a1 = station.dis1;
                    // 该工作站到工作站的distance2的第一个的距离的平方
                    double a2 = station.dis2;
                    // 机器人到该工作站的disance1的第一个的距离的平方
                    double b1 = Math.pow(x - station.distance1.get(0).x, 2) + Math.pow(y - station.distance1.get(0).y, 2);
                    // 机器人到该工作站的distance2的第一个的距离的平方
                    double b2 = Math.pow(x - station.distance2.get(0).x, 2) + Math.pow(y - station.distance2.get(0).y, 2);
                    // 机器人到其他工作站类型1的最短距离，及编号
                    double[] d1 = this.findMinDis(station.distance1);
                    // 机器人到其他工作站类型2的最短距离，及编号
                    double[] d2 = this.findMinDis(station.distance2);
                    double c1 = Math.pow(sta.get((int) d1[1]).x - station.x, 2) + Math.pow(sta.get((int) d1[1]).y - station.y, 2);
                    double c2 = Math.pow(sta.get((int) d2[1]).x - station.x, 2) + Math.pow(sta.get((int) d2[1]).y - station.y, 2);
                    int strategy = this.findStrategy_4(new double[]{a1 + b1, a2 + b2, c1 + d1[0], c2 + d2[0]});

                    //通过状态判断是否执行
                    int state = station.state;
                    switch (state){
                        case 1:     // 001
                            if(station.queryDemand().size() == 2){  // 有两个需求

                                if(strategy == 0 ){

                                    doubleFill(station.distance1.get(0), station.distance2.get(0), station);
                                    doubleFill(station.distance1.get(0), station.distance2.get(0), station);

                                }else if(strategy == 1){

                                    doubleFill(station.distance2.get(0), station.distance1.get(0), station);
                                    doubleFill(station.distance1.get(0), station.distance2.get(0), station);

                                } else if(strategy == 2){

                                    doubleFill(sta.get((int) d1[1]), station.distance2.get(0), station);
                                    doubleFill(station.distance1.get(0), station.distance2.get(0), station);

                                }else {

                                    doubleFill(sta.get((int) d2[1]), station.distance1.get(0), station);
                                    doubleFill(station.distance1.get(0), station.distance2.get(0), station);

                                }
                                //                            station.rob_come_flag[0] = true;
                                //                            station.rob_come_flag[1] = true;
                                station.rob_come_cnt[0] = 2;
                                station.rob_come_cnt[1] = 2;
                                this.isbusy = true; // 设置为忙在忙
                                return true;


                            }else{      // 有一个需求
                                if ((station.queryDemand().get(0) == 1) || (station.types == 6 && station.queryDemand().get(0) == 2)){
                                    this.target.add(station.distance1.get(0).getNo());
                                    this.target.add( station.getNo());
                                    station.rob_come_cnt[0] = 1;
                                    this.isbusy = true; // 设置为忙在忙
                                    return true;

                                }else if(station.queryDemand().get(0) == 3 || (station.types == 4 && station.queryDemand().get(0) == 2)){
                                    this.target.add(station.distance2.get(0).getNo());
                                    this.target.add( station.getNo());
                                    station.rob_come_cnt[1] = 1;
                                    this.isbusy = true; // 设置为忙在忙
                                    return true;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        return false;
    }
    
    public boolean searchStation3_new(List<Station> list){
        
        int minDistance = 100000;
        int station_no = -1;
        
        for(Station station_now : list){
            double a1 = station_now.dis1;
            double a2 = station_now.dis2;
            
            
            
            
            
        }
        return false;
    }

    public int findStrategy_4(double[] data){
        double min = data[0];
        int index = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i] < min){
                min = data[i];
                index = i;
            }
        }
        return index;

    }
    public double[] findMinDis(List<Station> list){

        double min = Double.MAX_VALUE;
        double index = 0;
        for (int i = 0; i < list.size(); i++){
            double temp = Math.pow(x - list.get(i).x, 2) + Math.pow(y - list.get(i).y, 2);
            if(temp < min){
                min = temp;
                index = list.get(i).getNo();
            }
        }
        return new double[]{min, index};

    }
    
    
//    //初始跑的程序，现在用不到
//    public void distaancebuy(List<Station> stalist_1 ,List<Station> sta){
//        Queue<Double> pq = new PriorityQueue<>();
//        Map<Double,Integer> map = new HashMap<>();
//        for(int i = 0 ; i < stalist_1.size() ; i++){
//
//            Station station = stalist_1.get(i);
//            if( station.isFlag() ){
//                //等于true 进入
//                double distance = (x - station.getX()) * (x - station.getX()) + (y - station.getY()) * (y - station.getY()) ;
//                pq.add(distance);
//                map.put(distance,station.getNo());
//            }
//        }
//        if(pq.size()>0){
//            int no = map.get(pq.peek());
//            this.target.add(no);
//            sta.get(no).setFlag(false);
//        }
//    }
//    //初始跑的程序，时尚代码
//    public void distaancesell(List<Station> stalist_9 ,List<Station> sta){
//        Queue<Double> pq = new PriorityQueue<>();
//        Map<Double,Integer> map = new HashMap<>();
//        for(int i = 0 ; i < stalist_9.size() ; i++){
//
//            Station station = stalist_9.get(i);
//                //等于true 进入
//                double distance = (x - station.getX()) * (x - station.getX()) + (y - station.getY()) * (y - station.getY()) ;
//                pq.add(distance);
//                map.put(distance,station.getNo());
//        }
//        if(pq.size()>0){
//            int no = map.get(pq.peek());
//            this.target.add(no);
//            task_i++;
//        }
//    }

    public void updateState(String str0, String str1, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9){
        double last_coll = getCollvalue();
        if(last_coll > Double.parseDouble(str4) ){
            test_colltime ++;
        }
        
        
        setNowloaction(Integer.parseInt(str0)); // 更新所处工作台ID
        setCarrygood(Integer.parseInt(str1)); // 更新携带物品
        setTimevalue(Double.parseDouble(str2)); // 更新时间价值系数
        setCollvalue(Double.parseDouble(str3)); // 更新碰撞价值系数
        setCollpriority( goodvalue[carrygood] * timevalue * collvalue );
        setAnglevelocity(Double.parseDouble(str4)); // 设置角速度
        double x_v = Double.parseDouble(str5);
        double y_v = Double.parseDouble(str6);
        double v =  Math.sqrt(x_v * x_v + y_v * y_v);
        setX_velocity(x_v); // 更新x方向速度
        setY_velocity(y_v); // 更新y方向速度
        setVelocity(v); // 更新更新合速度

        setDirection(Double.parseDouble(str7)); // 更新朝向 pi ~ -pi
        setX(Double.parseDouble(str8)); // 更新x坐标
        setY(Double.parseDouble(str9)); // 更新y坐标
        
        
        
    }


    // 构造器
    public Robot() {
    }

    public Robot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //get set 方法;

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCarrygood() {
        return carrygood;
    }

    public void setCarrygood(int carrygood) {
        this.carrygood = carrygood;
    }

    public int getNowloaction() {
        return nowloaction;
    }

    public void setNowloaction(int nowloaction) {
        this.nowloaction = nowloaction;
    }

    public double getTimevalue() {
        return timevalue;
    }

    public void setTimevalue(double timevalue) {
        this.timevalue = timevalue;
    }

    public double getCollvalue() {
        return collvalue;
    }

    public void setCollvalue(double collvalue) {
        this.collvalue = collvalue;
    }

    public double getAnglevelocity() {
        return anglevelocity;
    }

    public void setAnglevelocity(double anglevelocity) {
        this.anglevelocity = anglevelocity;
    }

    public double getX_velocity() {
        return x_velocity;
    }

    public void setX_velocity(double x_velocity) {
        this.x_velocity = x_velocity;
    }

    public double getY_velocity() {
        return y_velocity;
    }

    public void setY_velocity(double y_velocity) {
        this.y_velocity = y_velocity;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getV_d() {
        return v_d;
    }

    public void setV_d(double v_d) {
        this.v_d = v_d;
    }
    
    public double getAngle_d() {
        return angle_d;
    }
    
    public void setAngle_d(double angle_d) {
        this.angle_d = angle_d;
    }
    
    public double getX_d() {
        return x_d;
    }

    public void setX_d(double x_d) {
        this.x_d = x_d;
    }

    public double getY_d() {
        return y_d;
    }

    public void setY_d(double y_d) {
        this.y_d = y_d;
    }
    
    public double getV_r() {
        return v_r;
    }
    
    public void setV_r(double v_r) {
        this.v_r = v_r;
    }
    
    public double getPhi_r() {
        return phi_r;
    }
    
    public void setPhi_r(double phi_r) {
        this.phi_r = phi_r;
    }
    
    public double getCollpriority() {
        return collpriority;
    }
    
    public void setCollpriority(double collpriority) {
        this.collpriority = collpriority;
    }
    
    public int getTest_colltime() {
        return test_colltime;
    }
    
    public void setTest_colltime(int test_colltime) {
        this.test_colltime = test_colltime;
    }
}