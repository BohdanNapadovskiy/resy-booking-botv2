package org.example.response.detail;

import java.util.List;

public class User {

  private List<PaymentMethod> paymentMethods;

  // Constructor
  public User() {
  }

  public User(List<PaymentMethod> paymentMethods) {
    this.paymentMethods = paymentMethods;
  }

  // Getter and Setter
  public List<PaymentMethod> getPaymentMethods() {
    return paymentMethods;
  }

  public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
    this.paymentMethods = paymentMethods;
  }

}
