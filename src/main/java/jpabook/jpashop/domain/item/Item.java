package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 추상 메소드(abstract method)란 자식 클래스에서 반드시 오버라이딩해야만 사용할 수 있는 메소드를 의미합니다.
 * 자바에서 추상 메소드를 선언하여 사용하는 목적은 추상 메소드가 포함된 클래스를 상속받는 자식 클래스가 반드시 추상 메소드를 구현하도록 하기 위함입니다.
 * 인터페이스 vs 추상 클래스
 * 인터페이스 : 메소드의 껍데기만 존재, 이유 : 메소드의 구현을 강제하기 위해
 * 추상클래스 : 그 추상클래스를 상속받아서 기능을 이용하고 확장
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // 상속관계 전략 (부모 클래스에서 설정함) : single table
@DiscriminatorColumn(name = "dtype")    // single table이므로 저장할 때 구분이 필요 => 구분을 위해서 넣는 값
@Getter @Setter // => stockQuantity를 변경하기 위해선 setter 사용말고 비즈니스 로직으로 사용해야 함
public abstract class Item {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==// 데이터를 가지고 있는 쪽에 비즈니스 메서드가 있는게 가장 좋음 (응집력 good)
    // 재고를 넣고 빼는 로직은 stockQuantity를 사용함 => Item이라는 entity에 존재 => 핵심 비즈니스 로직을 entity에 직접 넣음

    /**
     * stock 증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     * @param quantity
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
