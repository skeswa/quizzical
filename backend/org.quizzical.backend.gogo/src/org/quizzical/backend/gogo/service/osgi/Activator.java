package org.quizzical.backend.gogo.service.osgi;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.quizzical.backend.gogo.collection.CollectionCommands;
import org.quizzical.backend.gogo.execute.ExecuteCommands;
import org.quizzical.backend.gogo.execute.ScriptExecutor;
import org.quizzical.backend.gogo.service.ContentItemCommands;
import org.quizzical.backend.gogo.service.LessonCommands;
import org.quizzical.backend.gogo.service.NotifyCommands;
import org.quizzical.backend.gogo.service.ProblemCommands;
import org.quizzical.backend.gogo.service.QuestionCommands;
import org.quizzical.backend.gogo.service.QuizCommands;
import org.quizzical.backend.gogo.service.SMSCommands;
import org.quizzical.backend.gogo.service.TestDesignCommands;
import org.quizzical.backend.gogo.service.UserAnalyticsReportingCommands;
import org.quizzical.backend.gogo.service.UserCommands;

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
                .setInterface(Object.class.getName(), createProps(TestDesignCommands.SCOPE, TestDesignCommands.FUNCTIONS))
                .setImplementation(TestDesignCommands.class)
                );
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(UserCommands.SCOPE, UserCommands.FUNCTIONS))
                .setImplementation(UserCommands.class)
                );
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(UserAnalyticsReportingCommands.SCOPE, UserAnalyticsReportingCommands.FUNCTIONS))
                .setImplementation(UserAnalyticsReportingCommands.class)
                );
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(ProblemCommands.SCOPE, ProblemCommands.FUNCTIONS))
                .setImplementation(ProblemCommands.class)
                );
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(LessonCommands.SCOPE, LessonCommands.FUNCTIONS))
                .setImplementation(LessonCommands.class)
                );        
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(ContentItemCommands.SCOPE, ContentItemCommands.FUNCTIONS))
                .setImplementation(ContentItemCommands.class)
                );
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(SMSCommands.SCOPE, SMSCommands.FUNCTIONS))
                .setImplementation(SMSCommands.class)
                );  
        
        manager.add(createComponent()
                .setInterface(Object.class.getName(), createProps(NotifyCommands.SCOPE, NotifyCommands.FUNCTIONS))
                .setImplementation(NotifyCommands.class)
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
