package pro.kulebyakin.commonshoppinglist.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.concurrent.ThreadLocalRandom;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import pro.kulebyakin.commonshoppinglist.R;
import pro.kulebyakin.commonshoppinglist.adapters.ProductAdapter;
import pro.kulebyakin.commonshoppinglist.data.DataGenerator;
import pro.kulebyakin.commonshoppinglist.helpers.SwipeItemTouchHelper;
import pro.kulebyakin.commonshoppinglist.models.Product;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private EditText productEditText;
    private ImageButton sendButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.shopping_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final List<Product> items = DataGenerator.getSocialData(getActivity());

        //set data and list adapter
        mAdapter = new ProductAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setDragListener(new ProductAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });

        ItemTouchHelper.Callback callback = new SwipeItemTouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        sendButton = rootView.findViewById(R.id.product_send_text);
        productEditText = rootView.findViewById(R.id.product_edit_text);
        productEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Добавить продукт по кнопке
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypedArray drw_arr = getActivity().getResources().obtainTypedArray(R.array.social_images);
                int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);

                Product obj = new Product();
                obj.image = drw_arr.getResourceId(randomNum, -1);
                obj.name = productEditText.getText().toString();
                obj.imageDrw = getActivity().getResources().getDrawable(obj.image);
                items.add(obj);
                mAdapter.notifyDataSetChanged();
                productEditText.setText("");
            }
        });



        return rootView;
    }


}
