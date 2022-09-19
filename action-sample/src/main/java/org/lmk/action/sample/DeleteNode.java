package org.lmk.action.sample;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionFactory;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.jahia.api.Constants.EDIT_WORKSPACE;

@Component(service= Action.class)
public class DeleteNode extends AbstractAction{
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteNode.class);

    private static final String PARAM_REDIRECT_TO = "jcrRedirectTo";
    private static final String PARAM_NODE_UUID = "nodeId";

    @Activate
    public void activate(){
        setName("delete");
        setRequireAuthenticatedUser(false);
    }

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        LOGGER.info("Begin ActionResult");

        String redirectUrl = parameters.get(PARAM_REDIRECT_TO).get(0);
        int status = HttpServletResponse.SC_BAD_REQUEST;

        try {
            String nodeId = parameters.get(PARAM_NODE_UUID).get(0);
            JCRNodeWrapper existingNode = getExistingNode(nodeId);

            if(existingNode != null){
                boolean success = deleteNode(existingNode);

                if(success) {
                    status = HttpServletResponse.SC_OK;
                }
            }
        }catch(Exception e){
            LOGGER.error("Error while deleting node : {]", e.getMessage(), e);
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        return new ActionResult(status, redirectUrl, true, null);
    }

    /**
     * Finds existing node by given uuid
     * @param nodeId
     * @return
     */
    private JCRNodeWrapper getExistingNode(String nodeId) {
        JCRNodeWrapper node = null;

        try {
            JCRSessionWrapper session = JCRSessionFactory.getInstance().getCurrentUserSession(EDIT_WORKSPACE);
            node = session.getNodeByIdentifier(nodeId);
        }catch(Exception e){
            LOGGER.error("Error while getting existing node : {}", e.getMessage(), e);
        }
        return node;
    }
}
