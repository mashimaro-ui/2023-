package com.huawei.codecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xyhong
 * @create 2023-03-10-21:17
 */
public class Station_8 extends Station{
    public Station_8() {
        types = 8;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_8(int i ,int types , double x , double y) {
        this.no = i ;
        this.types = 8;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void find(String str){
        //八号站点 无操作
        
        
    }
    
    //重写  需要材料 的方法
    @Override
    public List<Integer> queryDemand(){
        List<Integer> list = new ArrayList<>();
        list.add(7);
        
        return list;
    }
    
    @Override
    public boolean can_sell(int carrygood){
        if(carrygood== 7){
            return true;
        }
        
        return false;
    }
    
}