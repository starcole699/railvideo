import rgups.railvideo.proc.sensors.ScriptSensorProcessor

beans {
    /*
    angleSensorsProc(ScriptSensorProcessor) {
        sensorName = ".*"
        sensorType = "angle_sensor"
        channel = ".*"
        language = "python"
        source = "classpath:/config/sensors_scripts/base_angle_proc.py"
        conf = [max_warn:20,min_warn:-20,max_alarm:50,min_alarm:-50]
    }
    */

    angleRelativeDivSensorsProc(ScriptSensorProcessor) {
        sensorName = ".*"
        sensorType = "angle_sensor"
        channel = ".*"
        language = "python"
        source = "classpath:/config/sensors_scripts/angle_rel_div.py"
        conf = [warn_threshold:5,error_threshold:10,time_interval_sec:20]
    }

    opticsAndSecuritySensorsProc(ScriptSensorProcessor) {
        sensorName = ".*"
        sensorType = "^(optical_sensor|security_sensor)"
        channel = ".*"
        language = "python"
        source = "classpath:/config/sensors_scripts/logical_sensors.py"
        conf = ["name":"value"]
    }

}