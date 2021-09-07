package com.example.test_db2;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class CONN_SERVER extends AsyncTask<String, Void, String> {
    StringBuffer Buffer;

    public CONN_SERVER() {

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Buffer = new StringBuffer();
    }

    @Override
    protected String doInBackground(String... serialkey) {
        String get_serialkey = serialkey[0];
        String get_json = "";
        String urlAddr = "";
        String food = "";
        String calorie = "";
        String source = "";

        if (get_serialkey.equals("select")) {
            food = serialkey[1];
            urlAddr = "http://10.0.2.2:3000/select/?FOOD=" + food;//쿼리스트링 2개면 &로 이어줌
        } else if (get_serialkey.equals("insert")) {
            food = serialkey[1];
            calorie = serialkey[2];
            source = serialkey[3];
            urlAddr = "http://10.0.2.2:3000/insert/?FOOD=" + food + "&CALORIE=" + calorie + "&SOURCE=" + source;//쿼리스트링 2개면 &로 이어줌
        }

        Log.i("lhh", "get_serialkey: " + get_serialkey);//글래스 객체 생성할때 넘기는 파라미터
        try {
            URL url = new URL(urlAddr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();// URL을 연결한 객체 생성.
            if (conn != null) {
                conn.setConnectTimeout(20000);
                conn.setUseCaches(false);// 캐싱데이터를 받을지 안받을지
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // 서버에서 읽어오기 위한 스트림 객체
                    InputStreamReader isr = new InputStreamReader(
                            conn.getInputStream());
                    // 줄단위로 읽어오기 위해 BufferReader로 감싼다.
                    BufferedReader br = new BufferedReader(isr);
                    // 반복문 돌면서읽어오기
                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        Buffer.append(line);
                    }
                    br.close();
                    conn.disconnect();
                }
            }

            get_json = Buffer.toString();
            Log.i("lhh", "get_json: " + get_json);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("lhh ", e.getMessage());
        }
        return get_json;
    }

//    @Override
//    protected void onCancelled(String s) {
//        super.onCancelled(s);
//        Log.e("lhh ", s);
//
//    }

    @Override
    protected void onPostExecute(String result) {//결과를 json으로 파싱하는 코드
        super.onPostExecute(result);
//        //tv.setText(result);
//        Log.i("lhh", " <<<<<onPostExecute>>>> ");
//        try {
//            int count = 0;
//            JSONArray jarray = new JSONObject(result).getJSONArray("List");//result라는 json형식의 문자열을 json객체로 만들고 json배열로 만든다.
//            while (jarray != null) {
//                JSONObject jsonObject = jarray.getJSONObject(count);
////
//                String FOOD = jsonObject.getString("FOOD");//키값 맞춰서 읽어들임
//                String CALORIE = jsonObject.getString("CALORIE");
//                String SOURCE = jsonObject.getString("SOURCE");
//
//                // null을 가끔 못 읽어오는 때가 있다고 하기에 써봄
//                //getString의 경우 키에 해당하는 값이 없는 경우 JsonException을 발생시키는 반면 optString은 ""와 같은 빈 문자열을 반환한다.
//                //String REG = jsonObject.optString("REG_TIME", "text on no value");
//                Log.i("lhh", FOOD + "/" + CALORIE + "/" + SOURCE);
//                count++;
//            }
//        } catch (Exception e) {
//            Log.e("lhh", e.getMessage());
//        }
    }

}