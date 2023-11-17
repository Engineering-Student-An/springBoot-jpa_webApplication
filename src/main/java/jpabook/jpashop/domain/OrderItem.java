package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // => protected OrderItem() { } 코드를 자동 생성 (createOrderItem을 사용하지 않고 orderItem 생성하는 것을 막기 위해 protected)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)  // EAGER 사용 시 N+1 문제 발생
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)  // EAGER 사용 시 N+1 문제 발생
    @JoinColumn(name="order_id")        // FK : order_id
    private Order order;

    private int orderPrice;     // 주문 가격
    private int count;          // 주문 수량

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문 상품 전체 가격 조회
     * @return int
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
