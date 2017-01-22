package org.quizzical.backend.gogo.service.osgi;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.quizzical.backend.gogo.collection.CollectionCommands;
import org.quizzical.backend.gogo.execute.ExecuteCommands;
import org.quizzical.backend.gogo.execute.ScriptExecutor;
import org.quizzical.backend.gogo.service.QuestionCommands;
import org.quizzical.backend.gogo.service.QuizCommands;

public class Activator extends DependencyActivatorBase {

    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
        // Nop
    }

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
        manager.add(createComponent()
            .setInterface(Object.class.getName(), createProps(QuizCommands.SCOPE, QuizCommands.FUNCTIONS))
            .setImplementation(QuizCommands.class)
            );
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(QuestionCommands.SCOPE, QuestionCommands.FUNCTIONS))
                .setImplementation(QuestionCommands.class)
                );

        manager.add(createComponent()
            .setInterface(Object.class.getName(), createProps(CollectionCommands.SCOPE, CollectionCommands.FUNCTIONS))
            .setImplementation(CollectionCommands.class)
        );
        
        String script = System.getProperty("jcr.gogo.script");
        if (script != null) {
            long delay = Long.getLong("jcr.gogo.script.delay", 300L);

            manager.add(createComponent()
                .setImplementation(new ScriptExecutor(script, delay))
                .setComposition("getInstances")
                .add(createServiceDependency()
                    .setService(CommandProcessor.class)
                    .setRequired(true)));
        }
    }

    private Properties createProps(String scope, String[] functions) {
        Properties props = new Properties();
        props.put(CommandProcessor.COMMAND_SCOPE, scope);
        props.put(CommandProcessor.COMMAND_FUNCTION, functions);
        return props;
    }
}
