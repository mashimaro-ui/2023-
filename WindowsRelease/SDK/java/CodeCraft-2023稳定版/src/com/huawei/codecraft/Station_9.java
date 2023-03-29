package com.huawei.codecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xyhong
 * @create 2023-03-10-21:18
 */
public class Station_9 extends Station{
    public Station_9() {
        types = 9 ;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_9(int i ,int types , double x, double y) {
        
        this.no = i ;
        this.types = 9;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void find(String str){
        //九号机查询物品的操作 无操作
        
    }
    @Override
    public List<Integer> queryDemand(){
        List<Integer> list = Arrays.asList(4, 5, 6);
        return list;
    }
    @Override
    public boolean can_sell(int carrygood){


        return true;
    }
    
}