package com.example.myapplication3.app.workers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.example.myapplication3.app.R;
import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;

/**
 * Created by sasha on 15.10.2015.
 */
public class DrawView extends View {
    private Organization organization;
    private String title, city, region;
    private Paint paint;
    private int wightCanvas;
    private int wight, q;

    public DrawView(Context context, GlobalModel globalModel, int position, int wightDisplay) {
        super(context);
        this.wightCanvas = (int) (wightDisplay * 0.7);
                getDataFromModel(globalModel, position);

        paint = new Paint();
    }

    private void getDataFromModel(GlobalModel globalModel, int position) {
        organization = globalModel.getOrganizations().get(position);
        title = organization.getTitle();
        city = Constants.getRealName(globalModel.getCitiesReal(), organization.getCityId());
        region = Constants.getRealName(globalModel.getRegionsReal(), organization.getRegionId());
        q = 220 + (80 * organization.getCurrenciesReal().size());
        if (!city.contains(region)) q += 50;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(wightCanvas, q);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        wight = 0;
        canvas.drawColor(getResources().getColor(R.color.color_of_cardView));

        paint.setColor(getResources().getColor(R.color.black));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(getResources().getDimension(R.dimen.text_size_name_of_bank));
        canvas.drawText(title, (wightCanvas / 10), wight += 70, paint);

        paint.setTextSize(getResources().getDimension(R.dimen.text_size_information));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(region, (wightCanvas / 11), wight += 50, paint);

        if (!city.contains(region))
            canvas.drawText(city, (wightCanvas / 11), wight += 50, paint);

        wight += 100;
        paint.setTextSize(getResources().getDimension(R.dimen.text_size_name_of_bank));
        for (Currency currency : organization.getCurrenciesReal()) {
            String ask = currency.getAsk().substring(0, 5);
            String bid = currency.getBid().substring(0, 5);

            paint.setColor(getResources().getColor(R.color.color_red_down));
            canvas.drawText(currency.getNameCurrency(), (wightCanvas / 8), wight, paint);

            paint.setColor(getResources().getColor(R.color.color_text));
            canvas.drawText(ask + "/" + bid, (wightCanvas / 2), wight, paint);
            wight = wight + 80;
        }
    }
}
