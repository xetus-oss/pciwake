package com.xetus.pci.wake.notification.mail

import groovy.transform.CompileStatic

import org.springframework.data.jpa.repository.JpaRepository

@CompileStatic
interface SMTPConfigRepository extends JpaRepository<SMTPConfig, Long> {}
