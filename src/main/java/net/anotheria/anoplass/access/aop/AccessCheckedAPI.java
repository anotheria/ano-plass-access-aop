package net.anotheria.anoplass.access.aop;

import net.anotheria.anoplass.access.aop.exception.OperationDeniedException;
import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;

public interface AccessCheckedAPI extends API {

    void checkReadAccess(AccessChecked accessChecked) throws APIException, OperationDeniedException;

    void checkSaveAccess(AccessChecked accessChecked) throws APIException, OperationDeniedException;

    void checkUpdateAccess(AccessChecked accessChecked) throws APIException, OperationDeniedException;

    void checkDeleteAccess(Object obj) throws APIException, OperationDeniedException;
}
