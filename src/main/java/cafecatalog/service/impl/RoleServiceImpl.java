package cafecatalog.service.impl;

import cafecatalog.exception.DataProcessingException;
import cafecatalog.model.Role;
import cafecatalog.repository.RoleRepository;
import cafecatalog.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.getRoleByRoleName(Role.RoleName.valueOf(roleName)).orElseThrow(
                () -> new DataProcessingException("Role with role name " + roleName + " not found"));
    }
}
