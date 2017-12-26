package cn.touch.demo.logback.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.log4j.helpers.LogLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static ch.qos.logback.core.CoreConstants.CODES_URL;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/19.
 */
public class KafkaLogbackAppender extends AppenderBase<ILoggingEvent> {
    private Encoder<ILoggingEvent> encoder;

    private static final String BOOTSTRAP_SERVERS_CONFIG = ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
    private static final String COMPRESSION_TYPE_CONFIG = ProducerConfig.COMPRESSION_TYPE_CONFIG;
    private static final String ACKS_CONFIG = ProducerConfig.ACKS_CONFIG;
    private static final String RETRIES_CONFIG = ProducerConfig.RETRIES_CONFIG;
    private static final String KEY_SERIALIZER_CLASS_CONFIG = ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
    private static final String VALUE_SERIALIZER_CLASS_CONFIG = ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
    private static final String SECURITY_PROTOCOL = CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
    private static final String SSL_TRUSTSTORE_LOCATION = SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;
    private static final String SSL_TRUSTSTORE_PASSWORD = SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG;
    private static final String SSL_KEYSTORE_TYPE = SslConfigs.SSL_KEYSTORE_TYPE_CONFIG;
    private static final String SSL_KEYSTORE_LOCATION = SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
    private static final String SSL_KEYSTORE_PASSWORD = SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;
    private static final String SASL_KERBEROS_SERVICE_NAME = SaslConfigs.SASL_KERBEROS_SERVICE_NAME;

    private String brokerList = null;
    private String topic = null;
    private String compressionType = null;
    private String securityProtocol = null;
    private String sslTruststoreLocation = null;
    private String sslTruststorePassword = null;
    private String sslKeystoreType = null;
    private String sslKeystoreLocation = null;
    private String sslKeystorePassword = null;
    private String saslKerberosServiceName = null;
    private String clientJaasConfPath = null;
    private String kerb5ConfPath = null;

    private int retries = 0;
    private int requiredNumAcks = Integer.MAX_VALUE;
    private boolean syncSend = false;
    private Producer<byte[], byte[]> producer = null;

    public Producer<byte[], byte[]> getProducer() {
        return producer;
    }

    public String getBrokerList() {
        return brokerList;
    }

    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    public int getRequiredNumAcks() {
        return requiredNumAcks;
    }

    public void setRequiredNumAcks(int requiredNumAcks) {
        this.requiredNumAcks = requiredNumAcks;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean getSyncSend() {
        return syncSend;
    }

    public void setSyncSend(boolean syncSend) {
        this.syncSend = syncSend;
    }

    public String getSslTruststorePassword() {
        return sslTruststorePassword;
    }

    public String getSslTruststoreLocation() {
        return sslTruststoreLocation;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public void setSslTruststoreLocation(String sslTruststoreLocation) {
        this.sslTruststoreLocation = sslTruststoreLocation;
    }

    public void setSslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
    }

    public void setSslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
    }

    public void setSslKeystoreType(String sslKeystoreType) {
        this.sslKeystoreType = sslKeystoreType;
    }

    public void setSslKeystoreLocation(String sslKeystoreLocation) {
        this.sslKeystoreLocation = sslKeystoreLocation;
    }

    public void setSaslKerberosServiceName(String saslKerberosServiceName) {
        this.saslKerberosServiceName = saslKerberosServiceName;
    }

    public void setClientJaasConfPath(String clientJaasConfPath) {
        this.clientJaasConfPath = clientJaasConfPath;
    }

    public void setKerb5ConfPath(String kerb5ConfPath) {
        this.kerb5ConfPath = kerb5ConfPath;
    }

    public String getSslKeystoreLocation() {
        return sslKeystoreLocation;
    }

    public String getSslKeystoreType() {
        return sslKeystoreType;
    }

    public String getSslKeystorePassword() {
        return sslKeystorePassword;
    }

