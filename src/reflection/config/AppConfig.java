package reflection.config;

import reflection.annotation.Instance;
import reflection.service.InjectService;
import reflection.service.MyService;
import reflection.service.MyServiceImpl;

public class AppConfig {

    @Instance
    public MyService myService() {
        return new MyServiceImpl(); // 초기화후 생성
    }

    @Instance
    public InjectService injectService() {
        return new InjectService(); // 초기화후 생성
    }
}
