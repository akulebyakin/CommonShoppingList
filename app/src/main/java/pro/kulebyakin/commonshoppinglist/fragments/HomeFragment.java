package pro.kulebyakin.commonshoppinglist.fragments;

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

import java.util.ArrayList;

import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import pro.kulebyakin.commonshoppinglist.R;
import pro.kulebyakin.commonshoppinglist.adapters.ProductAdapter;
import pro.kulebyakin.commonshoppinglist.data.DataGenerator;
import pro.kulebyakin.commonshoppinglist.helpers.SwipeItemTouchHelper;
import pro.kulebyakin.commonshoppinglist.models.Product;

public class HomeFragment extends Fragment {
    private static final String TAG = "Products";
    public static final String PRODUCTS_CHILD = "products";
    public static final String ANONYMOUS = "anonymous";

    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private EditText productEditText;
    private EditText productEditQuantity;
    private EditText productEditPrice;
    private ImageButton sendButton;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Product> productList;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private boolean isOffline = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (myRef == null) {
            database = FirebaseDatabase.getInstance();
            // Enabling Offline
//            database.setPersistenceEnabled(true);
            myRef = database.getReference(PRODUCTS_CHILD);
        }

        recyclerView = rootView.findViewById(R.id.shopping_list_view);

        productList = new ArrayList<>();

        setUpRecyclerView();
//        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int friendlyMessageCount = mAdapter.getItemCount();
//                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                // If the recycler view is initially being loaded or the
//                // user is at the bottom of the list, scroll to the bottom
//                // of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (friendlyMessageCount - 1) &&
//                                lastVisiblePosition == (positionStart - 1))) {
//                    recyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });

        //set data and list adapter
        recyclerView.setAdapter(mAdapter);

        mAdapter.setDragListener(new ProductAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });


        sendButton = rootView.findViewById(R.id.product_send_text);
        productEditText = rootView.findViewById(R.id.product_edit_text);
        productEditQuantity = rootView.findViewById(R.id.product_edit_quantity);
        productEditPrice = rootView.findViewById(R.id.product_edit_price);
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
                Product obj = new Product();
//                obj.image = drw_arr.getResourceId(randomNum, -1);
                obj.position = 1;
                obj.name = productEditText.getText().toString();
                obj.quantity = Double.parseDouble(productEditQuantity.getText().toString());
                obj.price = Double.parseDouble(productEditPrice.getText().toString());
//                obj.imageDrw = getActivity().getResources().getDrawable(obj.image);
//                items.add(0, obj);
//                mAdapter.notifyDataSetChanged();

                // push to DB
                myRef.push().setValue(obj);
                // clear edittext
                productEditText.setText("");
                productEditQuantity.setText("");
                productEditPrice.setText("");
            }
        });


        updateList();
        return rootView;
    }

    private void setUpRecyclerView() {
        final List<Product> items = DataGenerator.getSocialData(getActivity());
        Query query = myRef;
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        mAdapter = new ProductAdapter(options, getActivity(), productList);
        recyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        ItemTouchHelper.Callback callback = new SwipeItemTouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void updateList() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productList.add(dataSnapshot.getValue(Product.class));
                mAdapter.notifyDataSetChanged();
                //checkIfEmpty();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product message = dataSnapshot.getValue(Product.class);
                int index = getItemIndex(message);

                productList.set(index, message);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                Product product = dataSnapshot.getValue(Product.class);
//                int index = getItemIndex(product);
//
//                productList.remove(index);
//                mAdapter.notifyDataSetChanged();
                //checkIfEmpty();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Ищем индекс
    private int getItemIndex(Product product) {
        int index = -1;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().equals(product.getName())) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
