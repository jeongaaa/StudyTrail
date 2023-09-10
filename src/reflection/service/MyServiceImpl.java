package reflection.service;

public class MyServiceImpl implements MyService {
    @Override
    public void print() {
        System.out.println(this.getClass().getSimpleName());
    }
}
