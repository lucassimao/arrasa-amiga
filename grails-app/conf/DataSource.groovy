dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual'
}
// environment specific settings
environments {

    development {
        dataSource {
            dbCreate = "update"
            dialect= org.hibernate.dialect.MySQL5InnoDBDialect
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://localhost/arrasaamigadev"
            username= 'root'
            password = 'root'
            
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
    test {
        dataSource {
            //slogSql=true
            dialect= org.hibernate.dialect.MySQL5InnoDBDialect
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://localhost/arrasaamigatests"
            username= 'root'
            password = 'root'
        }
    }
    production {
        dataSource {
            dialect= org.hibernate.dialect.MySQL5InnoDBDialect
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://localhost/arrasaamiga"
            username= 'root'
            password = '17/06/1990lucas'
            pooled = true
            properties {
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
