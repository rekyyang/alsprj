package zju.edu.als.alarm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import zju.edu.als.util.PreCondition;

/**
 * Created by zzq on 2016/11/4.
 */
@Slf4j
public class AlarmConfig {
    private static PropertiesConfiguration configuration;

    public static String ALARM_UID;
    public static String ALARM_KEY;
    public static Long HEARTBEAT_SENSITIVITY;

    static {
        try {
            configuration= new PropertiesConfiguration("cloud-access.properties");
            ALARM_UID = configuration.getString("ALARM_UID");
            ALARM_KEY = configuration.getString("ALARM_KEY");
            HEARTBEAT_SENSITIVITY = configuration.getLong("HEARTBEAT_SENSITIVITY",5000);
        } catch (ConfigurationException e) {
            log.error("Config Init Failed",e);
        }
        PreCondition.checkArgument(ALARM_UID!=null,"AlARM_UID Cannot Be Null");
        PreCondition.checkArgument(ALARM_KEY!=null,"AlARM_KEY Cannot Be Null");

    }
}
