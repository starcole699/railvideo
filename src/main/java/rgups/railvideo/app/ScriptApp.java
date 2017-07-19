package rgups.railvideo.app;

import rgups.railvideo.model.Frame;

import javax.script.*;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Dmitry on 18.07.2017.
 */
public class ScriptApp {

    static String pycode =
            "for i in range(1,10): " +
            "    print(i)\n" +
            "    host.alarm(str(i))\n" +
            "print(frame)\n" +
            "frame.id = 100";

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
        System.out.println(frame.getId());
    }

    public void alarm(String text) {
        System.out.println("Alarm: " + text);
    }
}
