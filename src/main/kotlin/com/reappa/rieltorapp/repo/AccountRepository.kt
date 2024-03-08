package com.reappa.rieltorapp.repo

import com.reappa.rieltorapp.models.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AccountRepository :JpaRepository<Account, Long> {
    fun findByAccountEmail(email: String): Account?
}