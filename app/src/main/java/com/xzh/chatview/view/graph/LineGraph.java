/*
    The Android Not Open Source Project
    Copyright (c) 2014-8-21 wangzheng <iswangzheng@gmail.com>

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    @author wangzheng  DateTime 2014-8-21
 */

package com.xzh.chatview.view.graph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xzh.chatview.R;


public class LineGraph extends View {
    private Line line = new Line();
    private String tipPrefix = "";
    public int mTouchIndex = -1;

    private float bottomPadding = 40, topPadding = 40;
    private float leftPadding = 0, rightPadding = 10;

    private int maxVerticalScaleValue = 5 + 1;
    private int scaleTextColor, tipTextColor, curveColor, verticalLineColor, circleColor, tipRectColor;
    private float scaleTextSize, tipTextSize, curveStrokeWidth, verticalLineStrokeWidth, circleStrokeWidth, circleRadius;


    public LineGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs, defStyle);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs, -1);
    }

    private void initViews(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LineGraph, defStyle, 0);
        scaleTextSize = typedArray.getDimension(R.styleable.LineGraph_scale_text_size, 20);
        scaleTextColor = typedArray.getColor(R.styleable.LineGraph_scale_text_color, getResources().getColor(R.color.c5));
        tipRectColor = typedArray.getColor(R.styleable.LineGraph_tip_rect_color, getResources().getColor(R.color.c8));
        tipTextSize = typedArray.getDimension(R.styleable.LineGraph_tip_text_size, 22);
        tipTextColor = typedArray.getColor(R.styleable.LineGraph_tip_text_color, getResources().getColor(R.color.c12));
        curveStrokeWidth = typedArray.getDimension(R.styleable.LineGraph_curve_stroke_width, 4);
        curveColor = typedArray.getColor(R.styleable.LineGraph_curve_color, getResources().getColor(R.color.c8));
        verticalLineStrokeWidth = typedArray.getDimension(R.styleable.LineGraph_vertical_line_stroke_width, 2);
        verticalLineColor = typedArray.getColor(R.styleable.LineGraph_vertical_line_color, getResources().getColor(R.color.c8));
        circleStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.LineGraph_circle_stroke_width, 3);
        circleColor = typedArray.getColor(R.styleable.LineGraph_circle_color, getResources().getColor(R.color.c8));
        circleRadius = typedArray.getDimensionPixelSize(R.styleable.LineGraph_circle_radius, 7);
        typedArray.recycle();

        bottomPadding = dip2px(getContext(), 20);
        topPadding = dip2px(getContext(), 10);
        leftPadding = dip2px(getContext(), 20);
        rightPadding = dip2px(getContext(), 10);
    }

    public void setLine(Line line) {
        this.line = line;
        postInvalidate();
    }

    public int getSize() {
        return line.getSize();
    }

    @Override
    public void onDraw(Canvas rootCanvas) {
        Bitmap fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(fullImage);
        //刷屏
        refreshScreen(canvas);
        //绘制坐标系
        drawCoordinate(canvas);
        //绘制折线图
        drawCurve(canvas);
        //绘制提示
        drawTipRect(canvas);
        rootCanvas.drawBitmap(fullImage, 0, 0, null);
    }

    private void refreshScreen(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding, paint);
    }

    private float getHorizontalScaleStep() {
        return (getWidth() - leftPadding - rightPadding) / line.getSize();
    }

    private float getVerticalScaleStep() {
        return (getHeight() - bottomPadding - topPadding) / maxVerticalScaleValue;
    }

    private void drawCoordinate(Canvas canvas) {
        //坐标系画笔
        Paint coordinatePaint = new Paint();
        coordinatePaint.setAntiAlias(true);
        coordinatePaint.setStrokeWidth(1);
        coordinatePaint.setColor(getResources().getColor(R.color.c5));
        //坐标系文字画笔
        TextPaint coordinateTextPaint = new TextPaint();
        coordinateTextPaint.setAntiAlias(true);
        coordinateTextPaint.setTextSize(scaleTextSize);
        coordinateTextPaint.setAntiAlias(true);
        coordinateTextPaint.setColor(scaleTextColor);
        coordinateTextPaint.setTextAlign(Align.CENTER);

        //水平的刻度线
        float verticalScaleStep = getVerticalScaleStep();
        coordinateTextPaint.setTextAlign(Align.RIGHT);
        float textHeight = getTextHeight(coordinateTextPaint, "8");
        for (int i = 0; i < maxVerticalScaleValue; i++) {
            float y = getHeight() - bottomPadding - (verticalScaleStep * i);
            canvas.drawLine(leftPadding, y, getWidth() - rightPadding, y, coordinatePaint);
            canvas.drawText(i + "", leftPadding - 13, y + textHeight / 2, coordinateTextPaint);
        }
        //垂直的刻度线
        float horizontalScaleStep = getHorizontalScaleStep();
        for (int i = 0; i < line.getSize(); i++) {
            float x = leftPadding + (horizontalScaleStep * i);
            if (i == 0) {
                canvas.drawLine(x, topPadding, x, getHeight() - bottomPadding, coordinatePaint);
            }
            coordinateTextPaint.setColor(mTouchIndex == i ? verticalLineColor : scaleTextColor);
            coordinateTextPaint.setTextAlign(i == 0 ? Align.LEFT : Align.CENTER);
            canvas.drawText(line.getPoint(i).getX(), x, getHeight() - bottomPadding + textHeight + 10, coordinateTextPaint);
        }
    }

    private void drawCurve(Canvas canvas) {
        Paint curvePaint = new Paint();//曲线画笔
        curvePaint.setColor(curveColor);
        curvePaint.setAntiAlias(true);
        curvePaint.setStrokeWidth(curveStrokeWidth);

        float horizontalScaleStep = getHorizontalScaleStep();
        float lastXPixels = 0, newYPixels = 0;
        float lastYPixels = 0, newXPixels = 0;
        float useHeight = getHeight() - bottomPadding - topPadding;
        for (int i = 0; i < line.getSize(); i++) {
            float yPercent = line.getPoint(i).getY() / maxVerticalScaleValue;
            if (i == 0) {
                lastXPixels = leftPadding + i * horizontalScaleStep;
                lastYPixels = getHeight() - bottomPadding - useHeight * yPercent;
            } else {
                newXPixels = leftPadding + i * horizontalScaleStep;
                newYPixels = getHeight() - bottomPadding - useHeight * yPercent;
                canvas.drawLine(lastXPixels, lastYPixels, newXPixels, newYPixels, curvePaint);
                lastXPixels = newXPixels;
                lastYPixels = newYPixels;
            }
            line.getPoint(i).fLineX = lastXPixels;
            line.getPoint(i).fLineY = lastYPixels;
        }
    }

    private void drawTipRect(Canvas canvas) {
        if (mTouchIndex == -1) return;
        LinePoint point = line.getPoint(mTouchIndex);
        float x = point.fLineX;
        float y = point.fLineY;

        // 描绘竖线
        Paint paint = new TextPaint();
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(verticalLineStrokeWidth);
        paint.setColor(verticalLineColor);
        canvas.drawLine(x, topPadding, x, getHeight() - bottomPadding, paint);

        //描绘交汇圆点
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, circleRadius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleStrokeWidth);
        canvas.drawCircle(x, y, circleRadius, paint);

        float midY = (topPadding + getHeight() - bottomPadding) / 2;
        float midX = (leftPadding + getWidth() - rightPadding) / 2;

        //描绘圆角矩形
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(tipTextSize);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setColor(tipTextColor);
        textPaint.setAntiAlias(true);

        String label = tipPrefix + point.getY();
        float textWidth = textPaint.measureText(label) + 15;
        float textHeight = getTextHeight(textPaint, label) + 8;
        float hMargin = 10;//水平间距
        float vMargin = 8;//垂直间距
        float w = textWidth + hMargin * 2;//宽
        float h = textHeight + vMargin * 2;//高

        RectF rect = new RectF();
        if (x > midX) {
            rect.right = x - hMargin;
            rect.left = x - w;
        } else {
            rect.left = x + hMargin;
            rect.right = x + w;
        }

        if (y > midY) {
            rect.top = y - h;
            rect.bottom = y - vMargin;
        } else {
            rect.bottom = y + h;
            rect.top = y + vMargin;
        }
        Paint roundRectPaint = new Paint();
        roundRectPaint.setColor(tipRectColor);
        roundRectPaint.setStyle(Paint.Style.FILL);
        roundRectPaint.setAntiAlias(true);
        canvas.drawRoundRect(rect, 3, 3, roundRectPaint);

        // 描绘圆角矩形中间的文字
        float roundTextX = (rect.left + rect.right) / 2;
        float roundTextY = (rect.top + rect.bottom + getTextHeight(textPaint, label)) / 2;
        canvas.drawText(label, roundTextX, roundTextY, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchEvent = super.onTouchEvent(event);
        if ((event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN)) {
            float dur = Float.MAX_VALUE;
            // 点多了会慢，如果想优化，请用二叉树将X作为节点
            for (int i = 0; i < line.getSize(); i++) {
                if (Math.abs(line.getPoint(i).fLineX - event.getX()) < dur) {
                    dur = Math.abs(line.getPoint(i).fLineX - event.getX());
                    mTouchIndex = i;
                }
            }
            postInvalidate();
            touchEvent = true;

        }
        return touchEvent;
    }

    public void setTipPrefix(String tipPrefix) {
        this.tipPrefix = tipPrefix + ":";
    }

    public static float getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
