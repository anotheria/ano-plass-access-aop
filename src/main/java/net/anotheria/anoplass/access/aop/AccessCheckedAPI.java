package net.anotheria.anoplass.access.aop;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;

public interface AccessCheckedAPI extends API {

    void checkAccessBefore(Object[] args, int action) throws APIException;

    void checkAccessAfter(AccessChecked accessChecked, int action) throws APIException;

}
