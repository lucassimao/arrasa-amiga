grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.work.dir = "target/work"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy

// removendo arquivos de fotos de produtos enviados em ambiente de desenvolvimento
grails.war.resources = { stagingDir ->
    delete { fileset(dir: "${stagingDir}/images/produtos", includes: '*') }
}

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

         runtime 'mysql:mysql-connector-java:5.1.25'
         runtime 'commons-codec:commons-codec:1.8'
         compile "org.codehaus.groovy.modules.http-builder:http-builder:0.7"
         test "org.grails:grails-datastore-test-support:1.0.1-grails-2.4"
    }

    plugins {

        // plugins for the compile step
        build ":tomcat:7.0.54"

        compile ":scaffolding:2.1.2"
        compile ":cache:1.1.7"
        compile ':asset-pipeline:1.9.9'
        compile ":spring-security-core:2.0-RC3"
        compile ":browser-detection:0.4.3"
        compile ":quartz:1.0.2"
        compile ":asynchronous-mail:1.1"
        //compile ":mail:1.0.6"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.5.5"
        runtime ":database-migration:1.4.0"
        runtime ":console:1.3"
        runtime ":jquery:1.11.1"


    }
}
