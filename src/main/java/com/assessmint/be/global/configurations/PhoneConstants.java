package com.assessmint.be.global.configurations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneConstants {

   final static String DEFAULT_REGION = "ET";
   final static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

   public static Phonenumber.PhoneNumber parsePhoneNumber(String mobileNumber) throws NumberParseException {
      return phoneNumberUtil.parse(mobileNumber, DEFAULT_REGION);
   }

   public static String formatNumber(Phonenumber.PhoneNumber num) {
      return phoneNumberUtil.format(num, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
   }
}
