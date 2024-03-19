package com.reappa.rieltorapp.services

import com.reappa.rieltorapp.models.Role
import com.reappa.rieltorapp.repo.RoleRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class RoleService (
    @Autowired
    private val roleRepository: RoleRepository
){
    fun findRoleByRoleName(roleName: String): Role {
        val role: Role? = roleRepository.findByRoleName(roleName)
        if (role != null) {
            return role
        }
        throw RuntimeException("Role not found")
    }

    fun createRole(roleName: String?): Role {
        val role: Role? = roleName?.let { roleRepository.findByRoleName(it) }
        if (role != null) {
            throw RuntimeException("Role already exists")
        }
        return roleRepository.save(Role(null, roleName.orEmpty()))
    }
    fun getAllRoles(): List<Role>{
        return roleRepository.findAll();
    }
    fun checkAuthority(
        authorities: List<GrantedAuthority>,
        checkFor: Role,
        ):Boolean{
        for(authority in authorities){
            if (authority.authority.equals(checkFor.authority)){
                return true
            }
        }
        return false
    }
}
