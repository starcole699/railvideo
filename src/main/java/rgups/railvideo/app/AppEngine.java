package rgups.railvideo.app;

import rgups.railvideo.core.flow.FlowController;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * Created by Dmitry on 23.05.2017.
 */
public class AppEngine {

    FlowController flow;

    public void init() {
        flow = new FlowController();
    }

    public void runScript(String engineName, String code) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(engineName);
        for (ScriptEngineFactory f : manager.getEngineFactories()) {
            String msg = f.getEngineName() + " " +
                f.getEngineVersion() + "   " +
                f.getLanguageName() + " " +
                f.getLanguageVersion();
            System.out.println(msg);
        }

        engine.put("flow", flow);
    }
}
