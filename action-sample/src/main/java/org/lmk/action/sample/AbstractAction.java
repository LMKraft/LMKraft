package org.lmk.action.sample;

import org.jahia.bin.Action;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.List;
import java.util.Map;

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
}
