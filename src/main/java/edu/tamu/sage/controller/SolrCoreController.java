package edu.tamu.sage.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static edu.tamu.weaver.validation.model.BusinessValidationType.CREATE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.DELETE;
import static edu.tamu.weaver.validation.model.BusinessValidationType.UPDATE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.sage.model.Field;
import edu.tamu.sage.model.SolrCore;
import edu.tamu.sage.model.User;
import edu.tamu.sage.model.repo.SolrCoreRepo;
import edu.tamu.sage.service.ProcessorService;
import edu.tamu.weaver.auth.annotation.WeaverUser;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidation;

@RestController
@RequestMapping("/core/solr")
public class SolrCoreController {

    @Autowired
    private SolrCoreRepo solrCoreRepo;

    @Autowired
    private ProcessorService processorService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/test/location")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse testSolrCoreLocation(@WeaverValidatedModel SolrCore solrCore) throws IOException {
        logger.info("Testing SolrCore location: " + solrCore.getName());

        SolrClient solr = new HttpSolrClient(solrCore.getUri());

        ApiResponse response = new ApiResponse(SUCCESS);

        try {
            solr.ping();
            LukeRequest lukeRequest = new LukeRequest();
            lukeRequest.setNumTerms(0);

            LukeResponse lukeResponse = lukeRequest.process(solr);

            Map<String, FieldInfo> fieldInfoMap = lukeResponse.getFieldInfo();

            
            SolrQuery query = new SolrQuery();
            query.set("q", "*");
            
            for (Entry<String, FieldInfo> entry : fieldInfoMap.entrySet()) {
                String fieldName = entry.getKey();
                //FieldInfo fieldInfo = entry.getValue();
                query.addFilterQuery(fieldName+":*");
                query.setFacet(true);
                query.addFacetField(fieldName);               
            }
            
            
            System.out.println(query);
            QueryResponse queryResponse = solr.query(query);
           
            
            //List<FacetField> facetFields = queryResponse.getFacetFields();
            FacetField cnameMainFacetField = queryResponse.getFacetField("name");
            for (Count cnameAndCount : cnameMainFacetField.getValues()) {
                String cnameMain = cnameAndCount.getName();
                System.out.println(cnameMain);
                System.out.println(cnameAndCount.getCount());
            }
            
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            response = new ApiResponse(ERROR, "Error connecting with " + solrCore.getName() + " at URL " + solrCore.getUri());
        } finally {
            solr.close();
        }
        

        return response;
    }

    @RequestMapping("/test/authorization")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse testSolrCoreAuthorization(@WeaverValidatedModel SolrCore solrCore) {
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getAll(@WeaverUser User user) {
        return new ApiResponse(SUCCESS, solrCoreRepo.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = CREATE) })
    public ApiResponse createSolrCore(@WeaverValidatedModel SolrCore solrCore) {
        logger.info("Creating SolrCore: " + solrCore.getName());
        
        //TODO We'll eventually want the Fields and schema mappings to be dynamically configurable, but this will work in the very short term
        Map<String,String> schemaMap = new HashMap<String,String>();
        schemaMap.put("title", "title");
        schemaMap.put("creator", "creator");
        schemaMap.put("created", "created");
        schemaMap.put("subject", "subject");
        schemaMap.put("format", "format");
        schemaMap.put("language", "language");
        schemaMap.put("terms.identifier", "id");
        
        List<Field> fields = new ArrayList<Field>();
        schemaMap.forEach((k,v) -> {
            fields.add(new Field(v,k));
        });
        solrCore.setFields(fields);
        
        return new ApiResponse(SUCCESS, solrCoreRepo.create(solrCore));
    }

    // Why do we use POST for update and not put?
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('USER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = UPDATE) })
    public ApiResponse updateSolrCore(@WeaverValidatedModel SolrCore solrCore) {
        logger.info("Updating SolrCore: " + solrCore.getName());
        return new ApiResponse(SUCCESS, solrCoreRepo.update(solrCore));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('USER')")
    @WeaverValidation(business = { @WeaverValidation.Business(value = DELETE) })
    public ApiResponse deleteSolrCore(@WeaverValidatedModel SolrCore solrCore) {
        logger.info("Deleting SolrCore: " + solrCore.getName());
        solrCoreRepo.delete(solrCore);
        return new ApiResponse(SUCCESS);
    }

    @RequestMapping("/testing")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse test() {
        processorService.process();
        return new ApiResponse(SUCCESS);
    }

}
