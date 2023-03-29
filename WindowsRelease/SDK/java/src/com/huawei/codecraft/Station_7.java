package com.huawei.codecraft;

import java.util.ArrayList;
import java.util.List;

import static com.huawei.codecraft.Main.global456;

/**
 * @author xyhong
 * @create 2023-03-10-21:16
 */
public class Station_7 extends Station{
    
    public Station_7() {
        types = 7;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_7(int i ,int types ,double x, double y) {
        this.no = i ;
        this.types = 7;
        this.x = x;
        this.y = y;
        existgood = new boolean[3];
        rob_come_flag = new boolean[]{false,false,false} ;
        rob_come_cnt = new int[]{0,0,0};
    }
    //此站点的existgood = [4号材料，5号材料,6号材料]
    @Override
    public void find(String str){
        int num = Integer.parseInt(str);
        //七号机查询物品的操作
        if((num>>4 &1) == 1){
            existgood[0] = true;
        }else{
            existgood[0] = false;
        }
        
        if((num>>5&1) == 1){
            existgood[1] = true;
        }else{
            existgood[1] = false;
        }
    
        if((num>>6&1) == 1){
            existgood[2] = true;
        }else{
            existgood[2] = false;
        }
    }
    
    //重写  需要材料 的方法
    @Override
    public List<Integer> queryDemand(){
        
        int global4 = global456[0];
        int global5 = global456[1];
        int global6 = global456[2];
        
        int min = (global4 < global5 ? global4 : global5) < global6 ? (global4 < global5 ? global4 : global5) : global6;
        if(min == global4){
            List<Integer> list = new ArrayList<>();
            if(!  (existgood[0] | rob_come_cnt[0] >0 )) {
                list.add(4);
            }
            if(! (existgood[1] | rob_come_cnt[1] > 0 )){
                list.add(5);
            }

            if(! (existgood[2] | rob_come_cnt[2] > 0 )){
                list.add(6);
            }
            return list;
        }
        if(min == global5){
            List<Integer> list = new ArrayList<>();
            if(! (existgood[1] | rob_come_cnt[1] > 0 )){
                list.add(5);
            }
            if(!  (existgood[0] | rob_come_cnt[0] >0 )){
                list.add(4);
            }
            
            if(! (existgood[2] | rob_come_cnt[2] > 0 )){
                list.add(6);
            }
            return list;
            
        }
        if(min == global6){
            List<Integer> list = new ArrayList<>();
            if(! (existgood[2] | rob_come_cnt[2] > 0 )){
                list.add(6);
            }
            if(!  (existgood[0] | rob_come_cnt[0] >0 )){
                list.add(4);
            }
            if(! (existgood[1] | rob_come_cnt[1] > 0 )){
                list.add(5);
            }
            
            return list;
        }
    
    
        return new ArrayList<>();
    }
    
    @Override
    public void change(int carrygood){
        if(carrygood==4){
//            this.rob_come_flag[0] = false;
            this.rob_come_cnt[0] --;
        }
        if(carrygood==5){
//            this.rob_come_flag[1] = false;
            this.rob_come_cnt[1] --;
        }
        if(carrygood==6){
//            this.rob_come_flag[2] = false;
            this.rob_come_cnt[2] --;
        }
        
    }
    
    
    @Override
    public boolean can_sell(int carrygood){
        if(carrygood== 4){
            return existgood[0]?  false : true  ;
        }
        if(carrygood== 5){
            return existgood[1]?  false : true  ;
        }
        if(carrygood== 6){
            return existgood[2]?  false : true  ;
        }
        
        return false;
    }
    
    
}