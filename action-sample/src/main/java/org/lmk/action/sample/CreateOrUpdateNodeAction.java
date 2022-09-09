package org.lmk.action.sample;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jahia.services.sites.JahiaSitesService;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jahia.api.Constants.EDIT_WORKSPACE;
import static org.jahia.api.Constants.LIVE_WORKSPACE;

@Component(service= Action.class)
public class CreateOrUpdateNodeAction extends AbstractAction{
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateOrUpdateNodeAction.class);

    private static final String FOLDER_NODE_PATH = "contents/counter";
    private static final String COUNTER_LIST_NODENAME = "counterlist";
    // property node name from form, found in definitions.cnd file
    private static final String REDIRECT_PROPERTY_NAME = "redirect";
    private static final String FOLDER_NODE_TYPE = "jnt:contentFolder";
    private static final String COUNTER_LIST_NODE_TYPE = "lmknt:counterNodeList";
    private static final String COUNTER_NODE_TYPE = "lmknt:counterNode";
    // property counter, to increment, found in definitions.cnd file
    private static final String COUNTER_PROPERTY_NAME = "counter";
    private static final String FORM_PARAM = "field1";

    @Activate
    public void activate(){
        setName("createOrUpdateNode");
        setRequireAuthenticatedUser(false);
    }

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        LOGGER.info("Begin ActionResult");
        String currentNodePath = resource.getNodePath();
        ActionResult result = new ActionResult(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        JCRSiteNode site = renderContext.getSite();
        String redirectUrl;

        try {
            // Retrieve redirection page URL
            redirectUrl = getPageUrl(session, currentNodePath, REDIRECT_PROPERTY_NAME);

            // Call function to create or update node in folder
            createOrUpdateNode(parameters, site.getSiteKey());

            LOGGER.info("Not exception caught, seems everything went well :)");
            // Return response as OK, with redirection to specified page
            result = new ActionResult(HttpServletResponse.SC_OK, redirectUrl, true, new JSONObject());
        }catch(RepositoryException re){
            LOGGER.error("Cannot find redirection page node : {}", re.getMessage(), re);
            result = new ActionResult(HttpServletResponse.SC_NOT_FOUND);
        }catch(Exception e){
            LOGGER.error("Error while creating or updating node : {}", e.getMessage(), e);
            result = new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }finally{
            return result;
        }
    }

    private void createOrUpdateNode(Map<String, List<String>> parameters, String siteKey) throws Exception{
        // Retrieve form parameter
        String formProperty = getParam(parameters, FORM_PARAM);
        // Get current user edit session (he needs to have rights on the folder where we're going to create the node)
        JCRSessionWrapper editSession = JCRSessionFactory.getInstance().getCurrentUserSession(EDIT_WORKSPACE);
        // Retrieve the siteKey from the session
        JCRSiteNode siteNode = JahiaSitesService.getInstance().getSiteByKey(siteKey, editSession);
        // List used later for node publication in live mode
        List<String> uuidToPublish = new ArrayList<>();

        try {
            // Create or get nodes folder
            JCRNodeWrapper nodesFolder = JCRContentUtils.getOrAddPath(editSession, siteNode, FOLDER_NODE_PATH, FOLDER_NODE_TYPE);
            LOGGER.info("Folder found or created : {}", nodesFolder.getName());

            // Create or get counter node list node
            JCRNodeWrapper counterListNode = JCRContentUtils.getOrAddPath(editSession, nodesFolder, COUNTER_LIST_NODENAME, COUNTER_LIST_NODE_TYPE);
            LOGGER.info("Counter node list found or created : {}", counterListNode.getName());

            // Create or get counter node from folder
            String nodename = JCRContentUtils.generateNodeName(formProperty);
            LOGGER.info("Generated nodename : {}", nodename);
            JCRNodeWrapper counterNode = JCRContentUtils.getOrAddPath(editSession, counterListNode, nodename, COUNTER_NODE_TYPE);
            LOGGER.info("Node found or created : {}", counterNode.getName());

            if(counterNode.hasProperty(COUNTER_PROPERTY_NAME)){
                counterNode.setProperty(COUNTER_PROPERTY_NAME, (Long.valueOf(counterNode.getPropertyAsString(COUNTER_PROPERTY_NAME)) + 1));
            }else{
                counterNode.setProperty(COUNTER_PROPERTY_NAME, 1L);
            }

            // save edit session
            editSession.save();
            // add the node and the folder to be published (order IS important)
            uuidToPublish.add(counterNode.getIdentifier());
            uuidToPublish.add(counterListNode.getIdentifier());
            uuidToPublish.add(nodesFolder.getIdentifier());
            // publish
            JCRPublicationService.getInstance().publish(uuidToPublish, EDIT_WORKSPACE, LIVE_WORKSPACE, false, true, null);
            LOGGER.info("Folder & Node published ! GJ !");
        }catch(Exception e){
            LOGGER.error("Error while saving or updating node from property {}", formProperty, e);
            throw new Exception(e);
        }
    }
}
