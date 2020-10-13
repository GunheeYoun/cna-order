
package mall.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

//@FeignClient(name="delivery", url="http://delivery:8080")    // Cloud 일때
//@FeignClient(name="delivery", url="http://localhost:8082")  ==> cloud 안쓰고 local 일 경우
@FeignClient(name="delivery", url="${api.delivery.url}")    // application.yml 에서 불러오기
public interface CancellationService {

    @RequestMapping(method= RequestMethod.POST, path="/cancellations")
    public void cancel(@RequestBody Cancellation cancellation);

}