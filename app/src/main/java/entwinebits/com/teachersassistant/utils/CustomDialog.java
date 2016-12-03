package entwinebits.com.teachersassistant.utils;

/**
 * Created by Nargis Rahman on 12/1/2016.
 */
public class CustomDialog {

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
