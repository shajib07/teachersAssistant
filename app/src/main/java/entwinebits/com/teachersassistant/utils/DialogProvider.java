package entwinebits.com.teachersassistant.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import entwinebits.com.teachersassistant.AddNewStudentActivity;
import entwinebits.com.teachersassistant.R;

/**
 * Created by shajib on 12/28/2016.
 */
public class DialogProvider {

    public static void showDoubleOptionDialog(Context context, String upperText, String lowerText,
                                              final View.OnClickListener upperListener, final View.OnClickListener lowerListener) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.student_menu_dialog_layout);
            dialog.setCancelable(true);

            final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
            dialog_title.setText("Please Choose");

            final Button dialog_upper_btn = (Button) dialog.findViewById(R.id.dialog_upper_btn);
            dialog_upper_btn.setText(upperText);
            dialog_upper_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (upperListener != null) {
                        upperListener.onClick(v);
                    }
                    dialog.dismiss();
                }
            });

            final Button dialog_lower_btn = (Button) dialog.findViewById(R.id.dialog_lower_btn);
            dialog_lower_btn.setText(lowerText);
            dialog_lower_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lowerListener != null) {
                        lowerListener.onClick(v);
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {
        }
    }

//    public static void showPaymentExistDialog(Context context, ) {
//        try {
//            final Dialog dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setContentView(R.layout.student_menu_dialog_layout);
//            dialog.setCancelable(true);
//
//            final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
//            dialog_title.setText("Please Choose");
//
//            final Button dialog_upper_btn = (Button) dialog.findViewById(R.id.dialog_upper_btn);
//            dialog_upper_btn.setText(upperText);
//            dialog_upper_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (upperListener != null) {
//                        upperListener.onClick(v);
//                    }
//                    dialog.dismiss();
//                }
//            });
//
//            final Button dialog_lower_btn = (Button) dialog.findViewById(R.id.dialog_lower_btn);
//            dialog_lower_btn.setText(lowerText);
//            dialog_lower_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (lowerListener != null) {
//                        lowerListener.onClick(v);
//                    }
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//
//        } catch (Exception e) {
//        }
//    }
}
