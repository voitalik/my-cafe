package cafecatalog.repository.specification.cafe;

import javax.persistence.criteria.CriteriaBuilder;
import cafecatalog.model.Cafe;
import cafecatalog.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CafeNoiseLevelSpecification implements SpecificationProvider<Cafe> {
    private static final String FILTER_KEY = "noise";
    private static final String FIELD_NAME = "noiseLevel";
    @Override
    public Specification<Cafe> getSpecification(String[] noiseLevels) {
        return (root, query, cb) -> {
            CriteriaBuilder.In<String> predicate = cb.in(root.get(FIELD_NAME));
            for (String value : noiseLevels) {
                predicate.value(value);
            }
            return predicate;
        };
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
