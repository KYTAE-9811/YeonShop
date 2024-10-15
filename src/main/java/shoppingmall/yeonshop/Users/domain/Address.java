package shoppingmall.yeonshop.Users.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String zipCode; // 우편번호
}
