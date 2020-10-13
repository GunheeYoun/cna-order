package mall;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String productId;
    private Integer qty;
    //추가 요구사항
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();


    }

    // - Order Aggregate에 Istio Lab을 위한 코드 추가
    // 주문정보 생성시 텀을 줌
    @PrePersist
    public void onPrePersist(){
        try {
            // update 시 부하를 막기위해 텀을 둠
            Thread.currentThread().sleep((long) (800 + Math.random() * 220));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @PostUpdate
    public void onPostUpdate(){
        System.out.println(".................Order Update Hook Event is raised....!!");
    }

    @PreRemove
    public void onPreRemove(){
        OrderCancelled orderCancelled = new OrderCancelled();
        BeanUtils.copyProperties(this, orderCancelled);
        orderCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        mall.external.Cancellation cancellation = new mall.external.Cancellation();
        // 주문 취소 시 관련 정보 저장
        cancellation.setId(this.getId());
        cancellation.setOrderId(this.getId());
        cancellation.setStatus("DeleveryCancelled");
        // mappings goes here
        OrderApplication.applicationContext.getBean(mall.external.CancellationService.class)
            .cancel(cancellation);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }




}
