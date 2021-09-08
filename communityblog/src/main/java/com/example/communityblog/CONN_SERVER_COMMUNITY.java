package com.example.communityblog;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class CONN_SERVER_COMMUNITY extends AsyncTask<String, Void, String> {
    StringBuffer Buffer;

    public CONN_SERVER_COMMUNITY() {

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
        String _ID = "";
        String title = "";
        String text = "";
        String date = "";
        String writer_ID = "";
        if (get_serialkey.equals("show")) {
            //index = Integer.parseInt(serialkey[1]);
            urlAddr = "http://10.0.2.2:3000/community/show";
        }//쿼리스트링 2개면 &로 이어줌
         else if (get_serialkey.equals("community_insert")) {
            title = serialkey[1];
            text = serialkey[2];
            writer_ID = serialkey[3];
            urlAddr = "http://10.0.2.2:3000/community/insert/?TITLE=" + title + "&TEXT=" + text + "&WRITER=" + writer_ID;//쿼리스트링 2개면 &로 이어줌
        }else if (get_serialkey.equals("comment_show")){
             _ID=serialkey[1];
            urlAddr = "http://10.0.2.2:3000/community/comment/?ID=" + _ID ;//쿼리스트링 2개면 &로 이어줌
        }

        Log.i("lhh", "get_serialkey(action): " + get_serialkey);//글래스 객체 생성할때 넘기는 파라미터
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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}