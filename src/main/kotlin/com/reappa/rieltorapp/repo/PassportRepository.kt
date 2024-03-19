package com.reappa.rieltorapp.repo

import com.reappa.rieltorapp.models.PassportMultipartFile
import org.springframework.data.jpa.repository.JpaRepository

interface PassportRepository :JpaRepository<PassportMultipartFile, Long> {
    fun getPassportMultipartFileByMpfAccountId(accountId: Long): PassportMultipartFile?
}