//package com.untralvious.developdemo;
//
//import org.jasypt.encryption.StringEncryptor;
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class JasyptConfig {
//    @Value("${jasypt.encryptor.password}")
//    String passJasypt;
//    @Bean(name = "simpleStringPBEConfig")
//    public SimpleStringPBEConfig simpleStringPBEConfig() {
//        final SimpleStringPBEConfig pbeConfig = new SimpleStringPBEConfig();
//        pbeConfig.setPassword(passJasypt);
//        return pbeConfig;
//    }
//    @Bean(name = "jasyptStringEncryptor")
//    public StringEncryptor encryptor() {
//        final PooledPBEStringEncryptor pbeStringEncryptor = new PooledPBEStringEncryptor();
//        pbeStringEncryptor.setConfig(simpleStringPBEConfig());
//        pbeStringEncryptor.setPoolSize(5);
//        return pbeStringEncryptor;
//    }
//}