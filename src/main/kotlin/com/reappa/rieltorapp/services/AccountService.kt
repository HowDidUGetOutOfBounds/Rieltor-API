package com.reappa.rieltorapp.services

import com.reappa.rieltorapp.models.Account
import com.reappa.rieltorapp.models.Role
import com.reappa.rieltorapp.repo.AccountRepository
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AccountService (
    @Autowired
    val accountRepository: AccountRepository,
    ):UserDetailsService{
    
    @Transactional
    override fun loadUserByUsername(email: String): UserDetails {
     val account: Account? = accountRepository.findByAccountEmail(email)
        if (account != null) {
            return account
        };
        throw UsernameNotFoundException("Account not found (email: $email")
    }
    
    fun findAccountByEmail(email: String): Account? {
        val account: Account? = accountRepository.findByAccountEmail(email)
        if (account != null) {
            return account
        }
        return null
    }
    
    @Transactional
    fun saveNewAccount(
        email: String,
        encodedPassword: String,
        role: Role,
        ): Account {
        val accountFromDB: Account? = accountRepository.findByAccountEmail(email)
        if (accountFromDB!=null) {
            throw RuntimeException("Email is already registered")
        }
        val account = Account(null, email, encodedPassword, listOf(role))
        return accountRepository.save(account)
    }
}
