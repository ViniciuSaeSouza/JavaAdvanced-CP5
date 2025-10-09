package consertaJa.ConsertaJa.annotations.clazz;

import consertaJa.ConsertaJa.annotations.interfaces.Unique;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @Autowired
    private EntityManager entityManager;

    private String fieldName;
    private Class<?> domainClass;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.domainClass = constraintAnnotation.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            String jpql = String.format("SELECT COUNT(e) FROM %s e WHERE LOWER(e.%s) = LOWER(:value)",
                    domainClass.getSimpleName(), fieldName);

            Long count = entityManager.createQuery(jpql, Long.class)
                    .setParameter("value", value)
                    .getSingleResult();

            return count == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
