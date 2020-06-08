/* 
 * $Id: LoaderConfig.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.web.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Model;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.ModelType;
import es.uc3m.softlab.cbi4api.gbas.event.store.domain.Source;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.ModelFacade;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceException;
import es.uc3m.softlab.cbi4api.gbas.event.store.facade.SourceFacade;
import es.uc3m.softlab.cbi4api.gbas.web.StaticResources;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.XMLConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author averab
 */
@Service(value= StaticResources.SERVICE_NAME_RESOURCE_LOADER_CONFIG)
public class ResourceLoaderConfig implements LoaderConfig {
    /** Log for tracing */
    private static final Logger logger = Logger.getLogger(ResourceLoaderConfig.class);
    /** Event reader to extract the event data from the jms messages */
	@Autowired private SourceFacade sourceFacade;
    /** Event converter to transform  into the database */
    @Autowired private ModelFacade modelFacade;

    /**
     * Setup the GBAS node on a given model defined in path given at arguments.
     * @param model model name to to configure from external file.
     * @throws LoaderConfigurationException if any error occurred during the loader configuration process.
     */
    @Override
    public void setup(String model) throws LoaderConfigurationException {
        String configPath = String.format("%s/%s/%s.xml", StaticResources.ROOT_MODEL_DEFINITION_PATH, model, model);
        XMLConfiguration config;
        List<Source> sourcesToLoad = Lists.newArrayList();
        List<Model> processModelsToLoad = Lists.newArrayList();

        if (configPath == null) {
            throw new LoaderConfigurationException("Can't load configuration from resource. Please provide a valid resource path.");
        }
        // reads the xml config
        try {
            config = new XMLConfiguration(configPath);
        } catch (ConfigurationException cex) {
            logger.error(String.format("The loader configuration in resource path [%s] could not be found", configPath));
            throw new LoaderConfigurationException(String.format("The loader configuration in resource path [%s] could not be found.", configPath), cex);
        }
        // gets new sources
        List<HierarchicalConfiguration> sources = config.configurationsAt(SOURCES);
        for (HierarchicalConfiguration source : sources) {
            Source _source = new Source();
            _source.setId(source.getString(SOURCE_ID));
            _source.setName(source.getString(SOURCE_NAME));
            _source.setDescription(source.getString(SOURCE_DESCRIPTION));
            _source.setInetAddress(source.getString(SOURCE_INET_ADDRESS));
            _source.setPort(source.getInt(SOURCE_PORT));
            sourcesToLoad.add(_source);

            logger.debug(String.format("Read source [%s] from configuration loader.", _source));
        }
        // gets new process models
        List<HierarchicalConfiguration> processModels = config.configurationsAt(PROCESS_MODELS);
        for (HierarchicalConfiguration processModel : processModels) {
            Model _model = new Model();
            _model.setModelSrcId(processModel.getString(PROCESS_MODEL_ID));
            _model.setType(ModelType.PROCESS);
            _model.setName(processModel.getString(PROCESS_MODEL_NAME));
            _model.setDescription(processModel.getString(PROCESS_MODEL_DESCRIPTION));
            Source source = new Source();
            source.setId(processModel.getString(PROCESS_MODEL_SOURCE));
            Source _source = sourceFacade.getSource(source.getId());
            if (_source == null && !sourcesToLoad.contains(source)) {
                logger.error(String.format("Can't load process model [%s]. Invalid source [%s].", _model, source));
                throw new LoaderConfigurationException(String.format("Can't load process model [%s]. Invalid source [%s].", _model, source));
            }
            _model.setSource(source);
            processModelsToLoad.add(_model);

            logger.debug(String.format("Read process model [%s] from configuration loader.", _model));
        }
        // load new data
        try {
            for (Source source : sourcesToLoad) {
                if (!isSourceAlreadyDefined(source.getId())) {
                    sourceFacade.saveSource(source);
                }
            }
            for (Model _model : processModelsToLoad) {
                if (!isModelAlreadyDefined(_model.getModelSrcId(), _model.getSource())) {
                    modelFacade.saveModel(_model);
                }
            }
        } catch (SourceException sex) {
            logger.error(String.format("Can't load the model configuration. Source couldn't be persisted. Details [%s]", sex));
            throw new LoaderConfigurationException(String.format("Can't load the model configuration. Source couldn't be persisted. Details [%s]", sex), sex);
        } catch (ModelException sex) {
            logger.error(String.format("Can't load the model configuration. Process model couldn't be persisted. Details [%s]", sex));
            throw new LoaderConfigurationException(String.format("Can't load the model configuration. Process model couldn't be persisted. Details [%s]", sex), sex);
        }
    }
    /**
     *
     * @param sourceId
     * @return whether the source model exists or not.
     */
    private boolean isSourceAlreadyDefined(String sourceId) {
        Source source = sourceFacade.getSource(sourceId);
        return (source != null);
    }
    /**
     *
     * @param modelId process model identifier
     * @param source source identifier
     * @return whether the process model exists or not.
     * @throws LoaderConfigurationException
     */
    private boolean isModelAlreadyDefined(String modelId, Source source) throws LoaderConfigurationException {
        try {
            Model model = modelFacade.getModel(modelId, source, ModelType.PROCESS);
            return (model != null);
        } catch (ModelException mex) {
            logger.error(String.format("Can't load the model configuration. Details [%s]", mex));
            throw new LoaderConfigurationException(String.format("Can't load the model configuration. Details [%s]", mex), mex);
        }
    }
}