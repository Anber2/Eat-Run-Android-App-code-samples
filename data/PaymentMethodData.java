package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class PaymentMethodData {

    private String CreditCard;
    private String Expirydate;
    private String SecurityCode;
    private String Billingaddress;

    public PaymentMethodData(String creditCard, String expirydate, String securityCode, String billingaddress) {
        CreditCard = creditCard;
        Expirydate = expirydate;
        SecurityCode = securityCode;
        Billingaddress = billingaddress;
    }

    public String getCreditCard() {
        return CreditCard;
    }

    public String getExpirydate() {
        return Expirydate;
    }

    public String getSecurityCode() {
        return SecurityCode;
    }

    public String getBillingaddress() {
        return Billingaddress;
    }




    public void setCreditCard(String creditCard) {
        CreditCard = creditCard;
    }

    public void setExpirydate(String expirydate) {
        Expirydate = expirydate;
    }

    public void setSecurityCode(String securityCode) {
        SecurityCode = securityCode;
    }

    public void setBillingaddress(String billingaddress) {
        Billingaddress = billingaddress;
    }
}
