package com.reappa.rieltorapp.models

import com.reappa.rieltorapp.models.Role
import jakarta.persistence.*
import lombok.Data
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Data
@Getter
@Setter
@RequiredArgsConstructor
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:JvmName("getAccountId")
    val id: Long?,
    @get:JvmName("getAccountEmail")
    val accountEmail: String,
    @get:JvmName("getAccountPassword")
    val accountPassword: String,
    @get:JvmName("getAccountAuthorities")
    @ManyToMany
    @JoinTable(
        name = "accounts_roles",
        joinColumns = [JoinColumn(name = "accountId")],
        inverseJoinColumns = [JoinColumn(name = "roleId")]
    )
    var authorities: List<Role>,
    @get:JvmName("getAccountIsEnabled")
    val isEnabled: Boolean = true,
    @get:JvmName("getAccountIsNonExpired")
    val isAccountNonExpired: Boolean = true,
    @get:JvmName("getAccountIsNonLocked")
    val isAccountNonLocked: Boolean = true,
    @get:JvmName("getAccountIsCredentialNonExpired")
    val isCredentialsNonExpired: Boolean = true
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getPassword(): String = accountPassword
    override fun getUsername(): String = accountEmail
    override fun isEnabled(): Boolean = isEnabled
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
}