package com.example.team_project.main;

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

        if (get_serialkey.equals("select")) {
            food = serialkey[1];
            urlAddr = "http://192.168.0.29:3000/select/?FOOD=" + food;//쿼리스트링 2개면 &로 이어줌

        } else if (get_serialkey.equals("insert")) {
            food = serialkey[1];
            calorie = serialkey[2];
            urlAddr = "http://192.168.0.29:3000/insert/?FOOD=" + food + "&CALORIE=" + calorie;//쿼리스트링 2개면 &로 이어줌
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
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("lhh ", e.getMessage());
        }
        return get_json;
    }

    @Override
    protected void onPostExecute(String result) {//결과를 json으로 파싱하는 코드
        super.onPostExecute(result);
    }

}