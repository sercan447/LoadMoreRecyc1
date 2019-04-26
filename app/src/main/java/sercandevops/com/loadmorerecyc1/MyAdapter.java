package sercandevops.com.loadmorerecyc1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Item> itemList;
    Activity activity;
    View  v;

    private final int VIEW_TYPE_ITEM=0,
                      VIEW_TYPE_LOADING=1;
    ILoadMore loadMore;
    boolean isLoading;
    int visibleThreshold=5;
    int lastVisibleItem,totalItemCount;


    public MyAdapter(RecyclerView recyclerView, Context context, List<Item> itemList, Activity activity) {
        this.context = context;
        this.itemList = itemList;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount =  linearLayoutManager.getItemCount();
               // lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold))
                {
                    if(loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });

    }

    public MyAdapter(Context context, List<Item> itemList, Activity activity) {
        this.context = context;
        this.itemList = itemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i == VIEW_TYPE_ITEM)
        {
            v = LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);

            return new MyViewHolder(v);
        }else if(i == VIEW_TYPE_LOADING)
        {
          v = LayoutInflater.from(context).inflate(R.layout.item_loading,viewGroup,false);
            LoadingViewHolder loadingViewHolder = new LoadingViewHolder(v);

            return loadingViewHolder;
        }
        return null;

    }
    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) == null ? VIEW_TYPE_LOADING  : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder myViewHolder, final int i) {

        if(myViewHolder instanceof MyViewHolder)
        {
            ((MyViewHolder) myViewHolder).txtName.setText(""+itemList.get(i).getName());
            ((MyViewHolder) myViewHolder).txtLength.setText(""+itemList.get(i).getLength());

    }else if(myViewHolder instanceof LoadingViewHolder)
    {
        LoadingViewHolder loadingViewHolder = (LoadingViewHolder)myViewHolder;
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName,txtLength;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtLength = itemView.findViewById(R.id.txtLength);


        }
    }//CLASS


    public class LoadingViewHolder extends RecyclerView.ViewHolder
    {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView)
        {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
        }

    }//CLASS
}
