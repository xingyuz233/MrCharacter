package com.example.xyzhang.testapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by xingyu on 2018/3/8.
 */

public class MyAnimator {

    public static void inputAnimator(final View view, final View newView) {






        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,
                "scaleX",1f, 0.5f);
        animator2.setDuration(200);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
        animator2.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                newView.setVisibility(View.VISIBLE);
                progressAnimator(newView);
                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    public static void recoverInputAnimator(final View newView, final View view) {

        newView.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f,1f );
        animator2.setDuration(200);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    public static void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(2000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }
}
