package com.reappa.rieltorapp.services

import com.reappa.rieltorapp.exceptions.PassportMultipartException
import com.reappa.rieltorapp.models.PassportMultipartFile
import com.reappa.rieltorapp.repo.PassportRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PassportService(
    @Autowired
    private val passportRepository: PassportRepository,
) {

    fun getPassportByAccountId(accountId: Long): PassportMultipartFile{
        val passportMultipartFileFromDB = passportRepository
            .getPassportMultipartFileByMpfAccountId(accountId)
            ?: throw PassportMultipartException("Passport multipart file not found with accountId $accountId")
        return passportMultipartFileFromDB
    }

    fun savePassportMultipartFile(passportMultipartFile: PassportMultipartFile): Boolean{
        val passportMultipartFileFromDB = passportRepository
            .getPassportMultipartFileByMpfAccountId(passportMultipartFile.mpfAccountId)
        if (passportMultipartFileFromDB!=null)
            throw PassportMultipartException("Passport multipart file is already registered for accountId ${passportMultipartFileFromDB.mpfAccountId}")
        passportRepository.save(passportMultipartFile)
        return true
    }
}