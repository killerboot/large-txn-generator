package org.alfresco;

import java.util.LinkedList;
import java.util.List;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.security.MutableAuthenticationService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.transaction.TransactionService;
import org.alfresco.util.GUID;
import org.alfresco.util.PropertyMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LargeTxnGenerator
{
    private Log log = LogFactory.getLog(LargeTxnGenerator.class);
    public static final String TEST_USER = "user";
    public static final String TEST_FOLDER_1 = "folder";
    private FileFolderService fileFolderService;
    private PersonService personService;
    private PermissionService permissionService;
    private MutableAuthenticationService authenticationService;
    private NodeService nodeService;
    private TransactionService transactionService;

    public void generate(int numDocs)
    {
        transactionService.getRetryingTransactionHelper().doInTransaction(() ->
        {
            createUser(TEST_USER + "-" + System.currentTimeMillis());
            return null;
        }, false, true);
        NodeRef rootNodeRef = nodeService.getRootNode(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE);

        NodeRef folderRef = transactionService.getRetryingTransactionHelper().doInTransaction(() ->
                nodeService.createNode(
                        rootNodeRef,
                        ContentModel.ASSOC_CHILDREN,
                        QName.createQName(TEST_FOLDER_1 + "-" + GUID.generate()),
                        ContentModel.TYPE_FOLDER).getChildRef(), false, true);

        final List<NodeRef> docs = new LinkedList<>();
        transactionService.getRetryingTransactionHelper().doInTransaction(() ->
        {
            for (int i = 0; i < numDocs; i++)
            {
                String docName = "document-" + i;
                docs.add(
                        fileFolderService.create(
                                folderRef,
                                docName,
                                ContentModel.TYPE_CONTENT).getNodeRef());
                log.debug("Generated document: " + docName);
            }
            return null;
        }, false, true);

        log.info("Generated " + numDocs + " in folder " + folderRef);

        transactionService.getRetryingTransactionHelper().doInTransaction(() ->
        {
            for (NodeRef docRef : docs)
            {
                permissionService.setPermission(docRef, TEST_USER, PermissionService.ALL_PERMISSIONS, true);
                permissionService.setInheritParentPermissions(docRef, false);
                log.debug("Changed permissions on: " + docRef);
            }
            return null;
        }, false, true);
        log.info("Finished changing permissions in folder " + folderRef);
    }

    private void createUser(String username)
    {
        PropertyMap userParams = new PropertyMap();
        userParams.put(ContentModel.PROP_USERNAME, username);
        userParams.put(ContentModel.PROP_FIRSTNAME, username);
        userParams.put(ContentModel.PROP_LASTNAME, username);
        userParams.put(ContentModel.PROP_EMAIL, username + "@alfresco.com");
        userParams.put(ContentModel.PROP_JOBTITLE, "jobTitle");

        personService.createPerson(userParams);
        authenticationService.createAuthentication(username, username.toCharArray());
    }

    public void setFileFolderService(FileFolderService fileFolderService)
    {
        this.fileFolderService = fileFolderService;
    }

    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    public void setPermissionService(PermissionService permissionService)
    {
        this.permissionService = permissionService;
    }

    public void setAuthenticationService(MutableAuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

    public void setTransactionService(TransactionService transactionService)
    {
        this.transactionService = transactionService;
    }
}
