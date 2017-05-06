package com.timer.jike.timemanager.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.timer.jike.timemanager.R;


/**
 * Created by heyongjian
 * on 15/8/26.
 */
public class UtilDialog {

    public static MaterialDialog inputDialog(Context context,String title,String content,String hint,String inputPreFill,
                                             MaterialDialog.InputCallback callback){
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(hint, inputPreFill, callback)
                .show();
    }

    public static MaterialDialog buildDialog(
            Context context, String title, String positive, String negative,
            MaterialDialog.SingleButtonCallback callbackP, MaterialDialog.SingleButtonCallback callbackN) {
        return buildDialog(context, title, positive, negative, callbackP, callbackN, null);
    }

    public static MaterialDialog buildDialog(
            Context context, String title, String positive, String negative,
            MaterialDialog.SingleButtonCallback callbackP, MaterialDialog.SingleButtonCallback callbackN,
            View customView,boolean isAutoDismiss) {
        MaterialDialog dialog;
        if (customView != null) {
            dialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .titleColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .contentColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .positiveText(positive)
                    .customView(customView, true)
                    .positiveColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                    .negativeColor(ContextCompat.getColor(context, R.color.dark_light))
                    .backgroundColor(ContextCompat.getColor(context, R.color.white))
                    .onPositive(callbackP)
                    .onNegative(callbackN)
                    .autoDismiss(isAutoDismiss)
                    .show();
        } else {
            dialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .titleColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .contentColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .positiveText(positive)
                    .positiveColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                    .negativeColor(ContextCompat.getColor(context, R.color.dark_light))
                    .backgroundColor(ContextCompat.getColor(context, R.color.white))
                    .onPositive(callbackP)
                    .onNegative(callbackN)
                    .autoDismiss(isAutoDismiss)
                    .show();
        }

        if (negative != null) {
            dialog.setActionButton(DialogAction.NEGATIVE, negative);
            TextView textView = dialog.getActionButton(DialogAction.NEGATIVE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
            textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);
        }

        TextView textView = dialog.getActionButton(DialogAction.POSITIVE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
        textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);

        TextView textViewTitle = dialog.getTitleView();
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));

        if(dialog.getContentView()!= null){
            dialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_3xs));
        }
        return dialog;
    }

    public static MaterialDialog buildDialog(Context context, String title, String positive, String negative,
                                             MaterialDialog.SingleButtonCallback callbackP, MaterialDialog.SingleButtonCallback callbackN, View customView){
        return buildDialog(context,title,positive,negative,callbackP,callbackN,customView,true);
    }

    public static MaterialDialog buildProgressDialog(Context context, String title) {
        return buildProgressDialog(context, title, null, null, null, null);
    }



    public static MaterialDialog buildProgressDialog(
            Context context, String title, String positive, String negative,
            MaterialDialog.SingleButtonCallback callbackP, MaterialDialog.SingleButtonCallback callbackN) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .titleColor(ContextCompat.getColor(context, R.color.dark_holo))
                .contentColor(ContextCompat.getColor(context, R.color.dark_holo))
                .positiveText(positive)
                .widgetColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                .positiveColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                .negativeColor(ContextCompat.getColor(context, R.color.dark_light))
                .backgroundColor(ContextCompat.getColor(context, R.color.white))
                .onPositive(callbackP)
                .onNegative(callbackN)
                .progress(true, 0)
                .cancelable(false)
                .show();

        if (negative != null) {
            dialog.setActionButton(DialogAction.NEGATIVE, negative);
            TextView textView = dialog.getActionButton(DialogAction.NEGATIVE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
            textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);

        }

        TextView textView = dialog.getActionButton(DialogAction.POSITIVE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
        textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);

        TextView textViewTitle = dialog.getTitleView();
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));

        dialog.getProgressBar().getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.size_progress_bar_middle);
        dialog.getProgressBar().getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.size_progress_bar_middle);


        if(dialog.getContentView()!= null){
            dialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_3xs));
        }
        return dialog;
    }

    public static MaterialDialog BuildItemsDialog(Context context, String title, MaterialDialog.ListCallbackSingleChoice itemCallback, CharSequence... items) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .titleColor(ContextCompat.getColor(context, R.color.dark_holo))
                .contentColor(ContextCompat.getColor(context, R.color.dark_holo))
                .backgroundColor(ContextCompat.getColor(context, R.color.white))
                .items(items)
                .itemsColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                .itemsCallbackSingleChoice(-1, itemCallback)
                .dividerColor(ContextCompat.getColor(context, R.color.keypad_separator_line_color))
                .show();

        TextView textViewTitle = dialog.getTitleView();
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));

        if(dialog.getContentView()!= null){
            dialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_3xs));
        }

        return dialog;
    }

    public static void dismiss(MaterialDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (IllegalArgumentException ignore) {
                dialog = null;
            }
        }
    }
    public static MaterialDialog buildDialogStacking(
            Context context, String title, String positive, String negative, String neutral,
            MaterialDialog.SingleButtonCallback callbackP,
            MaterialDialog.SingleButtonCallback callbackN,
            MaterialDialog.SingleButtonCallback callbackNeutral) {
        return buildDialogStacking(context, title, positive, negative, neutral,callbackP, callbackN,callbackNeutral, null);
    }

    /**
     * 创建一个button是竖向排列的dialog，按钮排布顺序 Positive，Negative，Neutral
     * _______________
     * | title        |
     * | ____________ |
     * | |          | |
     * | | content  | |
     * | |__________| |
     * |              |
     * | Positive     |
     * | Negative     |
     * | Neutral      |
     * |______________|
     */
    public static MaterialDialog buildDialogStacking(
            Context context, String title, String positive, String negative, String neutral,
            MaterialDialog.SingleButtonCallback callbackP,
            MaterialDialog.SingleButtonCallback callbackN,
            MaterialDialog.SingleButtonCallback callbackNeutral,
            View customView) {
        MaterialDialog dialog;
        if (customView != null) {
            dialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .titleColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .contentColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .positiveText(positive)
                    .customView(customView, true)
                    .positiveColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                    .negativeColor(ContextCompat.getColor(context, R.color.dark_light))
                    .neutralColor(ContextCompat.getColor(context, R.color.dark_light))
                    .backgroundColor(ContextCompat.getColor(context, R.color.white))
                    .onPositive(callbackP)
                    .onNeutral(callbackNeutral)
                    .onNegative(callbackN)
                    .stackingBehavior(StackingBehavior.ALWAYS)
                    .btnStackedGravity(GravityEnum.START)
                    .show();
        } else {
            dialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .titleColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .contentColor(ContextCompat.getColor(context, R.color.dark_holo))
                    .positiveText(positive)
                    .positiveColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                    .negativeColor(ContextCompat.getColor(context, R.color.brand_color_blue))
                    .neutralColor(ContextCompat.getColor(context, R.color.dark_light))
                    .backgroundColor(ContextCompat.getColor(context, R.color.white))
                    .onPositive(callbackP)
                    .onNegative(callbackN)
                    .onNeutral(callbackNeutral)
                    .stackingBehavior(StackingBehavior.ALWAYS)
                    .btnStackedGravity(GravityEnum.START)
                    .show();
        }

        if (neutral != null){
            dialog.setActionButton(DialogAction.NEUTRAL, neutral);
            TextView textView = dialog.getActionButton(DialogAction.NEUTRAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
            textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);
        }

        if (negative != null) {
            dialog.setActionButton(DialogAction.NEGATIVE, negative);
            TextView textView = dialog.getActionButton(DialogAction.NEGATIVE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
            textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);
        }

        TextView textView = dialog.getActionButton(DialogAction.POSITIVE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));
        textView.setPaddingRelative(textView.getPaddingLeft(),0,textView.getPaddingRight(),0);

        TextView textViewTitle = dialog.getTitleView();
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_xsmall));

        if(dialog.getContentView()!= null){
            dialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.font_3xs));
        }
        return dialog;
    }


}
