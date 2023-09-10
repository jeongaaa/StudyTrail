package reflection.service;

import reflection.annotation.Inject;

public class InjectService {
    @Inject
    private MyService myService;

    public void print(){
        myService.print();
    }
}
