package sercandevops.com.loadmorerecyc1;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewKampanya;
    private List<Item> kampanyaModels;
    private MyAdapter kampanyaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kampanyaModels = new ArrayList<>();
        random10Data();

        recyclerViewKampanya = (RecyclerView)findViewById(R.id.recyclerviewm);
        RecyclerView.LayoutManager eng = new LinearLayoutManager(getApplicationContext());
        recyclerViewKampanya.setLayoutManager(eng);
        kampanyaAdapter = new MyAdapter(recyclerViewKampanya,getApplicationContext(),kampanyaModels,MainActivity.this);
        recyclerViewKampanya.setAdapter(kampanyaAdapter);

        kampanyaAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if(kampanyaModels.size() >= 5)
                {
                    Toast.makeText(getApplicationContext(),"Load data completd.",Toast.LENGTH_SHORT).show();
                    kampanyaModels.add(null);
                    kampanyaAdapter.notifyItemInserted(kampanyaModels.size() -1);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            kampanyaModels.remove(kampanyaModels.size() -1);
                            kampanyaAdapter.notifyItemRemoved(kampanyaModels.size());

                            int index = kampanyaModels.size();
                            int end = index + 3;
                            for(int i = index; i < end; i++)
                            {
                                String name = UUID.randomUUID().toString();
                                Item item = new Item(name,name.length());
                                kampanyaModels.add(item);
                            }
                            kampanyaAdapter.notifyDataSetChanged();
                            kampanyaAdapter.setLoaded();
                        }
                    },5000);

                    Toast.makeText(getApplicationContext(),"getiriliyor..",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Load data completd."+kampanyaModels.size(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void random10Data()
    {
        for(int i=0;i<10;i++)
        {
            String name = UUID.randomUUID().toString();
            Item item = new Item(name,name.length());
            kampanyaModels.add(item);
        }
    }
}
