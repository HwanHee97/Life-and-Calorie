package com.example.life_and_calorie.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.life_and_calorie.R;
import com.example.life_and_calorie.retrofit.RetrofitClient;
import com.example.life_and_calorie.retrofit.FoodRetrofitInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//프래그먼트는 액티비티위에 올라가있을때만 프래그먼트로서 동작할 수 있다.
public class MainFragment extends Fragment {
    MainActivity activity;
    ArrayList<Food> al_food;
    ArrayAdapter<Food> aa_food;
    ListView list;
    EditText et_search;
    ImageView fragment_iv_search;
    MySQLiteOpenHelper dbHelper;
    SQLiteDatabase mdb;
    //커서
    Cursor mCursor;
    FoodRetrofitInterface server = new RetrofitClient().retrofit.create(FoodRetrofitInterface.class);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될 때는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참조가안됨
        activity = null;
    }

    @Nullable
    @Override
    //xml레이아웃인 fragment_main을 레이아웃 inflater를 이용해서 inflation해준다.(xml파일을 소스코드에서 사용할 수 있게 해준다는 말)
    //그 다음 xml을 참조해 사용할 수 있으므로 버튼에 MainActivity의 메소드를 호출하도록 설정
    //onCreatView()는 즉 MainActivity의 onCreate처럼 사용해주면 된다.
    //(xml로 짜여진 뷰들을 소스코드에 연동하고 사용)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //키보드 내리기를 위한 객체 생성
        InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //food_bookmark.db 파일이 있으면 오픈, 없으면 생성 후 오픈
        dbHelper = new MySQLiteOpenHelper(getContext(), "LifeAndCalorie.db", null, 1);
        //읽고 쓰기가 가능한 SQLiteDatabase 객체를 반환한다.
        mdb = dbHelper.getWritableDatabase();
        dbHelper.onCreate(mdb);
        //프래그먼트 메인을 인플레이트해주고 컨테이너에 붙여달라는 뜻임
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        et_search = (EditText) view.findViewById(R.id.et_search);
        fragment_iv_search = (ImageView) view.findViewById(R.id.fragment_iv_search);
        al_food = new ArrayList<Food>();
        aa_food = new FoodAdapter(getContext(), R.layout.row, R.id.tv_foodname, al_food);
        list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(aa_food);

        //ListView 클릭 시 섭취한 음식 추가하기 창
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //클릭한 아이템의 Food 가져오기
                Food tmp_food = al_food.get(i);
                //CustomDialog 객체 생성
                DialogCustom dialogCustom = new DialogCustom(getContext(), tmp_food);
                //Dialog 밖에 터치 했을 때 Dialog가 꺼짐
                dialogCustom.setCanceledOnTouchOutside(true);
                //Dialog 취소 가능(back버튼)
                dialogCustom.setCancelable(true);
                //CustomDialog의 크기설정
                dialogCustom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogCustom.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                //DialogCustom show
                dialogCustom.show();
            }
        });

        //엔터 키 누르면 음식 검색
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch(keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        search(mInputMethodManager);
                }
                return false;
            }
        });

        //찾고자 하는 음식을 입력하고 돋보기 버튼 클릭 시
        fragment_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(mInputMethodManager);
            }
        });
        return view;
    }

    public void selectFood(String sql_action, String food) {
        Call<String> call = server.selectFood(food);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    makeArraryList(response.body().toString());
                    aa_food.notifyDataSetChanged();
                } else {
                    Log.i("lhh", "실패");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("lhh", "실패" + t.getMessage());
            }
        });

    }

    public void makeArraryList(String str_json) {

        Log.i("lhh", " <<<<<onPostExecute>>>> ");
        try {
            int count = 0;
            JSONArray jarray = new JSONObject(str_json).getJSONArray("List");//result라는 json형식의 문자열을 json객체로 만들고 json배열로 만든다.
            while (jarray != null) {
                JSONObject jsonObject = jarray.getJSONObject(count);
                String FOOD = jsonObject.getString("FOOD");
                String CALORIE = jsonObject.getString("CALORIE");
                al_food.add(new Food(FOOD, CALORIE, false));//
                count++;
            }
            aa_food.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("lhh", e.getMessage());
        }
    }
    public void search(InputMethodManager mInputMethodManager){
        //키패드를 내리지 않으면 ListView가 뜨지 않아서 Fragment에서의 키보드 내리기 함수 적용
        mInputMethodManager.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        //음식 검색 입력란 텍스트 가져오기
        String input_food_name = et_search.getText().toString();
        //ArrayList 초기화
        al_food.clear();
        selectFood("select", input_food_name);

        //SELECT SQL문 수행
        mCursor = mdb.rawQuery("SELECT * FROM Bookmark", null);
        //즐겨찾기 버튼 클릭으로 DB에 저장된 음식과 검색한 음식의 al_food의 size만큼 비교
        while (mCursor.moveToNext()) {
            String foodname = mCursor.getString(0);
            String foodkcal = mCursor.getString(1);
            for (int i = 0; i < al_food.size(); i++) {
                //DB에 저장된 음식과 al_food에 추가된 음식과 같으면
                if (foodname.equals(al_food.get(i).getName()) && foodkcal.equals(al_food.get(i).getKcal())) {
                    //즐겨찾기 등록
                    al_food.get(i).setChecked(true);
                }
            }
        }
        et_search.setText("");
    }


}