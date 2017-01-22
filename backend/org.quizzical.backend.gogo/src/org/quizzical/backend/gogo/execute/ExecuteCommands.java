package org.quizzical.backend.gogo.execute;

import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Descriptor;

/**
 * Executes a Gogo script.
 */
public class ExecuteCommands {
    public final static String SCOPE = "script";
    public final static String[] FUNCTIONS = new String[] { "execute", "executeAll" };

    // Injected by Felix DM...
    private volatile CommandProcessor m_processor;

    @Descriptor("executes one or more script definition")
    public void executeAll(CommandSession session, @Descriptor("the script definition(s) to execute, which consists of a map with at least a 'script' key") Map<String, String>[] defs) throws Exception {
        if (defs == null || defs.length == 0) {
            throw new IllegalArgumentException("Need at least one script definition!");
        }

        for (Map<String, String> def : defs) {
            String script = def.get("script");
            if (script != null && !"".equals(script.trim())) {
                execute(session, def.get("script"));
            }
            else {
                session.getConsole().printf("Ignoring script definition without 'script': %s...%n", def);
            }
        }
    }

    @Descriptor("executes a script definition")
    public void execute(CommandSession session, @Descriptor("the script to execute, multiple commands should be separated by semicolons") String script) throws Exception {
        CommandSession newSession = m_processor.createSession(session.getKeyboard(), session.getConsole(), System.err);
        try {
            newSession.execute(script);
        }
        finally {
            newSession.close();
        }
    }
}
