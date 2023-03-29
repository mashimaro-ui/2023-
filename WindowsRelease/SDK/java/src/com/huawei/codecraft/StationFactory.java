package com.huawei.codecraft;

/**
 * @author xyhong
 * @create 2023-03-10-21:18
 */
public class StationFactory {
    
    public static Station creatStation(int i , int types,double x, double y) {
        Station station = null;
    
        switch (types) {
            case 1:
                station = new Station_1(i, types, x, y);
                break;
            case 2:
                station = new Station_2(i, types, x, y);
                break;
            case 3:
                station = new Station_3(i, types, x, y);
                break;
            case 4:
                station = new Station_4(i, types, x, y);
                break;
            case 5:
                station = new Station_5(i, types, x, y);
                break;
            case 6:
                station = new Station_6(i, types, x, y);
                break;
            case 7:
                station = new Station_7(i, types, x, y);
                break;
            case 8:
                station = new Station_8(i, types, x, y);
                break;
            case 9:
                station = new Station_9(i, types, x, y);
                break;
            default:
//                station = new Station(i, types, x, y);
                break;
        }
        return station;
    
    }
    
}