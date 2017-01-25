package org.quizzical.backend.mail.api;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.osgi.service.log.LogService;


public class LogServiceChute implements LogChute {

    private static final String RUNTIME_LOG_SERVICE_LOGGER = "runtime.log.logsystem.slf4j.logger";

    private LogService logger = null;
    
    public LogServiceChute() {
    	super();
    }
    
    public LogServiceChute(LogService logger) {
    	this();
    	this.logger = logger;
    }    

    /**
     * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime.RuntimeServices)
     */
    public void init(RuntimeServices rs) throws Exception {
    	log(DEBUG_ID, "LogService using logger...");
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String)
     */
    public void log(int level, String message) {
        switch (level) {
            case LogChute.WARN_ID:
                logger.log(LogService.LOG_WARNING,message);
                break;
            case LogChute.INFO_ID:
            	logger.log(LogService.LOG_INFO,message);
                break;
            case LogChute.TRACE_ID:
            	logger.log(LogService.LOG_DEBUG,message);
                break;
            case LogChute.ERROR_ID:
            	logger.log(LogService.LOG_ERROR,message);
                break;
            case LogChute.DEBUG_ID:
            default:
            	logger.log(LogService.LOG_DEBUG,message);
                break;
        }
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String, java.lang.Throwable)
     */
    public void log(int level, String message, Throwable t) {
    	message = message+": \n"+t.getMessage();
    	log(level,message);
    }

    /**
     * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
     */
    public boolean isLevelEnabled(int level) {
    	return true;
    }
}
