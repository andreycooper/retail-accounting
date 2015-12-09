package by.cooper.android.retailaccounting.model;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import by.cooper.android.retailaccounting.util.CommodityContract;
import by.cooper.android.retailaccounting.util.PhoneContract;
import by.cooper.android.retailaccounting.util.UrlContract;


@Parcel(Parcel.Serialization.BEAN)
public class Phone implements Commodity {

    public static final int DEFAULT_DATE = 0;
    public static final String EMPTY = "";

    @JsonIgnore
    private String key;

    @JsonProperty(CommodityContract.BRAND)
    private String brand;
    @JsonProperty(CommodityContract.MODEL)
    private String model;
    @JsonProperty(CommodityContract.COUNT)
    private int count;
    @JsonProperty(CommodityContract.PRICE)
    private int price;
    @JsonProperty(CommodityContract.RECEIVE_DATE)
    private long receiveDate;
    @JsonProperty(CommodityContract.SOLD_DATE)
    private long soldDate;
    @JsonProperty(PhoneContract.IMEI)
    private String imei;
    @JsonProperty(PhoneContract.SERIAL_NUMBER)
    private String serialNumber;
    @JsonProperty(CommodityContract.COMMODITY_IMAGE_URL)
    private String imageUrl;

    public static String getUrlPath() {
        return UrlContract.BASE_URL + PhoneContract.PHONE_PATH;
    }

    public Phone() {
        // This code style and empty initialization is for Jackson serializer :(
        key = EMPTY;
        brand = EMPTY;
        model = EMPTY;
        serialNumber = EMPTY;
        imei = EMPTY;
        count = 1;
        price = 0;
        receiveDate = DEFAULT_DATE;
        soldDate = DEFAULT_DATE;
        imageUrl = EMPTY;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(@NonNull String key) {
        this.key = key;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(@NonNull String brand) {
        this.brand = brand;
    }

    @Override
    @NonNull
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(@NonNull String model) {
        this.model = model;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public long getReceiveDate() {
        return receiveDate;
    }

    @Override
    public void setReceiveDate(long receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Override
    public long getSoldDate() {
        return soldDate;
    }

    @Override
    public void setSoldDate(long soldDate) {
        this.soldDate = soldDate;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String url) {
        imageUrl = url;
    }

    @NonNull
    public String getImei() {
        return imei;
    }

    public void setImei(@NonNull String imei) {
        this.imei = imei;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", receiveDate=" + receiveDate +
                ", soldDate=" + soldDate +
                ", imei='" + imei + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
