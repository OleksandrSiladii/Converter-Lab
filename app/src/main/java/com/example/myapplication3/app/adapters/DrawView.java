package com.example.myapplication3.app.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.Currency;

import java.util.List;

/**
 * Created by sasha on 15.10.2015.
 */
public class DrawView extends View {
    private String title = "qq", city = "qq", region = "qq";
    private Paint paint;
    private List<Currency> currencies;
    private int height = 1;
    private int wightCanvas;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    public DrawView(Context context) {
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wightCanvas = getMeasuredWidth();
        setMeasuredDimension(wightCanvas, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        height = wightCanvas / 9;
        int step = wightCanvas / 9;
        canvas.drawColor(getResources().getColor(R.color.color_of_cardView));

        paint.setColor(getResources().getColor(R.color.black));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(getResources().getDimension(R.dimen.text_size_name_of_bank));
        canvas.drawText(title, (wightCanvas / 10), height, paint);

        paint.setTextSize(getResources().getDimension(R.dimen.text_size_information));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(region, (wightCanvas / 11), height += step, paint);

        if (!city.contains(region))
            canvas.drawText(city, (wightCanvas / 11), height += step, paint);

        paint.setTextSize(getResources().getDimension(R.dimen.text_size_name_of_bank));
        height += step;
        for (Currency currency : currencies) {
            String ask = currency.getAsk().substring(0, 5);
            String bid = currency.getBid().substring(0, 5);

            paint.setColor(getResources().getColor(R.color.color_red_down));
            canvas.drawText(currency.getNameCurrency(), (wightCanvas / 8), height += step, paint);

            paint.setColor(getResources().getColor(R.color.color_text));
            canvas.drawText(ask + "/" + bid, (wightCanvas / 2), height, paint);
        }
        height += step;
    }
}
