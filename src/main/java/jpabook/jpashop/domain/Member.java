package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded       // 내장타입 -> Embedded와 Embeddable(Adress.java) 둘 중 하나만 사용해도 됨
    private Address address;

    @OneToMany(mappedBy = "member")     // order 테이블에 있는 "member" 필드에 의해서 mapping 된 것
                                        // mapping 된 거울일 뿐 (연관관계의 주인 x)
    private List<Order> orders = new ArrayList<>();     // 컬랙션은 필드에서 초기화 하자! => NULL 문제에서 안전해짐
                                                        // orders 라는 컬랙션은 가급적이면 변경하지 말 것 => 있는 그대로 사용!
}
