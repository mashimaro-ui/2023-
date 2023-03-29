package com.huawei.codecraft;

import java.util.*;

/**
 * @author xyhong
 * @create 2023-03-10-20:27
 */

public abstract class Station {
    //序号，每个站点的序号都不一样 从0开始计数
    public int no ;
    //站点的类型，1-9种
    public int types;
    //特殊的站点的属性，用来表示此站点现在已有材料
    public boolean[] existgood;
//    //反计算，这个站还缺什么材料
//    public List<Integer> needgood = new ArrayList<>();

    //剩余生产时间
    public int product_time;
    //当前站点已经有物品生成
    public boolean havegenerated;
    public double x,y;
    // 工作台状态，（有人查找为true，没有查找为false）
    public boolean flag = false ;
    //工作台状态新版本
    public boolean[] rob_come_flag ;
    public int[] rob_come_cnt;
    

    // 表示该工作台距离两种原材料工作台最近的距离
    public double dis1, dis2, dis3, dis4;

    // 存放当前工作站的原材料工作站
    public List<Station> distance1 = new ArrayList<Station>(); // 所需的第一个原材料的工作站
    public List<Station> distance2 = new ArrayList<Station>();// 所需的第二个原材料的工作站
    public List<Station> distance3 = new ArrayList<Station>(); // 所需的第三个原材料的工作站(如果有)
    public List<Station> distance4 = new ArrayList<Station>(); // 所需的第四个原材料的工作站(如果有)
    public List<Station> distance456 = new ArrayList<Station>(); // 9类型工作站

    
    
    public int state = 0;

    public Station(){
    };

    public Station(int i , int types ,double x , double y){
        this.no = i ;
        this.types = types;
        this.x = x ;
        this.y = y ;
    }

    //此方法，方向计算这个站点还需要什么材料
    public List<Integer>  queryDemand(){
        return new ArrayList<>();
    }

    //此方法用于帧通信时，传入一个str，如"48",则在list添加4和5这两个物品
    public void find(String str){

    }
    //更新状态，参数：str1 为生产时间,str2 原始状态格,str3 产品格
    public void updateState(String str1, String str2 , String str3 ){
        state = 0;
        int time = Integer.parseInt(str1);
        setProduct_time(time);
        if(time >= 0 ){              // 有待斟酌
            state +=2;
        }
        
        find(str2);
        
        if(existgood!=null){
            for(int i = 0 ; i < existgood.length ;  i++ ){
                if(existgood[i] ==false && rob_come_cnt[i] ==0 ){
                    //要需求
                    state +=1;
                    break;
                }
            }
        }
        
//        List list = queryDemand();
//        if(list.size()!=0){
//            state +=1;
//        }
        
        if( str3.equals("1") ){
            setHavegenerated(true);
            state += 4;
        }else{
            setHavegenerated(false);
        }
        
        

    }

    //此方法用于栈帧通讯时，更改站点的

    //下面全是 get set 方法


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getNo() {
        return no;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public void setTypes(int types) {
        this.types = types;
    }
    public int getTypes() {
        return types;
    }

    public void setProduct_time(int product_time) {
        this.product_time = product_time;
    }

    public void setHavegenerated(boolean havegenerated) {
        this.havegenerated = havegenerated;
    }

    public int getProduct_time() {
        return product_time;
    }

    public boolean isHavegenerated() {
        return havegenerated;
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
    
    
//    public boolean[] getRob_come_flag() {
//        return rob_come_flag;
//    }
//
//    public void setRob_come_flag(boolean[] rob_come_flag) {
//        this.rob_come_flag = rob_come_flag;
//    }
    public void change(int carrygood){
        return;
    }
    public boolean can_sell(int carrygood){
        
        return false;
    }
    
}


