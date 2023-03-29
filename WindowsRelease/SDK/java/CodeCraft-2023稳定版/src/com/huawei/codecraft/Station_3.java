package com.huawei.codecraft;

/**
 * @author xyhong
 * @create 2023-03-10-21:12
 */
public class Station_3 extends Station{
    
    public Station_3() {
        types = 3;
    }
    //输入参数1：i 代表 序号 ,  types 代表类型
    public Station_3(int i ,int types , double x , double y) {
        no = i ;
        this.types = 3;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void find(String str){
        //三号机查询物品的操作 无
        
    }
    
    
}