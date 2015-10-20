package com.example.myapplication3.app.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.Organization;

import java.util.List;

/**
 * Created by sasha on 15.10.2015.
 */
public class DrawView extends View {
    private Organization organization;
    private String title, city, region;
    private Paint paint;
    private List<Currency> currencies;
    private int height = 33;
    private int wightCanvas;
    private int heightCanvas;



    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        paint = new Paint();
    }

    public  DrawView(Context context) {
        super(context);
        paint = new Paint();
    }

    public void setName(String name) {
        title = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        currencies = currencyList;
    }
    //    public DrawView(Context context, GlobalModel globalModel, int position, int wightDisplay) {
//        super(context);
//        this.wightCanvas = (int) (wightDisplay * 0.7);
//                getDataFromModel(globalModel, position);
//
//        paint = new Paint();
//    }
//    private void getDataFromModel(GlobalModel globalModel, int position) {
//        organization = globalModel.getOrganizations().get(position);
//        title = organization.getTitle();
//        city = Constants.getRealName(globalModel.getCitiesReal(), organization.getCityId());
//        region = Constants.getRealName(globalModel.getRegionsReal(), organization.getRegionId());
//        q = 220 + (80 * organization.getCurrenciesReal().size());
//        if (!city.contains(region)) q += 50;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        wightCanvas = 600;
        heightCanvas = 700;
        Log.d("qqq","wi: " +widthMeasureSpec);
        Log.d("qqq","hi: " +heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        height = heightCanvas / 20;
        canvas.drawColor(getResources().getColor(R.color.color_of_cardView));

        paint.setColor(getResources().getColor(R.color.black));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(getResources().getDimension(R.dimen.text_size_name_of_bank));
        canvas.drawText(title, (wightCanvas / 10), height, paint);

        paint.setTextSize(getResources().getDimension(R.dimen.text_size_information));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(region, (wightCanvas / 11), height += height, paint);

        if (!city.contains(region))
            canvas.drawText(city, (wightCanvas / 11), height += height, paint);

        paint.setTextSize(getResources().getDimension(R.dimen.text_size_name_of_bank));

        for (Currency currency : currencies) {
            String ask = currency.getAsk().substring(0, 5);
            String bid = currency.getBid().substring(0, 5);

            paint.setColor(getResources().getColor(R.color.color_red_down));
            canvas.drawText(currency.getNameCurrency(), (wightCanvas / 8), height += height, paint);

            paint.setColor(getResources().getColor(R.color.color_text));
            canvas.drawText(ask + "/" + bid, (wightCanvas / 2), height, paint);
        }
    }
}
