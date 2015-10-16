package by.cooper.android.retailaccounting.model;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import by.cooper.android.retailaccounting.util.CommodityContract;
import by.cooper.android.retailaccounting.util.PhoneContract;
import by.cooper.android.retailaccounting.util.UrlContract;


@Parcel
public class Phone implements Commodity {

    public static final int INCORRECT_DATE = -1;

    @JsonIgnore
    private String mKey;

    @JsonProperty(CommodityContract.BRAND)
    private String mBrand;
    @JsonProperty(CommodityContract.MODEL)
    private String mModel;
    @JsonProperty(CommodityContract.COUNT)
    private int mCount;
    @JsonProperty(CommodityContract.PRICE)
    private int mPrice;
    @JsonProperty(CommodityContract.RECEIVE_DATE)
    private long mReceiveDate;
    @JsonProperty(CommodityContract.SOLD_DATE)
    private long mSoldDate;

    @JsonProperty(PhoneContract.IMEI)
    private String mImei;
    @JsonProperty(PhoneContract.SERIAL_NUMBER)
    private String mSerialNumber;
    @JsonProperty(CommodityContract.COMMODITY_IMAGE_URL)
    private String mImageUrl;

    public static String getUrlPath() {
        return UrlContract.BASE_URL + PhoneContract.PHONE_PATH;
    }

    public Phone() {
    }

    public Phone(@NonNull String brand, @NonNull String model, long receiveDate, @NonNull String imei) {
        mKey = "";
        mBrand = brand;
        mModel = model;
        mReceiveDate = receiveDate;
        mImei = imei;
        mPrice = 0;
        mCount = 1;
        mSoldDate = INCORRECT_DATE;
    }

    @Override
    public String getKey() {
        return mKey;
    }

    @Override
    public void setKey(@NonNull String key) {
        mKey = key;
    }

    @Override
    public String getBrand() {
        return mBrand;
    }

    @Override
    public void setBrand(@NonNull String brand) {
        mBrand = brand;
    }

    @Override
    @NonNull
    public String getModel() {
        return mModel;
    }

    @Override
    public void setModel(@NonNull String model) {
        mModel = model;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public void setCount(int count) {
        mCount = count;
    }

    @Override
    public int getPrice() {
        return mPrice;
    }

    @Override
    public void setPrice(int price) {
        mPrice = price;
    }

    @Override
    public long getReceiveDate() {
        return mReceiveDate;
    }

    @Override
    public void setReceiveDate(long receiveDate) {
        mReceiveDate = receiveDate;
    }

    @Override
    public long getSoldDate() {
        return mSoldDate;
    }

    @Override
    public void setSoldDate(long soldDate) {
        mSoldDate = soldDate;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public void setImageUrl(String url) {
        mImageUrl = url;
    }

    @NonNull
    public String getImei() {
        return mImei;
    }

    public void setImei(@NonNull String imei) {
        mImei = imei;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        mSerialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "mBrand='" + mBrand + '\'' +
                ", mModel='" + mModel + '\'' +
                ", mCount=" + mCount +
                ", mPrice=" + mPrice +
                ", mReceiveDate=" + mReceiveDate +
                ", mSoldDate=" + mSoldDate +
                ", mImei='" + mImei + '\'' +
                ", mSerialNumber='" + mSerialNumber + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
