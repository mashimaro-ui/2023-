package com.huawei.codecraft;

/**
 * @author xyhong
 * @create 2023-03-10-21:12
 */
public class Station_2 extends Station{
    public Station_2() {
        types = 2;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_2(int i ,int types , double x , double y) {
        this.no = i ;
        this.types = 2;
        this.x = x;
        this.y = y;
        
    }
    @Override
    public void find(String str){
        //二号机查询物品的操作 无
        
    }
    
    
    
}