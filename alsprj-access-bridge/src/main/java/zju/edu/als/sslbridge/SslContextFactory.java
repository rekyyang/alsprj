package org.juwir.ssl;

import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.Security;
import java.util.Objects;

@Slf4j
public class SslContextFactory {

    private static final String ALGORITHM_SUN_X509="SunX509";
    private static final String KEYSTORE_SERVER= "ssl_certs/alsprj-server.jks";
    private static final String KEYSTORE_CLIENT= "ssl_certs/alsprj-client.jks";
//    private static final String ALGORITHM="ssl.KeyManagerFactory.algorithm";
    private static final String KEYSTORE_TYPE="JKS";
    private static final String KEYSTORE_PASSWORD= "123456";
    private static final String CERT_PASSWORD="123456";

    public static synchronized SslContext sslContextService(){
        FileInputStream inputStream = null;
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);

            inputStream = new FileInputStream(Objects.requireNonNull(
                    SslContextFactory.class.getClassLoader().getResource(KEYSTORE_SERVER)).getFile());
            ks.load(inputStream,KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ALGORITHM_SUN_X509);
            kmf.init(ks,CERT_PASSWORD.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(ALGORITHM_SUN_X509);
            tmf.init(ks);

            return SslContextBuilder
                    .forServer(kmf)
                    .trustManager(tmf)
                    .sslProvider(SslProvider.JDK)
                    .clientAuth(ClientAuth.REQUIRE)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
            } catch (IOException e) {
                log.error("Cannot close keystore file stream ",e);
            }
        }
        throw new IllegalArgumentException("Cannot create sslContext");
    }


    public static synchronized SslContext sslContextClient(){
        FileInputStream inputStream = null;
        try {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);

            inputStream = new FileInputStream(Objects.requireNonNull(
                    SslContextFactory.class.getClassLoader().getResource(KEYSTORE_CLIENT)).getFile());
            ks.load(inputStream,KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ALGORITHM_SUN_X509);
            kmf.init(ks,CERT_PASSWORD.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(ALGORITHM_SUN_X509);
            tmf.init(ks);

            return SslContextBuilder
                    .forClient()
                    .keyManager(kmf)
                    .trustManager(tmf)
                    .sslProvider(SslProvider.JDK)
                    .clientAuth(ClientAuth.REQUIRE)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
            } catch (IOException e) {
                log.error("Cannot close keystore file stream ",e);
            }
        }
        throw new IllegalArgumentException("Cannot create sslContext");

    }
}
