package pro.kulebyakin.commonshoppinglist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import pro.kulebyakin.commonshoppinglist.R;
import pro.kulebyakin.commonshoppinglist.helpers.DragItemTouchHelper;
import pro.kulebyakin.commonshoppinglist.helpers.SwipeItemTouchHelper;
import pro.kulebyakin.commonshoppinglist.helpers.ProductTouchHelper;
import pro.kulebyakin.commonshoppinglist.models.Product;
import pro.kulebyakin.commonshoppinglist.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.OriginalViewHolder>
        implements SwipeItemTouchHelper.SwipeHelperAdapter, DragItemTouchHelper.MoveHelperAdapter {

    private List<Product> items = new ArrayList<>();
    private List<Product> items_swiped = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnStartDragListener mDragStartListener = null;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options,
                          Context context, List<Product> items) {
        super(options);
        this.items = items;
        ctx = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

//    public ProductAdapter(Context context, List<Product> items) {
//        this.items = items;
//        ctx = context;
//    }

    public void setDragListener(OnStartDragListener dragStartListener) {
        this.mDragStartListener = dragStartListener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements SwipeItemTouchHelper.TouchViewHolder {
        public ImageView image;
        public TextView name;
        public TextView quantity;
        public TextView price;
        public Button bt_undo;
        public View lyt_parent;
        public View ll;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            quantity = (TextView) v.findViewById(R.id.quantity);
            price = (TextView) v.findViewById(R.id.price);
            bt_undo = (Button) v.findViewById(R.id.bt_undo);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            ll = (View) v.findViewById(R.id.ll);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(ctx.getResources().getColor(R.color.grey_5));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @NonNull
    @Override
    public OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                for (Product s : items_swiped) {
                    int index_removed = items.indexOf(s);
                    if (index_removed != -1) {
                        items.remove(index_removed);
                        notifyItemRemoved(index_removed);
                    }
                }
                items_swiped.clear();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected void onBindViewHolder(@NonNull final OriginalViewHolder holder, final int position, @NonNull Product model) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final Product p = items.get(position);
            view.name.setText(p.name);
            view.quantity.setText(Double.toString(p.quantity));
            view.price.setText(Double.toString(p.price) + " р");
//            Tools.displayImageOriginal(ctx, view.image, p.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.bt_undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(position).swiped = false;
                    items_swiped.remove(items.get(position));
                    notifyItemChanged(position);
                }
            });

            if (p.swiped) {
                view.lyt_parent.setVisibility(View.GONE);
            } else {
                view.lyt_parent.setVisibility(View.VISIBLE);
            }

            view.lyt_parent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && mDragStartListener != null) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getRef().removeValue();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(items, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
//         handle when double swipe
        if (items.get(position).swiped) {
            items_swiped.remove(items.get(position));
            items.remove(position);
            deleteItem(position);
            notifyItemRemoved(position);
            return;
        }
        items.get(position).swiped = true;
        items_swiped.add(items.get(position));
        notifyItemChanged(position);
    }
}
