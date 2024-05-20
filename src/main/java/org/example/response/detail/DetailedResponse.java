package org.example.response.detail;

public class DetailedResponse {

  private User user;
  private BookToken bookToken;

  public DetailedResponse() {
  }

  public DetailedResponse(User user, BookToken bookToken) {
    this.user = user;
    this.bookToken = bookToken;
  }

  // Getter and Setter
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public BookToken getBookToken() {
    return bookToken;
  }

  public void setBookToken(BookToken bookToken) {
    this.bookToken = bookToken;
  }

}
