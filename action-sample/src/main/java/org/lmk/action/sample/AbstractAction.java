package org.lmk.action.sample;

import org.jahia.bin.Action;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRPublicationService;
import org.jahia.services.content.JCRSessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jahia.api.Constants.EDIT_WORKSPACE;
import static org.jahia.api.Constants.LIVE_WORKSPACE;

public abstract class AbstractAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAction.class);

    String getPageUrl(JCRSessionWrapper session, String nodePath, String pagePropertyName) throws RepositoryException {
        String pageUrl = null;
        JCRNodeWrapper formNode = session.getNode(nodePath);

        if(formNode != null && formNode.hasProperty(pagePropertyName)){
            Property prop = formNode.getProperty(pagePropertyName);

            if(prop != null && prop.getNode() != null){
                JCRNodeWrapper page = session.getNodeByIdentifier(prop.getNode().getIdentifier());

                if(page != null){
                    pageUrl = page.getUrl();
                    LOGGER.info("Redirection page : {}", pageUrl);
                }
            }
        }
        return pageUrl;
    }

    String getParam(Map<String, List<String>> parameters, String fieldName){
        return getParameter(parameters, fieldName);
    }

    Boolean deleteNode(JCRNodeWrapper node) throws RepositoryException{
        Boolean result = false;

        try {
            if (node != null) {
                JCRSessionWrapper session = node.getSession();
                List<String> uuidToPublish = new ArrayList<>();

                // Adding the node to delete to the publication list
                uuidToPublish.add(node.getIdentifier());

                // Retrieving & adding the parent node to the publication list
                JCRNodeWrapper parentNode = node.getParent();
                uuidToPublish.add(parentNode.getIdentifier());

                // Removing the node and seving the session
                node.remove();
                session.save();

                // Publishing the changes
                JCRPublicationService.getInstance().publish(uuidToPublish, EDIT_WORKSPACE, LIVE_WORKSPACE, false, true, null);
                result = true;
            }
        } catch (RepositoryException re){
            LOGGER.error("Error while deleting node : {}", re.getMessage());
            throw new RepositoryException(re);
        }
        return result;
    }
}
