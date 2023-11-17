package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 준영속 엔티티 수정하는 방법 1. 변경 감지 기능 (권장)
     * save 호출 필요 x / em.persist 호출 필요 x
     * 이유 : JPA가 관리하는 item을 DB에서 꺼내옴 -> 영속상태
     *       값 세팅하면 @Transactional에 의해서 commit => 플러쉬 => dirtychecking
     *       => 변경된 내용에 대해서 update 쿼리를 DB에 날림
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        // findItem.change(name, price, stockQuantity); => 이런식으로 아래의 setter 없이
        // 엔티티 안에서 바로 추적할 수 있는 메서드를 만들고 메서드 안에서 값을 변경하는게 더 나음
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
