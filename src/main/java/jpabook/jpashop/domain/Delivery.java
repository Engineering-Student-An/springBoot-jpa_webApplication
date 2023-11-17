package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    // EnumType.ORDINAL : 숫자로 변형되서 들어감 vs EnumType.STRING : string으로 들어감
    // ORDINAL인 경우에 중간에 ENUM값이 추가되면 (READY, XXX, COMP 인 경우) 처리 불가
    // ORDINAL 사용 X !! => 반드시 STRING 사용!!
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;  // READY, COMP [ENUM]
}
