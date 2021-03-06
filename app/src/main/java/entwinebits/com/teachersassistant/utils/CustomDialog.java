package entwinebits.com.teachersassistant.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import entwinebits.com.teachersassistant.AddNewStudentActivity;
import entwinebits.com.teachersassistant.R;
import entwinebits.com.teachersassistant.adapter.DateChooserAdapter;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class CustomDialog {


/*
    public static void studentMenuDialog(final Context context) {

        try {
			final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.student_menu_dialog_layout);
            dialog.setCancelable(true);

            final TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
            tv_message.setText("mesage");

            final Button live_dialog_cancel = (Button) dialog.findViewById(R.id.live_dialog_cancel);
            live_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final Button live_dialog_recharge = (Button) dialog.findViewById(R.id.live_dialog_recharge);
            live_dialog_recharge.setText("Go to Settings");
            live_dialog_recharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddNewStudentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(Constants.EDIT_STUDENT_DTO, dto);
                    mActivity.startActivityForResult(intent, 120);

//                    Intent mediaFeedIntent = new Intent(context, AddNewStudentActivity.class);
//                    mediaFeedIntent.putExtra(SettingsParentActivity.TAB_POSITION_TAG, SettingsParentActivity.TAB_POSITION_ACCOUNT);
//                    mediaFeedIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    context.startActivity(mediaFeedIntent);
//                    dialog.dismiss();
                }
            });
            dialog.show();

        } catch (Exception e) {
        }

    }
*/


    /*
    * private void addNewBatchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_batch_layout, null);
        builder.setView(dialogView);
        builder.setTitle(getString(R.string.add_new_batch));

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText batch_name_et = (EditText) dialogView.findViewById(R.id.batch_name_et);
        TextView tv_ok = (TextView) dialogView.findViewById(R.id.tv_turn_on);
        TextView tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (batch_name_et.getText() != null) {
                    added_batch_name = batch_name_et.getText().toString();
                    notifyAdapter();
                    Toast.makeText(BatchActivity.this, added_batch_name, Toast.LENGTH_SHORT).show();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }*/

/*
    public String showDialogWithDoubleBtn(final Activity context, boolean cancelable) {

        final String[] batchName = new String[1];
        batchName[0] = new String();
        final Dialog dialog = new Dialog(context);
        try {
//			final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_add_batch_layout);
            dialog.setCancelable(cancelable);

            final EditText batch_name_et = (EditText) dialog.findViewById(R.id.batch_name_et);


            */
/*ImageView iv_dialog = (ImageView) dialog.findViewById(R.id.iv_dialog);
            iv_dialog.setVisibility(View.GONE);

            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            tv_title.setVisibility(View.VISIBLE);
            if (title != null && title.length() > 0) {
                tv_title.setText(title);
            }

            TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
            if (message != null && message.length() > 0) {
                tv_message.setText(message);
            }

            LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv_message.setLayoutParams(Params1);

            TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_turn_on);
            TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
*//*

//            if (leftBtnText != null && leftBtnText.length() > 0) {
//                tv_cancel.setText(leftBtnText);
//            } else


//                tv_cancel.setText("Cancel");
//
//            if (rightBtnText != null && rightBtnText.length() > 0) {
//                tv_ok.setText(rightBtnText);
//            } else
//                tv_ok.setText("OK");
//
            TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_turn_on);
            TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);

            tv_ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(context, batch_name_et.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (batch_name_et.getText().toString() != null ) {
                        batchName[0] = batch_name_et.getText().toString();
                    }
//                    if (rightBtnListener != null) {
//                        rightBtnListener.onClick(v);
//                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            tv_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    if (leftBtnListener != null) {
//                        leftBtnListener.onClick(v);
//                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
//            batchName[0] = "";
        }
//        if (batchName[0] == null) {
//            batchName[0] = "default";
//        }
        return batchName[0];
    }
*/

}
