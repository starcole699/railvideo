package rgups.railvideo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rgups.railvideo.model.graph.RvLink;
import rgups.railvideo.model.graph.RvNode;
import rgups.railvideo.proc.ImageProcessor;
import rgups.railvideo.proc.RvFlowItem;
import rgups.railvideo.proc.model.RvFlowItemDefinition;
import rgups.railvideo.service.RvItemsService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitry on 29.06.2017.
 */
@RestController
@RequestMapping("/flow")
public class FlowController {

    @Autowired
    RvItemsService itemService;

    @RequestMapping(value = "/comps.json",  method = RequestMethod.GET)
    public ResponseEntity<List<RvFlowItemDefinition>> getComponents() {
        List<RvFlowItemDefinition> ret = itemService.getFlowItemsDefinitions();
        return new ResponseEntity<List<RvFlowItemDefinition>>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/comps/{id}/show_frame",  method = RequestMethod.GET)
    public ResponseEntity<String> ShowItemFrame(@PathVariable("id") final String id) {
        boolean res = itemService.showFrameIfPossible(id);
        return new ResponseEntity<String>("" + res, HttpStatus.OK);
    }

    @RequestMapping(value = "/comps/graph.json",
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public ResponseEntity<Map<String, Object>> getGraph() {
        Map<String,Object> items = itemService.getFlowItems();
        List<RvNode> nodes = new ArrayList<>();
        List<RvLink> links = new ArrayList<>();
        for (Map.Entry<String,Object> entry : items.entrySet()) {
            if (entry.getValue() instanceof ImageProcessor) {
                ImageProcessor ip = (ImageProcessor)(entry.getValue());
                RvNode node = new RvNode(ip.getName(), ip.getProcName());
                for (String s : ip.getAccepts()){
                    String[] accept = s.split("->");
                    RvLink link = new RvLink(accept[0], ip.getName());
                    links.add(link);
                }
                nodes.add(node);
            }
        }

        Map<String,Object> ret = new HashMap<>();
        ret.put("nodes", nodes);
        ret.put("links", links);
        ret.put("beans", items.values());

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/comps/{id}/img", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("id") final String id) throws IOException {
        BufferedImage img = itemService.getCurrentImage(id);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", os);
        return os.toByteArray();
    }


}
