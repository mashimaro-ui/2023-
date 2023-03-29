package com.huawei.codecraft;


public class Station_1 extends Station {
    
    public Station_1() {
        types = 1;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_1(int i ,int types , double x, double y) {
        this.no = i ;
        this.types = 1;
        this.x = x;
        this.y = y;
    }
    @Override
    public void find(String str){
        //一号机查询物品的操作 无
        
    }
    
    
    
    
    
}