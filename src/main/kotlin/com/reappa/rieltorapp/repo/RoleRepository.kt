package com.reappa.rieltorapp.repo

import com.reappa.rieltorapp.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface RoleRepository : JpaRepository<Role, Long>{
    fun findByRoleName(roleName: String): Role?
}