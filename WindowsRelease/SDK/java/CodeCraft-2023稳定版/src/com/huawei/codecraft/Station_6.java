package com.huawei.codecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xyhong
 * @create 2023-03-10-21:15
 */
public class Station_6 extends Station{
    public Station_6() {
        types = 6;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_6(int i ,int types , double x, double y) {
        this.no = i ;
        this.types = 6;
        this.x = x;
        this.y = y;
        existgood = new boolean[2];
        rob_come_flag = new boolean[]{false,false} ;
        rob_come_cnt = new int[]{0,0,0};
    }
    
    //此站点的existgood = [2号材料，3号材料]
    @Override
    public void find(String str){
        int num = Integer.parseInt(str);
        //六号机查询物品的操作
        if((num>>2&1) == 1){
            existgood[0] = true;
        }else{
            existgood[0] = false;
        }
        
        if((num>>3&1) == 1){
            existgood[1] = true;
        }else{
            existgood[1] = false;
        }
    }
    
    //重写  需要材料 的方法
    @Override
    public List<Integer> queryDemand(){
        List<Integer> list = new ArrayList<>();
//        if(!  (existgood[0] |rob_come_flag[0] )){
        if(!  (existgood[0] |rob_come_cnt[0]>0 )){
            list.add(2);
        }
//        if(! (existgood[1] | rob_come_flag[1])){
        if(! (existgood[1] | rob_come_cnt[1]>0  )){
            list.add(3);
        }
    
        return list;
    }
    
    @Override
    public void change(int carrygood){
        if(carrygood==2 ){
//            this.rob_come_flag[0] = false;
            this.rob_come_cnt[0]  --;
        }
        if(carrygood==3 ){
//            this.rob_come_flag[1] = false;
            this.rob_come_cnt[1]  --;
        }
        
    }
    
    @Override
    public boolean can_sell(int carrygood){
        if(carrygood== 2){
            return existgood[0]?  false : true  ;
        }
        if(carrygood== 3){
            return existgood[1]?  false : true  ;
        }
        
        return false;
    }
    
    
}