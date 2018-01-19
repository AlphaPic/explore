package com.fan.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author:fanwenlong
 * @date:2018-01-18 13:41:45
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:把json数据写入数据库
 * @detail:
 */
public class JsonToDb {
    private String Dir = "E:\\data\\git\\datasource\\";

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://10.115.1.223:3306/monitor";

    static final String USER = "root";
    static final String PASS = "xiwang";

    int index = 0;
    static Connection connection = null;

    static {
        try {
            Class.forName(JDBC_DRIVER);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * 内部类,表示地址
     */
    static class  Address{
        private String country;
        private String province;
        private String city;
        private String postCode;

        public Address(String country, String province, String city, String postCode) {
            this.country = country;
            this.province = province;
            this.city = city;
            this.postCode = postCode;
        }

        public Address() {
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"country\":\"")
                    .append(country).append('\"');
            sb.append(",\"province\":\"")
                    .append(province).append('\"');
            sb.append(",\"city\":\"")
                    .append(city).append('\"');
            sb.append(",\"postCode\":\"")
                    .append(postCode).append('\"');
            sb.append('}');
            return sb.toString();
        }

        public void clear(){
            province = null;
            city     = null;
            postCode = null;
        }

        /**
         * 判断是不是存在空成员
         * @return
         */
        public boolean containEmptyMember(){
            if(country == null          || country.length() <= 0 ||
                    province == null    || province.length() <= 0 ||
                    city == null        || city.length() <= 0 ||
                    postCode == null    || postCode.length() <= 0 ){
                return true;
            }
            return false;
        }
    }

    /**
     * 把Address对象持久化到MySql数据库中
     * @param address
     */
    private void saveToDb(Address address){
        if(address.containEmptyMember() == true){
            return;
        }


        Statement stmt        = null;
        try{

            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = connection.createStatement();

            String insert = "insert into addressinfo " +
                    "VALUES (" + index + ",'" + address.getCountry() + "','" + address.getProvince() + "','" + address.getCity() + "','" + address.getPostCode() + "')";

            System.out.println(insert);
            stmt.executeUpdate(insert);
            index++;
        }catch (Exception e){
            System.out.println("写入数据出错" + e.getMessage());
            index++;
            return;
        }finally {
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把json文件中的数据写入数据库
     */
    public void toDB(){
        String jsonString = null;
        /** 1.把文件中的数据全部写入到内存 */
        RandomAccessFile file = null;
        FileChannel channel = null;
        try {
            file = new RandomAccessFile(Dir + "province.txt","rw");
            channel = file.getChannel();

            System.out.println(file.length());

            byte[] dest = new byte[(int) file.length() * 2];

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesNum = channel.read(buffer);

            int i = 0;
            while(bytesNum > 0){
                buffer.flip();

                while (buffer.hasRemaining()){
                    dest[i++] = buffer.get();
                }

                buffer.clear();
                bytesNum = channel.read(buffer);
            }

            jsonString = new String(dest,"UTF-8");
        } catch (Exception e){
            e.printStackTrace();
            return;
        }finally {
            if(channel != null) {
                try {
                    channel.close();    //使用之后关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(file != null) {
                try {
                    file.close();       //使用之后关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(jsonString == null || jsonString.length() <= 0){
            System.out.println("解析出来的json字符串不合法");
            return;
        }


        try {
            JSONObject object = (JSONObject) JSONObject.parse(jsonString);
            Set set = object.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                HashMap.Entry<String,Object> data = (HashMap.Entry<String,Object>) iterator.next();
                if(data.getKey().equals("data")){
                    JSONObject listObject = (JSONObject) data.getValue();
                    JSONArray array = (JSONArray) listObject.get("tree");
                    /** 开始解析地址 */
                    for(int i = 0;i < array.size();i++){
                        JSONObject countryObject = (JSONObject) array.get(i);
                        if(countryObject.size() == 3) {
                            Set countrySet = countryObject.entrySet();

                            Object[] countryArr = countrySet.toArray();
                            Address address = new Address();
                            address.setCountry("中国");
                            for (int k = countryArr.length - 1;k >= 0;k--) {
                                HashMap.Entry<String, Object> addressList = (HashMap.Entry<String, Object>) countryArr[k];
                                String key = addressList.getKey();
                                if("value".equals(key)){
                                    address.setProvince((String) addressList.getValue());
                                }else if("list".equals(key)){
                                    JSONArray cityArray = (JSONArray) addressList.getValue();
                                    for(int j = 0;j < cityArray.size();j++){
                                        JSONObject jsonObject = (JSONObject) cityArray.get(j);
                                        Set citySet = jsonObject.entrySet();
                                        Iterator iterator2 = citySet.iterator();
                                        while (iterator2.hasNext()){
                                            HashMap.Entry<String, Object> citys = (HashMap.Entry<String, Object>) iterator2.next();
                                            String cityValue = (String) citys.getValue();
                                            if(cityValue.contains("请选择") || cityValue == null || cityValue.length() <= 0){
                                                continue;
                                            }
                                            String postCodeRegex = "^[A-Za-z0-9]*$";
                                            Pattern pattern = Pattern.compile(postCodeRegex);
                                            if(pattern.matcher(cityValue).matches()){
                                                address.setPostCode(cityValue);
                                            }else{
                                                address.setCity(cityValue);
                                            }
                                        }
                                        saveToDb(address);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("处理json字符串的时候出现错误");
        }
    }

    public static void main(String[] args){
//        JsonToDb.Address address = new Address("中国","中国","中国","中国");
//        new JsonToDb().saveToDb(address);
        new JsonToDb().toDB();
    }
}
