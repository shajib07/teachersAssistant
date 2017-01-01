package entwinebits.com.teachersassistant.listener;

import entwinebits.com.teachersassistant.model.PaymentDTO;

/**
 * Created by shajib on 12/30/2016.
 */
public interface PaymentUpdateListener {

    void onPaymentUpdate(PaymentDTO dto);
}
