import rgups.railvideo.proc.sensors.ScriptSensorProcessor

beans {
    angleSensorsProc(ScriptSensorProcessor) {
        sensorName = ".*"
        sensorType = "angle_sensor"
        channel = ".*"
        language = "python"
        source = "classpath:/config/sensors_scripts/base_angle_proc.py"
        conf = [max_warn:20,min_warn:-20,max_alarm:50,min_alarm:-50]
    }
}