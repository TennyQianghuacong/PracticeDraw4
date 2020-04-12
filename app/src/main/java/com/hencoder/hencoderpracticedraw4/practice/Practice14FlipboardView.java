package com.hencoder.hencoderpracticedraw4.practice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice14FlipboardView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    int degree;
    int turnDegree;

    ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "turnDegree", 270, -90);
    ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "degree", 0, 30);
    ObjectAnimator animator3 = ObjectAnimator.ofInt(this, "degree", 30, 0);

    AnimatorSet animatorSet = new AnimatorSet();

    public Practice14FlipboardView(Context context) {
        super(context);
    }

    public Practice14FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice14FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        animator1.setDuration(2500);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator2.setInterpolator(new LinearOutSlowInInterpolator());
        animator3.setInterpolator(new AccelerateInterpolator());

        animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(300);
        animatorSet.playSequentially(animator2, animator1, animator3);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animatorSet.end();
    }

    @SuppressWarnings("unused")
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    public void setTurnDegree(int turnDegree) {
        this.turnDegree = turnDegree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        Matrix matrix = new Matrix();

        //上半部分
        canvas.save();
        matrix.setRotate(turnDegree, centerX, centerY);
        canvas.concat(matrix);
        canvas.clipRect(0, 0, getWidth(), centerY);
        matrix.setRotate(-turnDegree, centerX, centerY);
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, x, y, paint);

        canvas.restore();

        //下半部分
        canvas.save();

        matrix.setRotate(turnDegree, centerX, centerY);
        canvas.concat(matrix);
        canvas.clipRect(0, centerY, getWidth(), getHeight());
        matrix.setRotate(-turnDegree, centerX, centerY);
        canvas.concat(matrix);

        camera.save();
        camera.rotateX(degree);
        canvas.rotate(turnDegree, centerX, centerY);
        canvas.translate(centerX, centerY);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        canvas.rotate(-turnDegree, centerX, centerY);
        camera.restore();

        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
    }
}
