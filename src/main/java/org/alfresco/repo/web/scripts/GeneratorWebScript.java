package org.alfresco.repo.web.scripts;

import java.util.HashMap;
import java.util.Map;
import org.alfresco.LargeTxnGenerator;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class GeneratorWebScript extends DeclarativeWebScript
{
    private LargeTxnGenerator largeTxnGenerator;

    public void setLargeTxnGenerator(LargeTxnGenerator largeTxnGenerator)
    {
        this.largeTxnGenerator = largeTxnGenerator;
    }

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        Map<String, Object> model = new HashMap<>(1);

        int numDocs = req.getParameter("numDocs") != null ? Integer.parseInt(req.getParameter("numDocs")): 1000;

        largeTxnGenerator.generate(numDocs);

        model.put("success", true);

        return model;
    }
}
