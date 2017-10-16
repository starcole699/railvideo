package rgups.railvideo.proc.sensors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jmx.export.annotation.ManagedResource;
import rgups.railvideo.model.Frame;
import rgups.railvideo.service.SensorStatsService;
import rgups.railvideo.utils.RvRuntimeException;

import javax.annotation.PostConstruct;
import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Dmitry on 27.07.2017.
 */
@ManagedResource
public class ScriptSensorProcessor extends SensorProcessor implements ResourceLoaderAware {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Logger SCRIPT_LOG = LoggerFactory.getLogger("SCRIPT_" + this.getClass().getName());

    String language;

    String source;

    String code;

    ScriptEngine engine;

    Map<String, ?> conf;

    private ResourceLoader resourceLoader;

    @Autowired
    private SensorStatsService sensorStatsService;

    @PostConstruct
    public void init() throws IOException {
        SCRIPT_LOG = LoggerFactory.getLogger("SCRIPT_" + this.getClass().getName() + "_" + language);
        prepareEnginesPaths();

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
        context.setAttribute("slog", SCRIPT_LOG,  ScriptContext.ENGINE_SCOPE);
        context.setAttribute("data", event.getData(),  ScriptContext.ENGINE_SCOPE);
        context.setAttribute("stats", sensorStatsService,  ScriptContext.ENGINE_SCOPE);
        context.setAttribute("s_name", event.getData().getSensorName(),  ScriptContext.ENGINE_SCOPE);
        context.setAttribute("s_type", event.getData().getSensorType(),  ScriptContext.ENGINE_SCOPE);
        context.setAttribute("s_chan", event.getData().getChannel(),  ScriptContext.ENGINE_SCOPE);

        context.setWriter(writer);
        LOG.error(" *** PROCESSING DATA: " + event.getData());
        // TODO:
        try {
            engine.eval(code, context);
        } catch (ScriptException e) {
            if (e.getMessage().contains("SystemExit:")){
                LOG.debug("Script exit.");
            } else {
                LOG.error("Error ", e);
            }
        }
    }

    public void prepareEnginesPaths() {
        if(language.toLowerCase().startsWith("py")){
            String path = getSourceFolder();
            if (null == path) return;

            Set<String> paths = new HashSet<>();
            String pPath = System.getProperty("python.path", null);
            if(null != pPath){
                String[] splittedPaths = pPath.split(Pattern.quote(File.pathSeparator));
                paths.addAll(Arrays.asList(splittedPaths));
            }
            paths.add(path);
            String newPropVal = paths.stream()
                    .map(s -> s.trim())
                    .collect(Collectors.joining(File.pathSeparator));

            LOG.info("python.path set to " + newPropVal);

            System.setProperty("python.path", newPropVal);
        }
    }

    public String getSourceFolder() {
        if (null == source) {
            return null;
        }

        Resource res = resourceLoader.getResource(source);
        try {
            return res.getFile().getParentFile().getAbsolutePath();
        } catch (Exception e) {
            LOG.error("Can't get resource's folder: " + source, e);
            return null;
        }
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
