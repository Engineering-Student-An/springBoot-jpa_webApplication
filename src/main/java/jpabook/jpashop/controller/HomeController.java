package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j  // (lombok) Logger log = LoggerFactory.getLogger(getClass()); << 코드 자동 생성
public class HomeController {

    //Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/")    // 첫번째 화면 : 여기로 잡힘
    public String home(){
        log.info("home controller");
        return "home";        // home.html로 찾아가서 타임 리프 파일을 찾아가게 됨
    }
}
