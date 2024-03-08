package com.reappa.rieltorapp.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.Data
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
@Entity
@Data
@Getter
@Setter
@RequiredArgsConstructor
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @get:JvmName("getRoleId")
    val id: Long?,
//    @get:JvmName("getRoleName")
    val roleName: String,
):GrantedAuthority {

    override fun getAuthority(): String {
        return roleName
    }
}
