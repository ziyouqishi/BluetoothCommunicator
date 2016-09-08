package tools;

/**
 * Created by 张佳亮 on 2016/8/29.
 * 字符串的处理
 */
public class DisplayString {
    public String string;
    public  String temp,damp,light,window;

    public String getData() {
        return string;
    }

    public void setData(String str) {
        this.string = str;
    }

    /**
     * 判断是否符合格式
     * @param str
     * @return
     */
    public boolean isRight(String str){
        if((str.length()==24)&&str.startsWith("0xaa0xff")){
            return true;
        }else {
            return false;
        }
    }

    public void separate(){
        temp=string.substring(8, 12);
        damp=string.substring(12, 16);
        light=string.substring(16, 20);
        window=string.substring(20,24);
    }

    public String   getTempAndDamp(String data){
        String newData=data.substring(2, 4);
        if(newData.startsWith("0")){
            return newData.substring(1);
        }else {
            return newData ;
        }
    }

    public String getState(String data){
        if(data.contains("00")){
            return "0";
        }else if (data.contains("01")) {
            return "1";
        }else {
            return "error";
        }
    }


}
