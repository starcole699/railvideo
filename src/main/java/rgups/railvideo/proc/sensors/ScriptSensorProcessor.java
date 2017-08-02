package rgups.railvideo.proc.sensors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jmx.export.annotation.ManagedResource;
import rgups.railvideo.model.Frame;

import javax.annotation.PostConstruct;
import javax.script.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by Dmitry on 27.07.2017.
 */
@ManagedResource
public class ScriptSensorProcessor extends SensorProcessor implements ResourceLoaderAware {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    String language;

    String source;

    String code;

    ScriptEngine engine;

    Map<String, ?> conf;

    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() throws IOException {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName(language);
        if (null == engine) {
            throw new RuntimeException("Can't find engine for script language: " + language);
        }

        if ((null == source) && (null == code)) {
            throw new RuntimeException("Code or source should be specified.");
        }

        if (null != source) {
            Resource res = resourceLoader.getResource(source);
            code = IOUtils.toString(res.getInputStream(), StandardCharsets.UTF_8);
        }
    }

    public void processEvent(SensorEvent event) {
        StringWriter writer = new StringWriter();

        ScriptContext context = new SimpleScriptContext();

        context.setAttribute("conf", conf, ScriptContext.ENGINE_SCOPE);
        context.setAttribute("host", this, ScriptContext.ENGINE_SCOPE);
        context.setWriter(writer);

        // TODO:
        try {
            engine.eval(code, context);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        System.out.println(writer.toString());
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, ?> getConf() {
        return conf;
    }

    public void setConf(Map<String, ?> conf) {
        this.conf = conf;
    }
}
