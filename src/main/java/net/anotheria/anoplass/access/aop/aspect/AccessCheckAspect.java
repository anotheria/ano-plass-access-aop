package net.anotheria.anoplass.access.aop.aspect;

import net.anotheria.anoplass.access.aop.AccessChecked;
import net.anotheria.anoplass.access.aop.AccessCheckedAPI;
import net.anotheria.anoplass.access.aop.AccessCheckedResult;
import net.anotheria.anoplass.access.aop.annotation.AccessCheckOperation;
import net.anotheria.anoplass.access.aop.exception.OperationDeniedException;
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

        boolean before = annotation.before();
        boolean after = annotation.after();
        if (!before && !after) {
            throw new OperationDeniedException("Before or/and After access check should be defined");
        }

        if (before) {
            accessCheckBefore(pjp, annotation);
        }

        Object methodReturnObject = pjp.proceed();

        if (after) {
            accessCheckAfter(methodReturnObject, annotation);
        }

        return methodReturnObject;
    }

    /**
     * Check before operation.
     */
    private void accessCheckBefore(ProceedingJoinPoint pjp, AccessCheckOperation annotation) throws Throwable {
        Object[] args = pjp.getArgs();
        int action = annotation.action();

        AccessCheckedAPI accessCheckedAPI = APIFinder.findAPI(annotation.accessApi());
        accessCheckedAPI.checkAccessBefore(args, action);
    }

    /**
     * Check operation's result
     */
    private void accessCheckAfter(Object methodReturnObject, AccessCheckOperation annotation) throws Throwable {
        AccessCheckedAPI accessCheckedAPI = APIFinder.findAPI(annotation.accessApi());
        int action = annotation.action();
        if (methodReturnObject instanceof AccessCheckedResult) {
            AccessCheckedResult ret = (AccessCheckedResult) methodReturnObject;
            checkReturnList(ret.getAccessCheckedList(), action, accessCheckedAPI);
        } else
        if (methodReturnObject instanceof List) {
            checkReturnList((List) methodReturnObject, action, accessCheckedAPI);
        } else {
            checkReturnObject(methodReturnObject, action, accessCheckedAPI);
        }
    }

    private void checkReturnObject(Object obj, int action, AccessCheckedAPI accessCheckedAPI) throws APIException {
        if (obj instanceof AccessChecked) {
            AccessChecked accessChecked = (AccessChecked) obj;
            accessCheckedAPI.checkAccessAfter(accessChecked, action);
            return;
        }
        throw new OperationDeniedException("Object is not AccessChecked: " + obj.toString());
    }

    private <T> void checkReturnList(List<T> list, int action, AccessCheckedAPI accessCheckedAPI) throws APIException {
        for (T obj: list) {
            checkReturnObject(obj, action, accessCheckedAPI);
        }
    }

}
