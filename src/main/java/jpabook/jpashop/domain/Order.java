package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // createOrder 사용하지 않고 order 생성하는 것을 막기 위해서
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)  // EAGER 사용 시 N+1 문제 발생
    @JoinColumn(name="member_id")       // mapping을 member_id로 함 => FK : member_id  연관관계 주인!!!!
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL)       // OrderItem의 "order"에 의해서 맵핑됨
    private List<OrderItem> orderItems = new ArrayList<>();

    // cascade =>  orderItems에 데이터를 넣어두고 Order를 저장하면 orderItems도 같이 저장됨
    /*  cascade : persist를 전파함, delete도 마찬가지 (ALL이므로)
    persist(orderItemA)                            =>       x
    persist(orderItemB)                            =>       x
    persist(orderItemC)                            =>       x
    persist(order)                                 =>       persist(order)
    원래는 orderItem을 각각 저장하고 order를 저장해야 됨    =>       orderItem을 따로 저장할 필요 없이 order만 저장하면 됨
    */

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문시간 (시간, 분 까지 있음)

    @Enumerated(EnumType.STRING)
    private OrderStatus status;         // 주문상태 [Order, Cancel] (ENUM)

    /**
     * 연관관계 (편의) 메서드
     * @param member
     */
    public void setMember(Member member){   // order.setMember(member); 코드 1줄로 양방향 연관관계 설정 위해서 (편의성)
        this.member = member;
        member.getOrders().add(this);
    }

    /**
     * 연관관계 (편의) 메서드
     * @param orderItem
     */
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 연관관계 (편의) 메서드
     * @param delivery
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==// => 복잡한 생성은 별도의 생성 메서드가 있으면 효율적임, 생성하는 지점을 변경해야 되면 이 메서드만 변경하면 됨!
    // order가 연관관계를 모두 걸면서 세팅 완료 (주문 상태, 주문 시간 까지 모두)
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){     // ... : 가변인자 => 여러개의 파라미터를 받을 수 있음
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : /*this.*/orderItems) {
             orderItem.cancel();
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     * @return
     */
    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

}
