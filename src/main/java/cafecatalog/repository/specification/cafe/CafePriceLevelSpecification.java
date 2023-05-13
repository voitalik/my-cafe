package cafecatalog.repository.specification.cafe;

import javax.persistence.criteria.CriteriaBuilder;
import cafecatalog.model.Cafe;
import cafecatalog.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CafePriceLevelSpecification implements SpecificationProvider<Cafe> {
    private static final String FILTER_KEY = "priceLevel";
    private static final String FIELD_NAME = "priceLevel";
    @Override
    public Specification<Cafe> getSpecification(String[] priceLevels) {
        return (root, query, cb) -> {
            CriteriaBuilder.In<String> predicate = cb.in(root.get(FIELD_NAME));
            for (String value : priceLevels) {
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