    public String getSaslKerberosServiceName() {
        return saslKerberosServiceName;
    }

    public String getClientJaasConfPath() {
        return clientJaasConfPath;
    }

    public String getKerb5ConfPath() {
        return kerb5ConfPath;
    }

    @Override
    public void start() {
        Properties props = new Properties();
        if (brokerList != null)
            props.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
        if (props.isEmpty()) {
//            throw new ConfigException("The bootstrap servers property should be specified");
            this.addError("The bootstrap servers property should be specified");
        } else if (topic == null) {
//            throw new ConfigException("Topic must be specified by the Kafka log4j appender");
            this.addError("Topic must be specified by the Kafka logback appender");
        } else {
            if (compressionType != null)
                props.put(COMPRESSION_TYPE_CONFIG, compressionType);
            if (requiredNumAcks != Integer.MAX_VALUE)
                props.put(ACKS_CONFIG, Integer.toString(requiredNumAcks));
            if (retries > 0)
                props.put(RETRIES_CONFIG, retries);
            if (securityProtocol != null) {
                props.put(SECURITY_PROTOCOL, securityProtocol);
            }
            if (securityProtocol != null && securityProtocol.contains("SSL") && sslTruststoreLocation != null &&
                    sslTruststorePassword != null) {
                props.put(SSL_TRUSTSTORE_LOCATION, sslTruststoreLocation);
                props.put(SSL_TRUSTSTORE_PASSWORD, sslTruststorePassword);

                if (sslKeystoreType != null && sslKeystoreLocation != null &&
                        sslKeystorePassword != null) {
                    props.put(SSL_KEYSTORE_TYPE, sslKeystoreType);
                    props.put(SSL_KEYSTORE_LOCATION, sslKeystoreLocation);
                    props.put(SSL_KEYSTORE_PASSWORD, sslKeystorePassword);
                }
            }
            if (securityProtocol != null && securityProtocol.contains("SASL") && saslKerberosServiceName != null && clientJaasConfPath != null) {
                props.put(SASL_KERBEROS_SERVICE_NAME, saslKerberosServiceName);
                System.setProperty("java.security.auth.login.config", clientJaasConfPath);
                if (kerb5ConfPath != null) {
                    System.setProperty("java.security.krb5.conf", kerb5ConfPath);
                }
            }

            props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
            props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
            this.producer = getKafkaProducer(props);
            LogLog.debug("Kafka producer connected to " + brokerList);
            LogLog.debug("Logging for topic: " + topic);

            super.start();
        }
    }

    protected Producer<byte[], byte[]> getKafkaProducer(Properties props) {
        return new KafkaProducer<byte[], byte[]>(props);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        byte[] message = null;
        try {
            if (this.encoder == null) {
                message = eventObject.getMessage().getBytes();
            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                this.encoder.init(bos);
                this.encoder.doEncode(eventObject);
                message = bos.toByteArray();
            }
        } catch (IOException e) {
            this.addError("Could not encode send message in Appender [" + this.name + "].", e);
            throw new RuntimeException(e);
        }

        LogLog.debug("[" + new Date(eventObject.getTimeStamp()) + "]" + message);
        Future<RecordMetadata> response = producer.send(new ProducerRecord<byte[], byte[]>(topic, message));
        if (syncSend) {
            try {
                response.get();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void stop() {
        if(this.started) {
            this.started = false;
            producer.close();
        }
    }

    public Encoder<ILoggingEvent> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        addWarn("This appender no longer admits a layout as a sub-component, set an encoder instead.");
        addWarn("To ensure compatibility, wrapping your layout in LayoutWrappingEncoder.");
        addWarn("See also "+CODES_URL+"#layoutInsteadOfEncoder for details");
        LayoutWrappingEncoder<ILoggingEvent> lwe = new LayoutWrappingEncoder<ILoggingEvent>();
        lwe.setLayout(layout);
        lwe.setContext(context);
        this.encoder = lwe;
    }
}
