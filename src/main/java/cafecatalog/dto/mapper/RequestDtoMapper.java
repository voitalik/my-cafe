package cafecatalog.dto.mapper;

public interface RequestDtoMapper<D, T> {
    T mapToModel(D dto);
}
