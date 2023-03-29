#!/bin/sh
cd "$( dirname "$0" )"

fee1=`./Robot.exe -f -m ./maps/1.txt -c ./SDK/java/src "java com.huawei.codecraft.Main"   `
fee2=`./Robot.exe -f -m ./maps/2.txt -c ./SDK/java/src "java com.huawei.codecraft.Main"   `
fee3=`./Robot.exe -f -m ./maps/3.txt -c ./SDK/java/src "java com.huawei.codecraft.Main"   `
fee4=`./Robot.exe -f -m ./maps/4.txt -c ./SDK/java/src "java com.huawei.codecraft.Main"   `
n1=`echo $fee1 | sed 's/[^0-9]//g'`
n2=`echo $fee2 | sed 's/[^0-9]//g'`
n3=`echo $fee3 | sed 's/[^0-9]//g'`
n4=`echo $fee4 | sed 's/[^0-9]//g'`

echo "map1:$n1">>新快速结果.txt
echo "map2:$n2">>新快速结果.txt
echo "map3:$n3">>新快速结果.txt
echo "map4:$n4">>新快速结果.txt
s1=$(($n1+$n2))
s2=$(($n3+$n4))
s3=$(($s1+$s2))
echo "total:$s3">>新快速结果.txt
pause