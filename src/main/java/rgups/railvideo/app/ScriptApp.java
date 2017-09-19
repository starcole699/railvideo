package rgups.railvideo.app;

import rgups.railvideo.model.Frame;
import rgups.railvideo.model.alarms.UiAlarm;

import javax.script.*;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Dmitry on 18.07.2017.
 */
public class ScriptApp {

    static String pycode =
            "import logging\n" +
            "import os, sys\n" +
                    "#jtext = java.lang.String('Раз Два Три', 'utf-8')\n" +
                    "print('Раз Два Три'.encode('utf-8'))\n" +
                    "print(sys.path)\n" +
                    "print(os.path)\n" +
            "for i in range(1,10):\n" +
            "    print(i)\n" +
            "    host.alarm(str(i))\n" +
            "    logging.info(i)\n" +
            "print(frame)\n" +
            "frame.path = ('Путь! %s ' + ('sensor_8')).decode('utf-8')";

    static String pycode2 =
            "from rgups.railvideo.model.alarms import UiAlarm\n" +
            "a = UiAlarm.AT_ANGLE_REL_CHANGE\n" +
            "print(a)\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";

    public static void main(String[] args) throws ScriptException, IOException {
        ScriptApp app = new ScriptApp();

        StringWriter writer = new StringWriter(); //ouput will be stored here

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptContext context = new SimpleScriptContext();

        Frame frame = new Frame();
        frame.setPath("path1");
        context.setAttribute("frame", frame, ScriptContext.ENGINE_SCOPE);
        context.setAttribute("host", app, ScriptContext.ENGINE_SCOPE);
        context.setWriter(writer); //configures output redirection
        ScriptEngine engine = manager.getEngineByName("python");
        System.out.println();
        engine.eval(pycode, context);
        System.out.println(writer.toString());
        System.out.println(frame.getPath());
    }

    public void alarm(String text) {
        System.out.println("DbAlarm: " + text);
    }
}
