package com.reappa.rieltorapp.services

import com.reappa.rieltorapp.models.Role
import com.reappa.rieltorapp.repo.RoleRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
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

}