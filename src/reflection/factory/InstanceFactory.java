package reflection.factory;

import reflection.annotation.Inject;
import reflection.annotation.Instance;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InstanceFactory {
    private final Map<String, Object> beans = new HashMap<>();

    //@Instance를 찾고 초기화후 맵에 저장
    public void loadBeans(Class<?> configClass) {
        try {
            Object configInstance = configClass.getConstructor().newInstance();

            for (Method method : configClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Instance.class)) {

                    Instance beanAnnotation = method.getAnnotation(Instance.class);

                    //패키지명까지 같이 출력 - 패키지마다 같은 클래스가 있을수 있으므로!
                    String beanName = beanAnnotation.name().isEmpty()
                            ? method.getReturnType().getName() : beanAnnotation.name();

                    // 어노테이션이 있는 메서드를 호출하여 빈객체 생성
                    Object bean = method.invoke(configInstance);

                    beans.put(beanName, bean);
                }
            }

            for (Object bean : beans.values()) {
                injectDependencies(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 빈반환
    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(beans.get(beanClass.getName()));
    }


    private void injectDependencies(Object bean) {

        Class<?> beanClass = bean.getClass();

        Arrays.stream(beanClass.getDeclaredFields())
                .forEach(field -> {
                    if (field.isAnnotationPresent(Inject.class)) {
                        String beanName = field.getType().getName();
                        Object dependency = beans.get(beanName);

                        if (dependency != null) {

                            field.setAccessible(true);

                            try {
                                field.set(bean, dependency);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

    }
}
