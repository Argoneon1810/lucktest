package ark.noah.lucktest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<RecyclerItem> items;
    private HashMap<Integer, Bitmap> bitmaps = new HashMap<>();

    private OnItemClickEventListener mItemClickListener;

    public MyAdapter(ArrayList<RecyclerItem> items) {
        this.items = items;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for (RecyclerItem item : items) {
            Objects.requireNonNull(bitmaps.get(item.color.toArgb())).recycle();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_view_item, parent, false) ;
        MyAdapter.MyViewHolder vh = new MyViewHolder(view);

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int color = items.get(position).color.toArgb();
        Bitmap current;
        if(bitmaps.containsKey(color)) {
            current = bitmaps.get(color);
        } else {
            current = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(current);
            canvas.drawColor(color);
            bitmaps.put(color, current);
        }
        holder.imgView.setImageBitmap(current);
        holder.imgView.setOnLongClickListener(v -> {
            mItemClickListener.onItemClick(v, position);
            return false;
        });
        if(items.get(position).isIndiv) holder.ring.setVisibility(View.VISIBLE);
        else holder.ring.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(int color, boolean isIndiv, float givenValue, float valueRangedFrom, float valueRangedTo, boolean headInclusive, boolean tailInclusive, RandomMode randomMode) {
        items.add(new RecyclerItem(color, isIndiv, givenValue, valueRangedFrom, valueRangedTo, headInclusive, tailInclusive, randomMode));
        notifyItemInserted(items.size()-1);
    }
    public void add(RecyclerItem item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    public RecyclerItem get(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        ImageView ring;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgView);
            ring = itemView.findViewById(R.id.ring);
        }
    }

    public interface OnItemClickEventListener {
        void onItemClick(View a_view, int a_position);
    }
}
