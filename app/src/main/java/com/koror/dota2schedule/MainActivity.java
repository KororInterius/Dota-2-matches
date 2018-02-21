package com.koror.dota2schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.listView)
    ListView listView;
    Elements content,dataElements;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> dataArrayList = new ArrayList<>();
    ArrayList<String> urlArrayList = new ArrayList<>();
    SimpleAdapter simpleAdapter;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    final String ATTRIBUTE_DATE = "date";
    final String ATTRIBUTE_TEXT1 = "text1";
    final String ATTRIBUTE_TEXT2 = "text2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new NewThread().execute();
        String[] from = {ATTRIBUTE_DATE,ATTRIBUTE_TEXT1,ATTRIBUTE_TEXT2};
        int[] to ={R.id.dataTextView, R.id.textView1, R.id.textView2};
        simpleAdapter = new SimpleAdapter(this,data,R.layout.list_item,from,to);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,MatchActivity.class);
                intent.putExtra("uri",("https://dota2lounge.com/"+urlArrayList.get(position)));
                startActivity(intent);
            }
        });
    }

    private class NewThread extends AsyncTask<String, Void, String >
    {
        @Override
        protected String doInBackground(String... params) {
            Document doc;
            try{
                doc = Jsoup.connect("https://dota2lounge.com/").get();
                dataElements = doc.select(".matchmain");
                content = doc.select(".teamtext");
                arrayList.clear();
                for (Element element: content)
                {
                    try{
                    arrayList.add(element.text());
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                for (Element element: dataElements)
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        c.setTime(sdf.parse(element.select(".match-time").text()));
                        c.add(Calendar.HOUR,2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try{
                        dataArrayList.add(sdf.format(c.getTime()));
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    urlArrayList.add(element.getElementsByTag("a").attr("href"));
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            Map<String, Object> elemMap ;
            int k =0;
            int j=0; // переменная для прохода по названиям команд она будет в два раза больше чем количество дат
            while(k<=dataArrayList.size()-1)
            {
                elemMap = new HashMap<>();
                elemMap.put(ATTRIBUTE_DATE,dataArrayList.get(k));
                elemMap.put(ATTRIBUTE_TEXT1,arrayList.get(j));
                elemMap.put(ATTRIBUTE_TEXT2,arrayList.get(j+1));
                k++;
                j+=2;
                data.add(elemMap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(simpleAdapter);
        }
    }
}
