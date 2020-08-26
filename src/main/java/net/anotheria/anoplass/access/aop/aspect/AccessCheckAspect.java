package net.anotheria.anoplass.access.aop.aspect;

import net.anotheria.anoplass.access.aop.AccessChecked;
import net.anotheria.anoplass.access.aop.AccessCheckedAPI;
import net.anotheria.anoplass.access.aop.AccessCheckedResult;
import net.anotheria.anoplass.access.aop.annotation.AccessCheckOperation;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Aspect used to intercept {@link AccessCheckOperation} annotated classes.
 */
@Aspect
public class AccessCheckAspect {

    /**
     * Logger.
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor.
     */
    public AccessCheckAspect() {
        logger.debug("AccessCheckAspect(): *** CONSTRUCT ***");
    }

    /**
     * Main point.
     */
    @Around("execution(* *(..)) && (@annotation(annotation))")
    public Object handleAccessCheckOperation(ProceedingJoinPoint pjp, AccessCheckOperation annotation) throws Throwable {
        logger.debug("handleAccessCheckOperation(pjp = [{}])", pjp);

        switch (annotation.action()) {
            case READ:
                return accessCheckRead(pjp, annotation);
            case SAVE:
                return accessCheckSave(pjp, annotation);
            case UPDATE:
                return accessCheckUpdate(pjp, annotation);
            case DELETE:
                return accessCheckDelete(pjp, annotation);
            default:
                return pjp.proceed();
        }
    }

    /**
     * Check read operation.
     */
    private Object accessCheckRead(ProceedingJoinPoint pjp, AccessCheckOperation annotation) throws Throwable {
        Object methodReturnObject = pjp.proceed();

        AccessCheckedAPI accessCheckedAPI = APIFinder.findAPI(annotation.accessApi());
        if (methodReturnObject instanceof AccessCheckedResult) {
            AccessCheckedResult ret = (AccessCheckedResult) methodReturnObject;
            checkReturnList(ret.getAccessCheckedList(), accessCheckedAPI);
        } else
        if (methodReturnObject instanceof List) {
            checkReturnList((List) methodReturnObject, accessCheckedAPI);
        } else {
            checkReturnObject(methodReturnObject, accessCheckedAPI);
        }

        return methodReturnObject;
    }

    /**
     * Check delete operation.
     */
    private Object accessCheckDelete(ProceedingJoinPoint pjp, AccessCheckOperation annotation) throws Throwable {

        Object arg = pjp.getArgs()[0];

        AccessCheckedAPI accessCheckedAPI = APIFinder.findAPI(annotation.accessApi());
        accessCheckedAPI.checkDeleteAccess(arg);

        return pjp.proceed();
    }

    /**
     * Check create/save operation.
     */
    private Object accessCheckSave(ProceedingJoinPoint pjp, AccessCheckOperation annotation) throws Throwable {
        Object arg = pjp.getArgs()[0];

        if (arg instanceof AccessChecked) {
            AccessCheckedAPI accessCheckedAPI = APIFinder.findAPI(annotation.accessApi());
            AccessChecked accessChecked = (AccessChecked) arg;
            accessCheckedAPI.checkSaveAccess(accessChecked);
        }

        return pjp.proceed();
    }

    /**
     * Check update operation.
     */
    private Object accessCheckUpdate(ProceedingJoinPoint pjp, AccessCheckOperation annotation) throws Throwable {
        Object arg = pjp.getArgs()[0];

        if (arg instanceof AccessChecked) {
            AccessCheckedAPI accessCheckedAPI = APIFinder.findAPI(annotation.accessApi());
            AccessChecked accessChecked = (AccessChecked) arg;
            accessCheckedAPI.checkUpdateAccess(accessChecked);
        }

        return pjp.proceed();
    }

    private void checkReturnObject(Object obj, AccessCheckedAPI accessCheckedAPI) throws APIException {

        if (obj instanceof AccessChecked) {
            AccessChecked accessChecked = (AccessChecked) obj;
            accessCheckedAPI.checkReadAccess(accessChecked);
        }

    }

    private <T> void checkReturnList(List<T> list, AccessCheckedAPI accessCheckedAPI) throws APIException {
        for (T obj: list) {
            checkReturnObject(obj, accessCheckedAPI);
        }
    }

}
