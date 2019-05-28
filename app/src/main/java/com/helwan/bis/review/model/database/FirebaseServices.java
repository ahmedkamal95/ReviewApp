package com.helwan.bis.review.model.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.helwan.bis.review.model.dao.Brand;
import com.helwan.bis.review.model.dao.Comment;
import com.helwan.bis.review.model.dao.Item;
import com.helwan.bis.review.model.dao.MainCategoryItem;
import com.helwan.bis.review.model.dao.Search;
import com.helwan.bis.review.model.dao.User;
import com.helwan.bis.review.screens.additemscreen.AddItemContract;
import com.helwan.bis.review.screens.additemscreen.AddItemPresenterImpl;
import com.helwan.bis.review.screens.brandsscreen.BrandsContract;
import com.helwan.bis.review.screens.homescreen.HomeContract;
import com.helwan.bis.review.screens.itemdetailsscreen.ItemDetailsContract;
import com.helwan.bis.review.screens.itemsscreen.ItemsContract;
import com.helwan.bis.review.screens.searchscreen.SearchPresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseServices {

    private static FirebaseServices instance;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseDatabase database;

    private FirebaseServices() {
        database = FirebaseDatabase.getInstance();
    }

    public static FirebaseServices getInstance() {
        if (instance == null) {
            synchronized (FirebaseServices.class) {
                if (instance == null) {
                    instance = new FirebaseServices();
                }
            }
        }
        return instance;
    }

    public void insertUser(User user) {
        String userId = this.user.getUid();
        myRef = database.getReference("users").child(userId);
        myRef.setValue(user);
    }

    public void fetchMainCategoryList(HomeContract.Presenter presenter) {
        myRef = database.getReference("mainCategory");
        List<MainCategoryItem> mainCategoryItemList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mainCategoryItemList.add(snapshot.getValue(MainCategoryItem.class));
                }
                presenter.setMainCategoryList(mainCategoryItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void fetchMainCategoryList(AddItemContract.Presenter presenter) {
        myRef = database.getReference("mainCategory");
        List<MainCategoryItem> mainCategoryItemList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mainCategoryItemList.add(snapshot.getValue(MainCategoryItem.class));
                }
                presenter.setMainCategoryList(mainCategoryItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void search(SearchPresenterImpl presenter, String itemName) {
        myRef = database.getReference("search");
        List<Search> searchList = new ArrayList<>();
        myRef.orderByChild("name").startAt(itemName).endAt(itemName + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            searchList.add(snapshot.getValue(Search.class));
                        }
                        presenter.setSearchList(searchList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public void fetchBrandsList(BrandsContract.Presenter presenter, String mainCategory) {
        myRef = database.getReference("mainCategory").child(mainCategory).child("brands");
        List<Brand> brands = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    brands.add(snapshot.getValue(Brand.class));
                }
                presenter.setBrandsList(brands);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void fetchBrandsList(AddItemContract.Presenter presenter, String mainCategory) {
        myRef = database.getReference("mainCategory").child(mainCategory).child("brands");
        List<Brand> brands = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    brands.add(snapshot.getValue(Brand.class));
                }
                presenter.setBrandsList(brands);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void fetchItemsList(ItemsContract.Presenter presenter, String mainCategory, String brandName) {
        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items");
        List<Item> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    items.add(snapshot.getValue(Item.class));
                }
                presenter.setItemsList(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void fetchCommentsList(ItemDetailsContract.Presenter presenter, String mainCategory, String brandName, String itemName) {
        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items").child(itemName).child("comments");
        List<Comment> comments = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    comments.add(snapshot.getValue(Comment.class));
                }
                presenter.setCommentsList(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void insertComment(ItemDetailsContract.Presenter presenter, String mainCategory, String brandName, String itemName, String comment, int rating, Float rateCount, Float rateSum, int oldRating) {
        if (user != null) {
            String userId = user.getUid();
            Comment commentObject = new Comment(userId, user.getDisplayName(), comment, rating);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String msg;
                    float newRateSum;
                    if (oldRating != 0) {
                        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items").child(itemName).child("comments").child(userId);
                        myRef.setValue(commentObject);
                        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items").child(itemName).child("rateSum");
                        newRateSum = (rateSum - oldRating) + rating;
                        myRef.setValue(newRateSum);
                        myRef = database.getReference("search").child(itemName).child("rateSum");
                        myRef.setValue(newRateSum);
                        Log.e("update", "onDataChange: " + oldRating + "   " + rateSum);
                        msg = "Comment updated";
                    } else {
                        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items").child(itemName).child("comments").child(userId);
                        myRef.setValue(commentObject);
                        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items").child(itemName).child("rateCount");
                        myRef.setValue(rateCount + 1);
                        myRef = database.getReference("mainCategory").child(mainCategory).child("brands").child(brandName).child("items").child(itemName).child("rateSum");
                        newRateSum = rateSum + rating;
                        myRef.setValue(newRateSum);
                        myRef = database.getReference("search").child(itemName).child("rateCount");
                        myRef.setValue(rateCount + 1);
                        myRef = database.getReference("search").child(itemName).child("rateSum");
                        myRef.setValue(newRateSum);
                        Log.e("add", "onDataChange: " + rateSum);
                        msg = "Comment added";
                    }
                    presenter.updateComments(msg, comment, user.getDisplayName(), user.getUid(), newRateSum);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void insertProduct(AddItemPresenterImpl addItemPresenter, Item item, String mainCategoryName, String brandName, int itemCount) {
        myRef = database.getReference("mainCategory").child(mainCategoryName).child("brands").child(brandName).child("items").child(item.getName());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    addItemPresenter.itemExists(true);
                } else {
                    myRef = database.getReference("search").child(item.getName());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Search search1 = dataSnapshot.getValue(Search.class);
                                if (search1 != null && search1.getBrand().equals(brandName) && search1.getMainCategory().equals(mainCategoryName)) {
                                    addItemPresenter.itemExists(true);
                                } else {
                                    item.setName(item.getName() + " " + brandName);
                                    addProduct(addItemPresenter, item, mainCategoryName, brandName, itemCount);
                                }
                            } else {
                                addProduct(addItemPresenter, item, mainCategoryName, brandName, itemCount);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            addItemPresenter.toast(databaseError.getMessage(),"Error");
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                addItemPresenter.toast(databaseError.getMessage(),"Error");

            }
        });
    }

    private void addProduct(AddItemPresenterImpl addItemPresenter, Item item, String mainCategoryName, String brandName, int itemCount){
        myRef = database.getReference("mainCategory").child(mainCategoryName).child("brands").child(brandName).child("items").child(item.getName());
        myRef.setValue(item);
        Search search = new Search();
        search.setBrand(brandName);
        search.setDescription(item.getDescription());
        search.setName(item.getName());
        search.setRateCount(item.getRateCount());
        search.setRateSum(item.getRateSum());
        search.setPrice(item.getPrice());
        search.setMainCategory(mainCategoryName);
        myRef = database.getReference("search").child(item.getName());
        myRef.setValue(search);
        myRef = database.getReference("mainCategory").child(mainCategoryName).child("brands").child(brandName).child("itemCount");
        int count = itemCount + 1;
        myRef.setValue(count);

        addItemPresenter.itemExists(false);
    }

    public void insertImage(AddItemPresenterImpl addItemPresenter, String
            mainCategoryName, String brandName, String itemName, HashMap<String, String> images) {
        myRef = database.getReference("mainCategory").child(mainCategoryName).child("brands").child(brandName).child("items").child(itemName).child("images");
        myRef.setValue(images);
        myRef = database.getReference("search").child(itemName).child("images");
        myRef.setValue(images);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addItemPresenter.uploadFinish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                addItemPresenter.toast(databaseError.getMessage(),"Error");
            }
        });

    }

    public String getUserId() {
        return user.getUid();
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
