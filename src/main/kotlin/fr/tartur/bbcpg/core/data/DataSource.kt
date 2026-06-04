package fr.tartur.bbcpg.core.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import fr.tartur.bbcpg.core.data.config.DatabaseCredentials

class DataSource(credentials: DatabaseCredentials) {
    private val source = HikariDataSource(HikariConfig().run {
        jdbcUrl = credentials.type uri credentials.host
        username = credentials.user
        password = credentials.password
        this
    })

    fun connect() = source.connection
}