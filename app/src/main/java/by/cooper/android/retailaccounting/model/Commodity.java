package by.cooper.android.retailaccounting.model;

import android.support.annotation.NonNull;


public interface Commodity {


    String getKey();

    void setKey(@NonNull String key);

    String getBrand();

    void setBrand(@NonNull String brand);

    String getModel();

    void setModel(@NonNull String model);

    int getCount();

    void setCount(int count);

    int getPrice();

    void setPrice(int price);

    long getReceiveDate();

    void setReceiveDate(long receiveDate);

    long getSoldDate();

    void setSoldDate(long soldDate);

    String getImageUrl();

    void setImageUrl(String url);

}
