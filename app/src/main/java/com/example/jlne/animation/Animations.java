package com.example.jlne.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Animations {
    final static String TAG = "__Animations";

    public static boolean toggleArrow(View view, Boolean isExpanded) {
        if (!isExpanded) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public static void expand(View view) {
        Animation animation = expandAction(view);
        view.startAnimation(animation);
    }

    private static Animation expandAction(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setVisibility(View.VISIBLE);
        view.getLayoutParams().height = 0;
        final int actualHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (actualHeight * interpolatedTime);
                view.requestLayout();
            }
        };


        animation.setDuration((long) (actualHeight / view.getContext().getResources().getDisplayMetrics().density));

        view.startAnimation(animation);

        return animation;
    }

    public static void collapse(final View view) {
        final int actualHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = actualHeight - (int) (actualHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };

        animation.setDuration((long) (actualHeight/ view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

}
