package pro.kulebyakin.commonshoppinglist.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import pro.kulebyakin.commonshoppinglist.R;
import pro.kulebyakin.commonshoppinglist.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> list;

    public ProductAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int position) {
        Product product = list.get(position);

        productViewHolder.nameTextView.setText(product.productName);
        productViewHolder.scoreTextView.setText(Integer.toString(product.unitPrice));
        productViewHolder.ratingTextView.setText((position + 1) + ".");

        if (product.userImageURL != null) {
            Picasso.get().load(product.userImageURL).into(productViewHolder.userImage);
        } else {
            productViewHolder.userImage.setImageResource(R.drawable.horse);
        }

        productViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(productViewHolder.getAdapterPosition(), 0, 0, "Удалить");
                menu.add(productViewHolder.getAdapterPosition(), 1, 0, "Изменииь");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView ratingTextView, nameTextView, scoreTextView;
        ImageView userImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingTextView = itemView.findViewById(R.id.rating_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            scoreTextView = itemView.findViewById(R.id.score_text_view);
            userImage = itemView.findViewById(R.id.user_image);

        }
    }

}

