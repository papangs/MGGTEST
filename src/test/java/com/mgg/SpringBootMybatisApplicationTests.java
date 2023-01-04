package com.mgg;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;

class SpringBootMybatisApplicationTests {

	@Test
	void contextLoads() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("p1nt4rkau23mggTest"); // encryptor's private key
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String plaintext = "0hts799cn8HTxjuXcDLmRGCXhnqw26tPHPHLq3qDlqXpZ5xNBlTEfKftxPEjxc40oILdKuXIDX3kJV1U1m5oIA==";
        System.out.println("Encrypted key : " + encryptor.decrypt(plaintext));
	}

}
